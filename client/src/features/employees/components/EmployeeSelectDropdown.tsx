import { useGetEmployeeOptions } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import type { UseFormRegisterReturn } from "react-hook-form";

type EmployeeSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
  defaultValue?: { id: string; name: string };
};

export function EmployeeSelectDropdown({
  registration,
  error,
  className,
  label,
  defaultValue,
}: EmployeeSelectDropdownProps) {
  const { data: employees, isPending } = useGetEmployeeOptions();

  const hasDefaultValue = defaultValue?.id && defaultValue?.name;
  const isDefaultInOptions = employees?.some((e) => e.id === defaultValue?.id);

  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select
        className="select select-bordered w-full"
        disabled={isPending && !hasDefaultValue}
        {...registration}
      >
        {!hasDefaultValue && (
          <option value="">Selecione um colaborador</option>
        )}
        
        {hasDefaultValue && !isDefaultInOptions && (
          <option value={defaultValue.id}>{defaultValue.name}</option>
        )}

        {employees?.map((employee) => (
          <option key={employee.id} value={employee.id}>
            {employee.name}
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
