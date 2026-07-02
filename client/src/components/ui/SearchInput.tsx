type SearchInputProps = {
  label?: string;
  value: string;
  placeholder: string;
  onChange: (value: string) => void;
};

export function SearchInput({ label = "Buscar", value, placeholder, onChange }: Readonly<SearchInputProps>) {
  return (
    <label className="form-control w-full md:w-80">
      <span className="label-text mb-2 text-sm font-medium text-base-content/70">{label}</span>
      <input
        className="input input-bordered w-full"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
      />
    </label>
  );
}
