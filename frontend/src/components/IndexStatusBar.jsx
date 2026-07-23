function IndexStatusBar() {
  return (
    <div className="border-b border-neutral-800 bg-neutral-900/50 px-6 py-2">
      <div className="flex items-center justify-between text-xs text-neutral-500">
        <span className="font-mono">indexing torvalds/linux...</span>
        <span className="font-mono">142 files parsed</span>
      </div>
      <div className="mt-1.5 h-1 w-full rounded-full bg-neutral-800">
        <div className="h-1 w-2/3 rounded-full bg-orange-600" />
      </div>
    </div>
  );
}

export default IndexStatusBar;