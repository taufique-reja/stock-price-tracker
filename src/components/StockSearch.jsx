import { useState, useEffect, useCallback } from "react";
import { fetchStockData } from "../services/stockService";
import StockCard from "./StockCard";

export default function StockSearch() {
  const [symbol, setSymbol] = useState("");
  const [stock, setStock] = useState(null);
  const [loading, setLoading] = useState(false);

  // useCallback prevents function recreation (so useEffect dependencies work correctly)
  const handleSearch = useCallback(async () => {
    if (!symbol.trim()) return;
    setLoading(true);
    try {
      const data = await fetchStockData(symbol.trim().toUpperCase());
      setStock(data);
    } catch (error) {
      console.error("Error fetching data:", error);
      alert("Failed to fetch stock data");
    } finally {
      setLoading(false);
    }
  }, [symbol]);

  // Auto refresh every 60 seconds
  useEffect(() => {
    if (!stock) return;
    const interval = setInterval(() => {
      handleSearch();
    }, 60000); // 60 seconds
    return () => clearInterval(interval);
  }, [stock, handleSearch]);

  return (
    <div className="flex flex-col items-center mt-10">
      <h1 className="text-3xl font-bold text-indigo-600 mb-4">
        📊 Stock Tracker
      </h1>
      <div className="flex gap-2">
        <input
          type="text"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
          placeholder="Enter stock symbol (e.g. TCS.BSE)"
          className="border p-2 rounded-md"
        />
        <button
          onClick={handleSearch}
          disabled={loading}
          className={`px-4 py-2 rounded-md text-white ${
            loading ? "bg-gray-400" : "bg-indigo-600 hover:bg-indigo-700"
          }`}
        >
          {loading ? "Loading..." : "Search"}
        </button>
      </div>

      {stock && <StockCard stock={stock} />}
    </div>
  );
}
