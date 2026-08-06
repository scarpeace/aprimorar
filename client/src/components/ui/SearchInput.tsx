type SearchInputProps = {
  label?: string;
  value: string;
  placeholder: string;
  onChange: (value: string) => void;
  className?: string;
};

export function SearchInput({ label, value, placeholder, onChange, className = "" }: Readonly<SearchInputProps>) {
  return (
    <label className={`form-control grow ${className}`.trim()}>
      {label ? <span className="label-text mb-2 text-sm font-medium text-base-content/70">{label}</span> : null}
      <input
        className="input input-bordered w-full"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
      />
    </label>
  );
}
