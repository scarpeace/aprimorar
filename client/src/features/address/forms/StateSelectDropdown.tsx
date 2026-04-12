import { brazilianStates } from "@/lib/utils/brazilianStates";
import { TriangleAlert } from "lucide-react";
import { type UseFormRegisterReturn } from "react-hook-form";

type StateSelectDropdownProps = {
  registration?: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
};

export function StateSelectDropdown({
  registration,
  error,
  className,
  label,
}: StateSelectDropdownProps) {
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select className="select select-bordered w-full" {...registration}>
        {Object.values(brazilianStates).map((content) => (
          <option key={content} value={content}>
            {content}
          </option>
        ))}
      </select>

      {error && (
        <p className="label text-error">
          <TriangleAlert className="w-3 h-3" />
          {error}
        </p>
      )}
    </fieldset>
  );
}
