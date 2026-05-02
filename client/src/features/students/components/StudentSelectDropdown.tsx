import { useGetStudentsOptions } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import {type UseFormRegisterReturn} from "react-hook-form";

type StudentSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
  defaultValue?: { id: string; name: string };
};

export function StudentSelectDropdown({
  registration,
  error,
  className,
  label,
  defaultValue,
}: StudentSelectDropdownProps) {
  const { data: students, isPending } = useGetStudentsOptions();

  const hasDefaultValue = defaultValue?.id && defaultValue?.name;
  const isDefaultInOptions = students?.some((s) => s.id === defaultValue?.id);

  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select
        className="select select-bordered w-full"
        disabled={isPending && !hasDefaultValue}
        {...registration}
      >
        {!hasDefaultValue && (
          <option value="">Selecione um aluno</option>
        )}

        {hasDefaultValue && !isDefaultInOptions && (
          <option value={defaultValue.id}>{defaultValue.name}</option>
        )}

        {students?.map((student) => (
          <option key={student.id} value={student.id}>
            {student.name}
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
