type Option = {
  value: string;
  label: string;
};

type TransacaoFilterSelectProps = {
  label: string;
  value: string;
  placeholder: string;
  options: readonly Option[];
  onChange: (value: string) => void;
  className?: string;
};

export function TransacaoFilterSelect({
  label,
  value,
  placeholder,
  options,
  onChange,
  className = "",
}: Readonly<TransacaoFilterSelectProps>) {
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>
      <select
        className="select select-bordered w-full"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      >
        <option value="">{placeholder}</option>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </fieldset>
  );
}
