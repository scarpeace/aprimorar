import { useListAlunos } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import {type UseFormRegisterReturn} from "react-hook-form";

type AlunoSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
  defaultValue?: { id: string; name: string };
};

export function AlunoSelectDropdown({
  registration,
  error,
  className,
  label,
  defaultValue,
}: AlunoSelectDropdownProps) {
  const { data: alunos, isPending } = useListAlunos();

  const hasDefaultValue = defaultValue?.id && defaultValue?.name;
  const isDefaultInOptions = alunos?.some((s) => s.id === defaultValue?.id);

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

        {alunos?.map((aluno) => (
          <option key={aluno.id} value={aluno.id}>
            {aluno.name}
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
