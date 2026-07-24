import { useState } from "react";  // NEW
import ResultRow from "./ResultRow";

const RESULTS_PER_PAGE = 10;  // NEW

function ResultsList({ results, selectedResult, onSelectResult }) {
  const [currentPage, setCurrentPage] = useState(0);  // NEW

  const safeResults = Array.isArray(results) ? results : [];

  if (safeResults.length === 0) {
    return (
      <div className="px-6 py-8 text-center font-mono text-sm text-neutral-600">
        no results yet — try indexing a user and searching
      </div>
    );
  }

  const totalPages = Math.ceil(safeResults.length / RESULTS_PER_PAGE);          // NEW
  const startIndex = currentPage * RESULTS_PER_PAGE;                            // NEW
  const pageResults = safeResults.slice(startIndex, startIndex + RESULTS_PER_PAGE); // NEW

  return (
    <div className="flex flex-col">
      <div className="flex items-center justify-between border-b border-neutral-900 px-6 py-1.5 font-mono text-xs text-neutral-500">
        <span>{safeResults.length} results</span>
        <span>page {currentPage + 1} / {totalPages}</span>
      </div>

      {pageResults.map((result) => (                                            // CHANGED: was safeResults.map
        <ResultRow
          key={result.id}
          result={result}
          isSelected={selectedResult?.id === result.id}
          onClick={() => onSelectResult(result)}
        />
      ))}

      {totalPages > 1 && (                                                      // NEW: pagination controls
        <div className="flex items-center justify-center gap-3 border-t border-neutral-900 px-6 py-2">
          <button
            onClick={() => setCurrentPage((p) => Math.max(0, p - 1))}
            disabled={currentPage === 0}
            className="rounded-md border border-neutral-700 px-2 py-1 font-mono text-xs text-neutral-300 disabled:opacity-30 disabled:cursor-not-allowed"
          >
            ← prev
          </button>
          <button
            onClick={() => setCurrentPage((p) => Math.min(totalPages - 1, p + 1))}
            disabled={currentPage >= totalPages - 1}
            className="rounded-md border border-neutral-700 px-2 py-1 font-mono text-xs text-neutral-300 disabled:opacity-30 disabled:cursor-not-allowed"
          >
            next →
          </button>
        </div>
      )}
    </div>
  );
}

export default ResultsList;