"use client";

import type { FormEvent } from "react";
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
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  onCreate: () => void;
};

export function AtendimentosFilters({
  searchInput,
  status,
  tipo,
  onSearchInputChange,
  onStatusChange,
  onTipoChange,
  onSubmit,
  onCreate,
}: Readonly<AtendimentosFiltersProps>) {
  return (
    <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
      <div>
        <h2 className="text-2xl font-bold text-base-content">Atendimentos</h2>
        <p className="mt-2 text-sm text-base-content/65">Listagem mensal com busca, filtros e paginação.</p>
      </div>

      <form className="flex flex-col gap-3" onSubmit={onSubmit}>
        <div className="flex flex-col gap-3 md:flex-row md:items-end">
          <SearchInput label="Buscar" value={searchInput} onChange={onSearchInputChange} placeholder="Digite aluno, colaborador ou tipo" />

          <label className="form-control w-full md:w-56">
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

          <label className="form-control w-full md:w-64">
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

          <Button type="submit" variant="outline">
            Buscar
          </Button>

          <Button type="button" onClick={onCreate}>
            Novo atendimento
          </Button>
        </div>
      </form>
    </div>
  );
}
