import { eventRequestDTOContentEnum } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import { type UseFormRegisterReturn } from "react-hook-form";

type ContentSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
};

export function ContentSelectDropdown({
  registration,
  error,
  className,
  label,
}: ContentSelectDropdownProps) {
  return (

    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select
        className="select select-bordered w-full"
        {...registration}
      >
       {Object.values(eventRequestDTOContentEnum).map((content) => (
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
