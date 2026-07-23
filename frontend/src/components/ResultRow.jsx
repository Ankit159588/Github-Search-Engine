function ResultRow({ result, isSelected, onClick }) {
  return (
    <div
      onClick={onClick}
      className={`border-b border-neutral-800 px-6 py-3 cursor-pointer ${
        isSelected ? "bg-neutral-900" : "hover:bg-neutral-900/50"
      }`}
    >
      <div className="flex items-center gap-2 text-xs text-neutral-500 font-mono">
        <span>{result.repo}</span>
        <span>/</span>
        <span>{result.file}:{result.line}</span>
      </div>
      <div className="mt-1 flex items-center gap-2">
        <span className="rounded-full border border-neutral-700 bg-neutral-900 px-2 py-0.5 font-mono text-xs text-neutral-300">
          {result.type}
        </span>
        <span className="font-mono text-sm text-neutral-100">
          {result.signature}
        </span>
      </div>
    </div>
  );
}

export default ResultRow;