import { useState } from "react";

function RepoPanel({ repos }) {
  const [cloningId, setCloningId] = useState(null);
  const [clonedIds, setClonedIds] = useState([]);

  const handleClone = async (repoId) => {
    setCloningId(repoId);

    try {
      const response = await fetch(`http://localhost:8080/api/clone/${repoId}`);
      await response.json();
      setClonedIds((prev) => [...prev, repoId]);
    } catch (error) {
      console.error("Failed to clone repo:", error);
    } finally {
      setCloningId(null);
    }
  };

  return (
    <div className="border-b border-neutral-800">
      <div className="flex items-center justify-between bg-neutral-900/30 px-6 py-1.5">
        <span className="font-mono text-xs text-neutral-500">
          {repos.length} repos indexed
        </span>
        <span className="font-mono text-xs text-neutral-500">
          {clonedIds.length} / {repos.length} cloned
        </span>
      </div>

      <div className="max-h-56 overflow-auto">
        {repos.map((repo) => {
          const isCloning = cloningId === repo.Id;
          const isCloned = clonedIds.includes(repo.Id);

          return (
            <div
              key={repo.Id}
              className={`flex items-center justify-between border-b border-neutral-900 px-6 py-2.5 ${
                isCloned ? "opacity-50" : ""
              }`}
            >
              <span className="font-mono text-sm text-neutral-300">
                {repo.repoName}
              </span>

              {isCloned ? (
                <span className="flex items-center gap-1 font-mono text-xs text-green-500">
                  ✓ cloned
                </span>
              ) : (
                <button
                  onClick={() => handleClone(repo.Id)}
                  disabled={isCloning}
                  className="rounded-md bg-neutral-800 px-3 py-1.5 font-mono text-xs text-neutral-200 hover:bg-neutral-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {isCloning ? "cloning..." : "clone"}
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default RepoPanel;
