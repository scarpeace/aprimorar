import { Button } from "@/components/Ui/Button.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput";
import { ToggleSwitch } from "@/components/Ui/ToggleSwitch";
import { Plus } from "lucide-react";

type ColaboradoresTableFiltersProps = {
  openForm: () => void;
  showArchived: boolean;
  onSearchChange: (value: string) => void;
  onShowArchivedChange: (value: boolean) => void;
};

export function ColaboradoresTableFilters({
  openForm,
  showArchived,
  onSearchChange,
  onShowArchivedChange,
}: ColaboradoresTableFiltersProps) {
  return (
    <section className="flex flex-row items-center justify-between">
      <div className="flex gap-3 w-5xl">
        <TextSearchInput
          className=""
          label="Pesquisar"
          placeholder="Buscar colaborador por nome, email ou CPF"
          onChange={onSearchChange}
        />

        <ToggleSwitch
          label="Mostrar Arquivados"
          tip="Exibe colaboradores arquivados junto com os ativos"
          checked={showArchived}
          setToggle={onShowArchivedChange}
          variant="info"
        />
      </div>

      <Button onClick={openForm} variant="success">
        <Plus className="mr-2 h-4 w-4" />
        Novo Colaborador
      </Button>
    </section>
  );
}
