import type { AlunoFormSchema } from "@/features/alunos/lib/alunoFormSchema";
import { useListResponsaveis } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import {
  Controller,
  type Control
} from "react-hook-form";

type ResponsavelSelectDropdownProps = {
  className?: string;
  label?: string;
  control: Control<AlunoFormSchema>;
  error?: string;
};

export function ResponsavelSelectDropdown({
  control,
  error,
  className,
  label,
}: ResponsavelSelectDropdownProps) {
  const { data: responsavels, isPending } = useListResponsaveis();
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend w-full flex flex-row justify-between items-center">
        {label}
        <span className="text-xs font-normal text-base-content/60">
          Cadastre novos responsáveis na página de responsáveis.
        </span>
      </legend>

      <Controller
        control={control}
        name="parentId"
        render={({ field }) => (
          <select
            className="select select-bordered w-full"
            disabled={isPending}
            {...field}
            value={field.value ?? ""}
          >
            <option value="">Selecione um responsável</option>
            {responsavels?.map((responsavel) => (
              <option key={responsavel.id} value={responsavel.id}>
                {responsavel.name}
              </option>
            ))}
          </select>
        )}
      />

      {error && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" />{error}</p>
      )}
    </fieldset>
  );
}
