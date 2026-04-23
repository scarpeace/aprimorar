import { eventRequestDTOStatusEnum } from "@/kubb";
import { EventStatusLabels } from "@/lib/shared/eventStatusLabels";
import { TriangleAlert } from "lucide-react";
import { type UseFormRegisterReturn } from "react-hook-form";

type StatusSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
};

export function StatusSelectDropdown({
  registration,
  error,
  className,
  label,
}: StatusSelectDropdownProps) {
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select className="select select-bordered w-full" {...registration}>
        {Object.values(eventRequestDTOStatusEnum).map((status) => (
          <option key={status} value={status}>
            {EventStatusLabels[status] || status}
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
