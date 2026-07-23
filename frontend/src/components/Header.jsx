import { useState } from "react";

const Header = () => {
  const [username, setUsername] = useState("");

  const handleIndexUser = async () => {
    if (!username.trim()) return;

    try {
      const response = await fetch(
        `http://localhost:8080/api/index/${username}`,
      );
      const data = await response.json();
      console.log("Indexed repos:", data);
    } catch (error) {
      console.error("Failed to index user:", error);
    }
  };

  return (
    <header className="flex items-center gap-3 border-b border-neutral-800 px-6 py-3">
      <span className="font-mono text-lg font-bold text-neutral-100">
        grepo
      </span>
      <div className="flex-1" />
      <div className="flex items-center gap-2 rounded-md border border-neutral-800 bg-neutral-900 px-3 py-1.5">
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="github username"
          className="bg-transparent text-sm text-neutral-100 placeholder:text-neutral-500 focus:outline-none"
        />
      </div>
      <button
        onClick={handleIndexUser}
        className="rounded-md bg-orange-600 px-3 py-1.5 text-sm font-semibold text-neutral-100"
      >
        index user
      </button>
    </header>
  );
};

export default Header;
