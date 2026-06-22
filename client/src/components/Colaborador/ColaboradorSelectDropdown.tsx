import { useGetColaboradoresList } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import type { UseFormRegisterReturn } from "react-hook-form";

type ColaboradorSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label: string;
  defaultValue?: { id: string; name: string };
};

export function ColaboradorSelectDropdown({
  registration,
  error,
  className,
  label,
  defaultValue,
}: ColaboradorSelectDropdownProps) {
  const { data: colaboradores, isPending } = useGetColaboradoresList();

  const hasDefaultValue = defaultValue?.id && defaultValue?.name;
  const isDefaultInOptions = colaboradores?.some((e) => e.id === defaultValue?.id);

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

        {colaboradores?.map((colaborador) => (
          <option key={colaborador.id} value={colaborador.id}>
            {colaborador.nome}
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
