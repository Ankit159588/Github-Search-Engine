import ResultRow from "./ResultRow";
import mockResults from "../data/MockResults";

function ResultsList({ selectedResult, onSelectResult }) {

  
  return (
    <div className="flex flex-col">
      {mockResults.map((result) => (
        <ResultRow
          key={result.id}
          result={result}
          isSelected={result.id === selectedResult.id}
          onClick={() => onSelectResult(result)}
        />
      ))}
    </div>
  );
}

export default ResultsList;
