import { Search } from "lucide-react";

type TextSearchInputProps = {
  placeholder: string;
  label?: string;
  onChange?: (value: string) => void;
  className?: string;
};

export function TextSearchInput({ placeholder, label, onChange, className }: TextSearchInputProps) {
  return (
    <label className={`input ${className}`}>
     <Search size={12}/>
      <input type="search" placeholder={placeholder} onChange={(e) => onChange?.(e.target.value)} />
      {label && <span>{label}</span>}
    </label>
  );
}
