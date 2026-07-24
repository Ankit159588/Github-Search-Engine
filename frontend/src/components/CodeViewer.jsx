import { useState, useEffect } from "react";  

function CodeViewer({ selectedResult }) {
  const [fileContents, setFileContents] = useState("");  
  const [isLoading, setIsLoading] = useState(true);       

  useEffect(() => {                                      
    if (!selectedResult?.sourceFileId) return;

    setIsLoading(true);

    fetch(`http://localhost:8080/api/sourcefile/${selectedResult.sourceFileId}`)
      .then((res) => {
        if (!res.ok) throw new Error(`Failed to load file: ${res.status}`);
        return res.text();
      })
      .then((text) => setFileContents(text))
      .catch((error) => {
        console.error("Failed to load file contents:", error);
        setFileContents("// failed to load file contents");
      })
      .finally(() => setIsLoading(false));
  }, [selectedResult?.sourceFileId]);

  const lines = fileContents.split("\n");   
  const highlightedLine = selectedResult.line;

  return (
    <div className="flex-1 overflow-auto bg-neutral-950">
      <div className="border-b border-neutral-800 px-4 py-2 font-mono text-xs text-neutral-500">
        {selectedResult.repo} / {selectedResult.file}
      </div>

      {isLoading ? (                       
        <div className="px-4 py-3 font-mono text-sm text-neutral-600">
          loading file...
        </div>
      ) : (
        <div className="font-mono text-sm">
          {lines.map((line, index) => {
            const lineNumber = index + 1;
            const isHighlighted = lineNumber === highlightedLine;

            return (
              <div
                key={lineNumber}
                className={`flex ${isHighlighted ? "bg-orange-600/10" : ""}`}
              >
                <span className="w-10 shrink-0 select-none text-right pr-3 text-neutral-600">
                  {lineNumber}
                </span>
                <span className="text-neutral-300 whitespace-pre">{line}</span>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default CodeViewer;