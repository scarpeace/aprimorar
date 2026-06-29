import { TransacaoFilterSelect } from "@/components/Financeiro/TransacaoFilterSelect";
import { Button } from "@/components/Ui/Button.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput.tsx";
import {
  type AtendimentoResponseStatusEnumKey,
  type AtendimentoResponseTipoEnumKey,
} from "@/kubb";
import {
  statusOptions,
  tipoOptions,
} from "@/utils/constants/atendimento-constants.ts";
import { Plus } from "lucide-react";

type AtendimentosTableHeaderProps = {
  openForm: () => void;
  filterStatus: AtendimentoResponseStatusEnumKey | "";
  filterTipo: AtendimentoResponseTipoEnumKey | "";
  onSearchChange: (value: string) => void;
  onStatusChange: (value: AtendimentoResponseStatusEnumKey | "") => void;
  onTipoChange: (value: AtendimentoResponseTipoEnumKey | "") => void;
  title?: string;
  description?: string;
  children?: React.ReactNode;
};

export function AtendimentosTableHeader({
  openForm,
  filterStatus,
  filterTipo,
  onSearchChange,
  onStatusChange,
  onTipoChange,
  title,
  description,
  children,
}: AtendimentosTableHeaderProps) {
  return (
    <section className="flex flex-row items-center gap-3 justify-between">
      <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
        <div>
          <h3 className="text-2xl font-bold text-base-content">{ title ? title : "Atendimentos" }</h3>
          {description && <p className="text-sm text-base-content/60">{description}</p>}
        </div>

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
            onChange={(value) =>
              onStatusChange(value as AtendimentoResponseStatusEnumKey | "")
            }
          />

          <TransacaoFilterSelect
            label="Tipo"
            value={filterTipo}
            placeholder="Todos os tipos"
            options={tipoOptions}
            onChange={(value) =>
              onTipoChange(value as AtendimentoResponseTipoEnumKey | "")
            }
          />
        </div>
      </div>

      <Button className="mt-6" variant="success" onClick={openForm} tooltip="Novo Atendimento">
        <Plus size={21} />
      </Button>
      {children}
    </section>
  );
}
