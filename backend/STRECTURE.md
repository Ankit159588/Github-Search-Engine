# grepo — GitHub User Code Search Engine

A tool that takes a GitHub username, clones their public repositories, parses the Java source code inside them, and builds a searchable index of every class, interface, method, and annotation it finds — across every repo, all in one place.

**Tech stack:** Java · Spring Boot · PostgreSQL (via Supabase) · JGit · JavaParser · React (planned)

---

## Table of Contents

1. [What This Project Actually Does](#what-this-project-actually-does)
2. [Architecture Overview](#architecture-overview)
3. [The Database Schema, Explained](#the-database-schema-explained)
4. [The Pipeline, Step by Step](#the-pipeline-step-by-step)
5. [Project Structure](#project-structure)
6. [Setup & Running Locally](#setup--running-locally)
7. [API Endpoints (Current)](#api-endpoints-current)
8. [Known Limitations](#known-limitations)
9. [Roadmap](#roadmap)

---

## What This Project Actually Does

Point it at any public GitHub username, and it will:

1. Fetch the list of that user's public repositories from GitHub's API.
2. Clone each repository to disk (shallow clone — just the latest snapshot, not full history).
3. Walk every `.java` file in each repo and parse its actual syntax tree (not just text-search — real structural parsing).
4. Extract every class, interface, method, and annotation it finds, along with its exact line numbers and full signature.
5. Store all of it in a relational database, structured so it can be searched, filtered by repo, and filtered by type (class/method/interface/annotation).

The end goal (not yet built) is a search UI where you type something like `createOrder` and instantly see every matching method across every repo you've indexed, with a link straight to the exact file and line.

**Important scope note:** this project only understands **Java** source code. JavaParser (the library doing the structural parsing) has no knowledge of Python, JavaScript, C, or any other language. If a user's repos are written in other languages, their repo metadata will still be indexed, but no classes/methods will be extracted from those files, because there's nothing for a Java parser to find.

---

## Architecture Overview

The system is built as a single Spring Boot application, structured in classic layers:

```
Browser / Frontend
      │
      ▼
 Controllers        ← the "front door"; turns HTTP requests into method calls
      │
      ▼
  Services          ← the actual business logic (talk to GitHub, clone repos, parse files)
      │
      ▼
 Repositories        ← Spring Data JPA interfaces; talk to the database, no SQL written by hand
      │
      ▼
  PostgreSQL          ← hosted on Supabase
```

Every feature in this project (fetching repos, cloning, parsing) follows this exact same shape: a controller receives a request, delegates to a service, the service does the real work and talks to a repository interface when it needs to read/write data.

### Why this layering matters

- **Controllers** know nothing about *how* something is done — only *that* it should happen, based on an incoming request.
- **Services** contain all real logic — calling GitHub's API, running JGit, running JavaParser — and are reusable independent of whether they're triggered by a controller, a scheduled job, or a test.
- **Repositories** are pure data access — Spring Data JPA auto-generates their implementation from the interface's method names (a mechanism called *query derivation*), so no SQL is ever hand-written for basic operations.

This separation means, for example, that the parsing logic (`ParserService`) has no idea it's being triggered by an HTTP request — it could just as easily be triggered by a background job later, with zero changes to the parsing code itself.

---

## The Database Schema, Explained

Three tables, nested like layers of a filing system:

```
GitRepository   (one row per indexed GitHub repo)
     │
     │  one repo → many files
     ▼
SourceFile      (one row per .java file inside that repo)
     │
     │  one file → many code elements
     ▼
CodeElement     (one row per class / method / interface / annotation found in that file)
```

### `git_repository`
| Column | Meaning |
|---|---|
| `id` | Primary key |
| `github_username` | Whose repo this is |
| `repo_name` | The repo's name |
| `clone_url` | HTTPS URL used to clone it via JGit |
| `last_commit_sha` | Reserved for future use — will allow skipping re-cloning/re-parsing unchanged repos |

### `source_files`
| Column | Meaning |
|---|---|
| `id` | Primary key |
| `repository_id` | Foreign key → `git_repository.id` |
| `file_path` | Path relative to the repo root, e.g. `src/main/java/.../AuthController.java` |
| `package_name` | The Java package declared in the file |

### `code_elements`
| Column | Meaning |
|---|---|
| `id` | Primary key |
| `source_file_id` | Foreign key → `source_files.id` |
| `type` | One of `CLASS`, `INTERFACE`, `METHOD`, `ANNOTATION` |
| `name` | The element's own name, e.g. `createOrder` |
| `signature` | Full readable signature, e.g. `public OrderResponseDto createOrder(CreateOrderRequestDto dto, UserDetails currentUser)` |
| `start_line` / `end_line` | Exact line range in the source file |

### Why three tables instead of one

Splitting the data this way avoids duplicating repo/file info on every single method row, and makes natural queries trivial — "everything in this file," "everything in this repo" become simple relationship lookups rather than string matching. It also mirrors the actual pipeline: one repo is cloned → many files are found inside it → many elements are found inside each file. The schema *is* the pipeline, one table per stage.

---

## The Pipeline, Step by Step

### Stage 1 — Fetch repo list from GitHub

`GithubService.fetchAndSaveUserRepos(username)` calls GitHub's public REST API:

```
GET https://api.github.com/users/{username}/repos
```

No authentication required for public data (rate-limited to 60 requests/hour without a token). The raw JSON response is parsed into `GithubRepoDto` objects (a throwaway class matching GitHub's exact field names), then converted into `GitRepository` entities and saved — updating the existing row if that repo was already indexed before, rather than creating duplicates.

**Endpoint:** `GET /api/index/{username}`

### Stage 2 — Clone each repo to disk

`CloneService.cloneRepo(repo)` uses JGit (a pure-Java Git implementation — no dependency on a `git` binary being installed) to shallow-clone (`--depth 1`, latest snapshot only, no full history) the repo into a local folder:

```
cloned-repos/{username}/{repo-name}/
```

If a folder from a previous clone already exists, it's wiped first, so re-running is always safe.

**Endpoint (test/manual trigger):** `GET /api/clone/{repositoryId}`

### Stage 3 — Parse every `.java` file

`ParserService` does two things:

1. **`findJavaFiles(rootDir)`** — recursively walks the cloned folder and returns every file ending in `.java`, no matter how deeply nested in package folders.
2. **`parseAndSave(file, repo, repoRootDir)`** — for each file:
    - Parses it into a `CompilationUnit` (JavaParser's in-memory tree representation of the file) using a dedicated `JavaParser` instance configured for `JAVA_17` syntax (needed to correctly handle modern features like `record` classes).
    - Saves one `SourceFile` row, capturing its relative path and package name.
    - Walks the parsed tree looking for `ClassOrInterfaceDeclaration`, `MethodDeclaration`, and `AnnotationDeclaration` nodes, and saves one `CodeElement` row per match, capturing its name, full signature, and line range.
    - Wraps each file's parsing in a try/catch, so one malformed or unusual file doesn't stop the entire batch — it's logged and skipped instead.

This stage is triggered automatically as part of `GET /api/clone/{repositoryId}` — after cloning, the controller loops over every found `.java` file and calls `parseAndSave` on it.

### Stage 4 — Search (not yet built)

Planned: a `GET /api/search` endpoint querying `CodeElement` rows by name/signature (starting with a simple substring match via Spring Data JPA query derivation, likely upgraded later to Postgres full-text search for better ranking).

---

## Project Structure

```
grepo-backend/
├── pom.xml
├── .env                          ← DB credentials (never committed)
├── .gitignore
├── cloned-repos/                 ← where JGit clones land (gitignored)
└── src/main/java/com/grepo/grepobackend/
    ├── GithubSEngineApplication.java   ← entry point; loads .env before Spring starts
    │
    ├── model/                    ← JPA entities (the 3 tables above)
    │   ├── GitRepository.java
    │   ├── SourceFile.java
    │   ├── CodeElement.java
    │   └── ElementType.java      ← enum: CLASS / INTERFACE / METHOD / ANNOTATION
    │
    ├── dto/                      ← shapes for parsing external API responses
    │   └── GithubRepoDto.java    ← matches GitHub's JSON exactly (snake_case fields)
    │
    ├── repository/                ← Spring Data JPA interfaces (no SQL written by hand)
    │   ├── GitRepositoryRepository.java
    │   ├── SourceFileRepository.java
    │   └── CodeElementRepository.java
    │
    ├── service/                   ← all real logic lives here
    │   ├── GithubService.java     ← Stage 1: fetch + save repos
    │   ├── CloneService.java      ← Stage 2: clone to disk via JGit
    │   └── ParserService.java     ← Stage 3: parse + save code elements
    │
    └── controller/                 ← HTTP entry points
        ├── GithubController.java  ← GET /api/index/{username}
        └── CloneController.java   ← GET /api/clone/{id}
```

---

## Setup & Running Locally

### Prerequisites
- Java 21 (JDK)
- Maven (bundled via `mvnw`/`mvnw.cmd`, no separate install needed)
- A PostgreSQL database (this project uses [Supabase](https://supabase.com)'s free tier)

### 1. Clone this repo
```powershell
git clone <this-repo-url>
cd Github-SEngine
```

### 2. Set up environment variables

Create a `.env` file in the project root (same folder as `pom.xml`):

```
DB_URL=your-supabase-host.pooler.supabase.com
DB_USERNAME=your-supabase-db-username
DB_PASSWORD=your-supabase-db-password
```

These are loaded at startup via the `dotenv-java` library, in `GithubSEngineApplication.main()`, and registered as JVM system properties before Spring Boot itself starts — which is what lets `application.properties` resolve `${DB_URL}` etc.

### 3. Run it

```powershell
.\mvnw.cmd clean spring-boot:run
```

On success, you'll see `Started GithubSEngineApplication...` in the console, and Hibernate will auto-create the three tables in your database on first run (`spring.jpa.hibernate.ddl-auto=update`).

### 4. Try it

```
http://localhost:8080/api/index/{any-github-username}
```
Fetches and saves that user's public repos.

```
http://localhost:8080/api/clone/{repository-id}
```
(Find the `id` in your `git_repository` table after step above.) Clones that specific repo and parses every `.java` file inside it, saving everything to `source_files`/`code_elements`.

---

## API Endpoints (Current)

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/index/{username}` | Fetches a GitHub user's public repos and saves/updates them in the DB |
| `GET` | `/api/clone/{id}` | Clones the repo with this DB id, parses every `.java` file found, and saves the results |

Both are currently GET requests for ease of manual browser testing during development — a production version would likely make `/api/clone/{id}` a `POST` instead, since it has side effects (writes to disk and the DB).

---

## Known Limitations

- **Java-only.** Non-Java files are ignored entirely during parsing; only repo metadata gets stored for non-Java projects.
- **No re-indexing logic yet.** `last_commit_sha` exists on `GitRepository` but isn't used yet — every clone currently wipes and re-clones/re-parses from scratch, even if nothing changed.
- **Synchronous, blocking indexing.** Cloning + parsing happens directly inside the HTTP request — for a user with many/large repos, this could be slow and could time out. No async job queue or progress tracking exists yet.
- **No authentication against GitHub's API.** Limited to 60 requests/hour per IP; fine for development, would need a personal access token for heavier use.
- **No deduplication safeguard on repeated clones within the same request** beyond wiping the folder — running `/api/clone/{id}` twice in a row will currently insert duplicate `SourceFile`/`CodeElement` rows rather than updating existing ones.

---

## Roadmap

- [ ] Build the `/api/search` endpoint (substring search → later, Postgres full-text search)
- [ ] Prevent duplicate rows on repeated re-indexing (check `last_commit_sha`, delete old `SourceFile`/`CodeElement` rows before re-parsing)
- [ ] Move cloning/parsing to an async job with a status endpoint the frontend can poll
- [ ] Wire up the React frontend (already scaffolded separately) to real endpoints instead of mock data
- [ ] Add pagination/filtering to search results (by repo, by element type)
- [ ] Deploy backend + frontend somewhere publicly accessible, with a live demo link





GitHub → clone repos → parse Java files → store in DB → search API → (later) frontend

GitRepository  (one row per GitHub repo, e.g. "torvalds/linux")
│
│  one repo contains many files
▼
SourceFile     (one row per .java file inside that repo)
│
│  one file contains many code elements
▼
CodeElement    (one row per class / method / interface / annotation found in that file)

The `GithubService.java` file is a Spring Boot service responsible for interacting with the GitHub API to fetch a user's repositories and synchronize them with a local database.

Here is a breakdown of how it works:

### 1. External API Interaction
The service uses Spring's `RestClient` (introduced in Spring 6.1) to communicate with GitHub:
*   **Base URL:** It targets `https://api.github.com`.
*   **Request:** The `fetchAndSaveUserRepos(String username)` method sends a `GET` request to `/users/{username}/repos`.
*   **Data Mapping:** The JSON response from GitHub is automatically converted into an array of `GithubRepoDto` objects. The `@JsonIgnoreProperties(ignoreUnknown = true)` in the DTO ensures the application doesn't crash if GitHub sends extra data we don't need.

### 2. Synchronization Logic (Upsert)
Instead of just saving everything every time, the code implements an **"Upsert"** (Update or Insert) logic in the `saveOrUpdate` method:
*   **Check Existence:** It queries the database using `findByGithubUsernameAndRepoName` to see if the repository already exists for that user.
*   **Reuse or Create:**
    *   If it exists, it retrieves the existing record.
    *   If it doesn't, it creates a new `GitRepository` object.
*   **Update Fields:** It updates (or sets) the `githubUsername`, `repoName`, and `cloneUrl`.
*   **Persist:** Finally, it saves the record back to the database.

### 3. Functional Flow
The main method `fetchAndSaveUserRepos` uses Java Streams to process the results:
1.  Converts the array of DTOs to a `List`.
2.  Opens a `stream()`.
3.  Maps each DTO to a saved database entity via `saveOrUpdate`.
4.  Collects the final results into a `List<GitRepository>` and returns it.

### Key Components Involved:
*   **`GithubRepoDto`**: A simple data carrier for the incoming GitHub JSON.
*   **`GitRepository`**: The database entity representing a repository.
*   **`GitRepositoryRepository`**: The JPA interface that handles the actual SQL queries (like `save` and `findBy...`).

In short, this service acts as a bridge: it asks GitHub "what repos does this user have?", then ensures your local database matches that information without creating duplicates.