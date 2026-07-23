import Header from "./components/Header";
import IndexStatusBar from "./components/IndexStatusBar";
import SearchBar from "./components/SearchBar";
import TypeFilters from "./components/TypeFilters";
import ResultsList from "./components/ResultsList";
import CodeViewer from "./components/CodeViewer";
import { useState } from "react";
import mockResults from "./data/mockResults";

const App = () => {
  const [repositories, setRepositories] = useState([]);
  const [selectedResult, setSelectedResult] = useState(mockResults[0]);

  return (
    <div className="flex min-h-screen flex-col bg-neutral-950 text-neutral-100">
      <Header />
      <IndexStatusBar />
      <SearchBar />
      <TypeFilters />

      <div className="flex flex-1 overflow-hidden">
        <div className="w-1/2 overflow-auto border-r border-neutral-800">
          <ResultsList
            selectedResult={selectedResult}
            onSelectResult={setSelectedResult}
          />
        </div>
        <CodeViewer selectedResult={selectedResult} />
      </div>
    </div>
  );
};

export default App;
