import { useGetStudentsOptions } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import {type UseFormRegisterReturn} from "react-hook-form";

type StudentSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
};

export function StudentSelectDropdown({
  registration,
  error,
  className,
  label,
}: StudentSelectDropdownProps) {
  const { data: students, isPending } = useGetStudentsOptions();

  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend">{label}</legend>

      <select
        className="select select-bordered w-full"
        disabled={isPending}
        {...registration}
      >
        <option value="">Selecione um aluno</option>
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
