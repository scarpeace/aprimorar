type DateFieldProps = {
  label: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
};

export function DateField({ label, value, onChange, className = "" }: Readonly<DateFieldProps>) {
  return (
    <label className={`form-control ${className}`.trim()}>
      <span className="label-text mb-2 text-sm font-medium text-base-content/70">{label}</span>
      <input
        type="date"
        className="input input-bordered w-full"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  );
}
