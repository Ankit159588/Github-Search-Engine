function SearchBar() {
  return (
    <div className="flex items-center gap-3 px-6 py-3">
      <input
        type="text"
        placeholder="search classes, methods, annotations..."
        className="flex-1 rounded-md border border-neutral-800 bg-neutral-900 px-3 py-2 font-mono text-sm text-neutral-100 placeholder:text-neutral-500 focus:outline-none focus:border-orange-600"
      />
      <select className="rounded-md border border-neutral-800 bg-neutral-900 px-3 py-2 text-sm text-neutral-300">
        <option>all repos</option>
        <option>torvalds/linux</option>
      </select>
    </div>
  );
}

export default SearchBar;