import { atendimentoRequestDTOTipoEnum } from "@/kubb";
import { TriangleAlert } from "lucide-react";
import type { UseFormRegisterReturn } from "react-hook-form";
import { tipoAtendimentoLabels } from "../lib/tipo-atendimento-labels";

type ContentSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  label: string;
};

export function ContentSelectDropdown({
  registration,
  error,
  label,
}: Readonly<ContentSelectDropdownProps>) {
  return (
    <fieldset className="fieldset">
      <legend className="fieldset-legend">{label}</legend>

      <select className="select select-bordered w-full" {...registration}>
        <option value="">Selecione o tipo</option>
        {Object.values(atendimentoRequestDTOTipoEnum).map((tipo) => (
          <option key={tipo} value={tipo}>
            {tipoAtendimentoLabels[tipo]}
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
