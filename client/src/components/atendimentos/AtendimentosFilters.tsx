"use client";

import { Plus } from "lucide-react";
import { Button } from "@/components/ui/Button";
import { SearchInput } from "@/components/ui/SearchInput";
import type {
  AtendimentoResponseStatusEnumKey,
  AtendimentoResponseTipoEnumKey,
} from "@/lib/api/generated/types/AtendimentoResponse";
import { atendimentoStatusOptions, atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";

type AtendimentosFiltersProps = {
  searchInput: string;
  status: AtendimentoResponseStatusEnumKey | "";
  tipo: AtendimentoResponseTipoEnumKey | "";
  onSearchInputChange: (value: string) => void;
  onStatusChange: (value: AtendimentoResponseStatusEnumKey | "") => void;
  onTipoChange: (value: AtendimentoResponseTipoEnumKey | "") => void;
  onCreate: () => void;
};

export function AtendimentosFilters({
  searchInput,
  status,
  tipo,
  onSearchInputChange,
  onStatusChange,
  onTipoChange,
  onCreate,
}: Readonly<AtendimentosFiltersProps>) {
  return (
    <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
      <div>
        <h2 className="text-2xl font-bold text-base-content">Atendimentos</h2>
        <p className="mt-2 text-sm text-base-content/65">Listagem mensal com busca, filtros e paginação.</p>
      </div>

      <div className="flex flex-col gap-3 lg:min-w-[42rem]">
        <div className="flex items-end gap-3">
          <SearchInput
            label="Buscar"
            value={searchInput}
            onChange={onSearchInputChange}
            placeholder="Digite aluno, colaborador ou tipo"
          />

          <Button
            type="button"
            size="sm"
            className="btn-square mb-1 shrink-0"
            aria-label="Novo atendimento"
            title="Novo atendimento"
            onClick={onCreate}
          >
            <Plus size={18} />
          </Button>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <label className="form-control min-w-0">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Status</span>
            <select
              className="select select-bordered w-full"
              value={status}
              onChange={(event) => onStatusChange(event.target.value as AtendimentoResponseStatusEnumKey | "")}
            >
              {atendimentoStatusOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </label>

          <label className="form-control min-w-0">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Tipo</span>
            <select
              className="select select-bordered w-full"
              value={tipo}
              onChange={(event) => onTipoChange(event.target.value as AtendimentoResponseTipoEnumKey | "")}
            >
              {atendimentoTipoOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </label>
        </div>
      </div>
    </div>
  );
}
