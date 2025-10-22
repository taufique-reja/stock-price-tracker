import React, { useEffect, useState } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function StockCard({ stock }) {
  const [time, setTime] = useState(new Date());
  const [countdown, setCountdown] = useState(60);

  // ⏱ Auto update every 60 sec countdown
  useEffect(() => {
    const timer = setInterval(() => {
      setTime(new Date());
      setCountdown((prev) => (prev <= 1 ? 60 : prev - 1));
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const sentiment = stock.change >= 0 ? "Bullish" : "Bearish";
  const color = stock.change >= 0 ? "text-green-600" : "text-red-600";
  const gradientId = stock.change >= 0 ? "greenGradient" : "redGradient";

  const chartData = stock.chart.map((p, i) => ({
    name: `Day ${i + 1}`,
    price: p,
  }));

  return (
    <div className="w-full max-w-md mt-8 p-6 rounded-2xl shadow-lg bg-white/90 border border-gray-200 backdrop-blur">
      <div className="flex items-center justify-between mb-3">
        <h2 className="text-2xl font-bold text-gray-800">{stock.symbol}</h2>
        <span className={`text-sm font-semibold ${color}`}>{sentiment} 📈</span>
      </div>

      <p className="text-gray-500 mb-2">{stock.companyName}</p>
      <p className="text-3xl font-semibold mb-2">₹{stock.price.toFixed(2)}</p>
      <p className={`text-sm ${color}`}>
        Change: {stock.change} ({stock.changePercent})
      </p>

      {/* Chart */}
      <div className="h-52 mt-5">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={chartData}>
            <defs>
              <linearGradient id="greenGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#22c55e" stopOpacity={0.8} />
                <stop offset="95%" stopColor="#86efac" stopOpacity={0.1} />
              </linearGradient>
              <linearGradient id="redGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#ef4444" stopOpacity={0.8} />
                <stop offset="95%" stopColor="#fecaca" stopOpacity={0.1} />
              </linearGradient>
            </defs>
            <XAxis dataKey="name" hide />
            <YAxis hide />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="price"
              stroke={stock.change >= 0 ? "#22c55e" : "#ef4444"}
              strokeWidth={3}
              dot={false}
              fill={`url(#${gradientId})`}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>

      <div className="mt-4 text-gray-500 text-sm text-right">
        Last Updated: {time.toLocaleTimeString()} <br />⏳ Refreshing in{" "}
        {countdown}s
      </div>
    </div>
  );
}
