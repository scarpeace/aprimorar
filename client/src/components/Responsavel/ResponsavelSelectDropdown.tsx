import type { AlunoFormSchema } from "@/utils/zod/aluno-form-schema.ts";
import { useListResponsaveis } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import {
  Controller,
  useFormContext,
  type Control
} from "react-hook-form";

type ResponsavelSelectDropdownProps = {
  className?: string;
  label?: string;
};

export function ResponsavelSelectDropdown({
  className,
  label,
}: ResponsavelSelectDropdownProps) {
  const { data: responsavels, isPending } = useListResponsaveis();
  const { register, formState: { errors } } = useFormContext();

  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend w-full flex flex-row justify-between items-center">
        {label}
        <span className="text-xs font-normal text-base-content/60">
          Cadastre novos responsáveis na página de responsáveis.
        </span>
      </legend>

      <select
        className="select select-bordered w-full"
        disabled={isPending}
        {...register("responsavelId")}
      >
        <option value="">Selecione um responsável</option>
        {responsavels?.map((responsavel) => (
          <option key={responsavel.id} value={responsavel.id}>
            {responsavel.nome}
          </option>
        ))}
      </select>

      {errors["responsavelId"] && (
        <span className="text-error text-sm">
          {String(errors["responsavelId"]?.message)}
        </span>
      )}


    </fieldset>
  );
}
