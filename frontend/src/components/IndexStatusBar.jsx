function IndexStatusBar({ isIndexing, indexedUsername, indexedRepoCount }) {
  if (!isIndexing && !indexedUsername) {
    return null;
  }

  return (
    <div className="border-b border-neutral-800 bg-neutral-900/50 px-6 py-2">
      <div className="flex items-center justify-between text-xs text-neutral-500">
        <span className="font-mono">
          {isIndexing
            ? `indexing ${indexedUsername ?? "..."}...`
            : `indexed ${indexedUsername}`}
        </span>
        <span className="font-mono">
          {isIndexing ? "please wait..." : `${indexedRepoCount} repos found`}
        </span>
      </div>
      {isIndexing && (
        <div className="mt-1.5 h-1 w-full overflow-hidden rounded-full bg-neutral-800">
          <div className="h-1 w-1/3 rounded-full bg-orange-600 animate-loading-slide" />
        </div>
      )}
    </div>
  );
}

export default IndexStatusBar;
