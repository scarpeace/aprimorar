import { ButtonLink } from "@/components/ui/button";
import type { AlunoFormSchema } from "@/features/alunos/lib/studentFormSchema.ts";
import { useListarOpcoesResponsaveis } from "@/kubb";
import { TriangleAlert, UserPlus } from "lucide-react";
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
  const { data: responsavels, isPending } = useListarOpcoesResponsaveis();
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend w-full flex flex-row justify-between items-center">
        {label}
        <div className="tooltip flex" data-tip={"Novo Responsável"}>
          <ButtonLink to="/parents/new" variant="success" className="btn-xs flex">
            <UserPlus className="w-5 h-3" />
          </ButtonLink>
        </div>
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
