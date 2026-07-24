function SearchBar({ searchQuery, setSearchQuery, indexedRepos, selectedRepo, setSelectedRepo }) {
  return (
    <div className="flex items-center gap-3 px-6 py-3">
      <input
        type="text"
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        placeholder="search classes, methods, annotations..."
        className="flex-1 rounded-md border border-neutral-800 bg-neutral-900 px-3 py-2 font-mono text-sm text-neutral-100 placeholder:text-neutral-500 focus:outline-none focus:border-orange-600"
      />
      <select
        value={selectedRepo}
        onChange={(e) => setSelectedRepo(e.target.value)}
        className="rounded-md border border-neutral-800 bg-neutral-900 px-3 py-2 text-sm text-neutral-300"
      >
        <option value="">all repos</option>
        {indexedRepos.map((repo) => (
          <option key={repo.Id} value={repo.repoName}>
            {repo.repoName}
          </option>
        ))}
      </select>
    </div>
  );
}

export default SearchBar;