type SelectOption = {
  value: string;
  label: string;
};

type SelectFieldProps = {
  label: string;
  value: string;
  options: readonly SelectOption[];
  onChange: (value: string) => void;
  className?: string;
};

export function SelectField({
  label,
  value,
  options,
  onChange,
  className = "",
}: Readonly<SelectFieldProps>) {
  return (
    <label className={`form-control ${className}`.trim()}>
      <span className="label-text mb-2 text-sm font-medium text-base-content/70">{label}</span>
      <select className="select select-bordered w-full" value={value} onChange={(event) => onChange(event.target.value)}>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </label>
  );
}
