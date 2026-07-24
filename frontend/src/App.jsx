import { useState, useEffect } from "react";
import Header from "./components/Header";
import IndexStatusBar from "./components/IndexStatusBar";
import RepoPanel from "./components/RepoPanel";
import SearchBar from "./components/SearchBar";
import TypeFilters from "./components/TypeFilters";
import ResultsList from "./components/ResultsList";
import CodeViewer from "./components/CodeViewer";

const App = () => {
  const [isIndexing, setIsIndexing] = useState(false);
  const [indexedUsername, setIndexedUsername] = useState(null);
  const [indexedRepos, setIndexedRepos] = useState([]);

  const [searchQuery, setSearchQuery] = useState("");
  const [activeType, setActiveType] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [selectedResult, setSelectedResult] = useState(null);

  const [selectedRepo, setSelectedRepo] = useState("");

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      const params = new URLSearchParams();
      if (searchQuery.trim()) params.set("query", searchQuery.trim());
      if (activeType) params.set("type", activeType);
      if (selectedRepo) params.set("repo", selectedRepo);

      fetch(`http://localhost:8080/api/search?${params.toString()}`)
        .then((res) => {
          if (!res.ok) throw new Error(`Search request failed: ${res.status}`);
          return res.json();
        })
        .then((data) => {
          setSearchResults(Array.isArray(data) ? data : []);
          setSelectedResult(data[0] ?? null);
        })
        .catch((error) => {
          console.error("Search failed:", error);
          setSearchResults([]);
          setSelectedResult(null);
        });
    }, 400);

    return () => clearTimeout(timeoutId);
  }, [searchQuery, activeType, selectedRepo]);
  return (
    <div className="flex min-h-screen flex-col bg-neutral-950 text-neutral-100">
      <Header
        isIndexing={isIndexing}
        setIsIndexing={setIsIndexing}
        setIndexedUsername={setIndexedUsername}
        setIndexedRepos={setIndexedRepos}
      />
      <IndexStatusBar
        isIndexing={isIndexing}
        indexedUsername={indexedUsername}
        indexedRepoCount={indexedRepos.length}
      />
      {indexedRepos.length > 0 && <RepoPanel repos={indexedRepos} />}
      <SearchBar
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        indexedRepos={indexedRepos}
        selectedRepo={selectedRepo}
        setSelectedRepo={setSelectedRepo}
      />
      <TypeFilters activeType={activeType} setActiveType={setActiveType} />

      <div className="flex flex-1 overflow-hidden">
        <div className="w-1/2 overflow-auto border-r border-neutral-800">
          <ResultsList
            key={searchQuery + activeType + selectedRepo}
            results={searchResults}
            selectedResult={selectedResult}
            onSelectResult={setSelectedResult}
          />
        </div>
        {selectedResult && <CodeViewer selectedResult={selectedResult} />}
      </div>
    </div>
  );
};

export default App;
