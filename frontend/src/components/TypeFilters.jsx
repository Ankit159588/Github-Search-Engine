function TypeFilters() {
  return (
    <div className="flex flex-wrap gap-2 px-6 pb-3">
      <button className="rounded-full border border-neutral-700 bg-neutral-900 px-3 py-1 font-mono text-xs text-neutral-300">
        class
      </button>
      <button className="rounded-full border border-neutral-700 bg-neutral-900 px-3 py-1 font-mono text-xs text-neutral-300">
        interface
      </button>
      <button className="rounded-full border border-neutral-700 bg-neutral-900 px-3 py-1 font-mono text-xs text-neutral-300">
        method
      </button>
      <button className="rounded-full border border-neutral-700 bg-neutral-900 px-3 py-1 font-mono text-xs text-neutral-300">
        annotation
      </button>
    </div>
  );
}

export default TypeFilters;