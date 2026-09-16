import { DateField } from "@/components/ui/DateField";
import { SearchInput } from "@/components/ui/SearchInput";
import { SelectField } from "@/components/ui/SelectField";
import type { BuscarAtendimentosIndividuaisQueryParamsTipoEnumKey } from "@/lib/api/generated/types/BuscarAtendimentosIndividuais";
import { atendimentoTipoOptions, statusCobrancaOptions } from "@/lib/constants/atendimento-constants";

export type AlunoAtendimentoTipo = BuscarAtendimentosIndividuaisQueryParamsTipoEnumKey | "";

type AlunoAtendimentosFiltersProps = {
  search: string;
  tipo: AlunoAtendimentoTipo;
  statusCobranca: string;
  dataInicio: string;
  dataFim: string;
  onSearchChange: (value: string) => void;
  onTipoChange: (value: AlunoAtendimentoTipo) => void;
  onStatusCobrancaChange: (value: string) => void;
  onDataInicioChange: (value: string) => void;
  onDataFimChange: (value: string) => void;
};

export function AlunoAtendimentosFilters({
  search,
  tipo,
  statusCobranca,
  dataInicio,
  dataFim,
  onSearchChange,
  onTipoChange,
  onStatusCobrancaChange,
  onDataInicioChange,
  onDataFimChange,
}: Readonly<AlunoAtendimentosFiltersProps>) {
  return (
    <div className="flex flex-wrap items-end gap-3">
      <SearchInput
        label="Buscar"
        value={search}
        onChange={onSearchChange}
        placeholder="Busque pelo atendimento"
        className="min-w-60 flex-1"
      />

      <SelectField
        label="Tipo"
        value={tipo}
        options={atendimentoTipoOptions}
        onChange={(value) => onTipoChange(value as AlunoAtendimentoTipo)}
        className="w-full sm:w-48"
      />

      <SelectField
        label="Cobrança"
        value={statusCobranca}
        options={statusCobrancaOptions}
        onChange={onStatusCobrancaChange}
        className="w-full sm:w-48"
      />

      <DateField label="Início" value={dataInicio} onChange={onDataInicioChange} className="w-full sm:w-44" />
      <DateField label="Fim" value={dataFim} onChange={onDataFimChange} className="w-full sm:w-44" />
    </div>
  );
}
