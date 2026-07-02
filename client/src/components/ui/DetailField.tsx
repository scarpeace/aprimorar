type DetailFieldProps = {
  label: string;
  value?: string | number | null;
};

export function DetailField({ label, value }: Readonly<DetailFieldProps>) {
  return (
    <div className="space-y-1">
      <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">{label}</p>
      <p className="text-sm text-base-content">{value || "—"}</p>
    </div>
  );
}
