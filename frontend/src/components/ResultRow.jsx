import ResultRow from "./ResultRow";

function ResultsList({ results, selectedResult, onSelectResult }) {
  const safeResults = Array.isArray(results) ? results : [];   // NEW

  if (safeResults.length === 0) {
    return (
      <div className="px-6 py-8 text-center font-mono text-sm text-neutral-600">
        no results yet — try indexing a user and searching
      </div>
    );
  }

  return (
    <div className="flex flex-col">
      {safeResults.map((result) => (
        <ResultRow
          key={result.id}
          result={result}
          isSelected={selectedResult?.id === result.id}
          onClick={() => onSelectResult(result)}
        />
      ))}
    </div>
  );
}

export default ResultsList;