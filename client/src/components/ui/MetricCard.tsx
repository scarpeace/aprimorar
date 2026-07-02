type MetricCardProps = {
  label: string;
  value?: number;
  tone?: "default" | "success";
};

export function MetricCard({ label, value, tone = "default" }: Readonly<MetricCardProps>) {
  return (
    <div className="rounded-xl border border-base-300 bg-base-100 p-4 shadow-sm">
      <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">{label}</p>
      <p className={`mt-2 text-3xl font-bold ${tone === "success" ? "text-success" : "text-base-content"}`}>
        {value ?? 0}
      </p>
    </div>
  );
}
