function TypeFilters({ activeType, setActiveType }) {
  const types = ["class", "interface", "method", "annotation"];

  const handleClick = (type) => {
    setActiveType((current) => (current === type ? null : type));
  };

  return (
    <div className="flex flex-wrap gap-2 px-6 pb-3">
      {types.map((type) => {
        const isActive = activeType === type;

        return (
          <button
            key={type}
            onClick={() => handleClick(type)}
            className={`rounded-full border px-3 py-1 font-mono text-xs ${
              isActive
                ? "border-orange-600 bg-orange-600/10 text-orange-500"
                : "border-neutral-700 bg-neutral-900 text-neutral-300"
            }`}
          >
            {type}
          </button>
        );
      })}
    </div>
  );
}

export default TypeFilters;