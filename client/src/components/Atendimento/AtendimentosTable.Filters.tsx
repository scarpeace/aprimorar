import { TransacaoFilterSelect } from "@/components/Financeiro/TransacaoFilterSelect";
import { Button } from "@/components/Ui/Button.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput.tsx";
import { type AtendimentoResponseStatusEnumKey, type AtendimentoResponseTipoEnumKey } from "@/kubb";
import { statusOptions, tipoOptions } from "@/utils/constants/atendimento-constants.ts";
import { Plus } from "lucide-react";

type AtendimentosFiltersProps = {
  openForm: () => void;
  filterStatus: AtendimentoResponseStatusEnumKey | "";
  filterTipo: AtendimentoResponseTipoEnumKey | "";
  onSearchChange: (value: string) => void;
  onStatusChange: (value: AtendimentoResponseStatusEnumKey | "") => void;
  onTipoChange: (value: AtendimentoResponseTipoEnumKey | "") => void;
};

export function AtendimentosTableFilters({
  openForm,
  filterStatus,
  filterTipo,
  onSearchChange,
  onStatusChange,
  onTipoChange,
}: AtendimentosFiltersProps) {
  return (
    <section className="flex flex-row items-center justify-between">
      <div className="flex gap-3 w-5xl">
        <TextSearchInput
          label="Pesquisar"
          className=""
          placeholder="Aluno, colaborador, ou tipo do atendimento"
          onChange={onSearchChange}
        />

        <TransacaoFilterSelect
          label="Status"
          value={filterStatus}
          placeholder="Todos os status"
          options={statusOptions}
          onChange={(value) => onStatusChange(value as AtendimentoResponseStatusEnumKey | "")}
        />

        <TransacaoFilterSelect
          label="Tipo"
          value={filterTipo}
          placeholder="Todos os tipos"
          options={tipoOptions}
          onChange={(value) => onTipoChange(value as AtendimentoResponseTipoEnumKey | "")}
        />
      </div>

        <Button variant="success" onClick={openForm}>
          <Plus className="mr-2 h-4 w-4" />
          Novo atendimento
        </Button>
    </section>
  );
}
