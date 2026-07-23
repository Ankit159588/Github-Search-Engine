function CodeViewer({ selectedResult }) {
  const lines = [
    "package com.linux.kernel.scheduler;",
    "",
    "import java.util.List;",
    "",
    "public class CpuScheduler {",
    "    private List<Task> taskQueue;",
    "",
    "    public void schedule() {",
    "        // pick next task to run",
    "    }",
    "}",
  ];

  const highlightedLine = selectedResult.line;

  return (
    <div className="flex-1 overflow-auto bg-neutral-950">
      <div className="border-b border-neutral-800 px-4 py-2 font-mono text-xs text-neutral-500">
        {selectedResult.repo} / {selectedResult.file}
      </div>
      <div className="font-mono text-sm">
        {lines.map((line, index) => {
          const lineNumber = index + 1;
          const isHighlighted = lineNumber === highlightedLine;

          return (
            <div
              key={lineNumber}
              className={`flex ${
                isHighlighted ? "bg-orange-600/10" : ""
              }`}
            >
              <span className="w-10 shrink-0 select-none text-right pr-3 text-neutral-600">
                {lineNumber}
              </span>
              <span className="text-neutral-300">{line}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default CodeViewer;