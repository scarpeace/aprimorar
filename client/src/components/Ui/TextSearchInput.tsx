import { Search } from "lucide-react";

type TextSearchInputProps = {
  placeholder: string;
  label?: string;
  onChange?: (value: string) => void;
  className?: string;
};

export function TextSearchInput({ placeholder, label, onChange, className }: TextSearchInputProps) {
  return (
  <fieldset className={`fieldset ${className}`}>
    <legend className="fieldset-legend">{label}</legend>
      <input type="search" className="input" placeholder={placeholder} onChange={(e) => onChange?.(e.target.value)} />
      </fieldset>
  );
}
