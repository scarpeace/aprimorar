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
    <section className="my-3 flex flex-col gap-3 animate-[fade-up_220ms_ease-out_both] lg:flex-row lg:items-center lg:justify-between">
      <div>
        <h3 className="text-2xl font-bold text-base-content">Atendimentos registrados</h3>
        <p className="text-sm text-base-content/60">
          Navegue por mês e selecione um atendimento para visualizar os detalhes.
        </p>
      </div>

      <div className="flex flex-wrap items-end gap-3">
        <TextSearchInput
          label="Pesquisar"
          className="w-120"
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

        <Button variant="success" onClick={openForm}>
          <Plus className="mr-2 h-4 w-4" />
          Novo atendimento
        </Button>
      </div>
    </section>
  );
}
