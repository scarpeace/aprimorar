import { useGetAtendimentos } from "@/kubb";
import { AppointmentsTable } from "./AppointmentsTable";
import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { RotateCcw } from "lucide-react";
import { useAppointmentsFilters } from "../hooks/use-appointments-filters";

type AppointmentsListSectionProps = {
  page: number;
  search: string;
  hideCharged: boolean;
  hidePaid: boolean;
  onPageChange: (page: number) => void;
};

export function AppointmentsListSection({
  onPageChange,
}: AppointmentsListSectionProps) {
  const {
    search,
    hideCharged,
    hidePaid,
    page,
    hasFilters,
    handleClearFilters,
    handleSearchChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
  } = useAppointmentsFilters();

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    sort: ["startDate,desc", "id,asc"],
    search: search || undefined,
    hideCharged: hideCharged || undefined,
    hidePaid: hidePaid || undefined,
  });



  return (
    <>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
        {/*<div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
        </div>*/}

        <div className="mt-3 flex items-center flex-row justify-between">
          <ListSearchInput
            placeholder="Buscar por aluno, colaborador, conteúdo ou descrição"
            ariaLabel="Buscar atendimento"
            value={search}
            onChange={handleSearchChange}
          />

            <div className="flex flex-row items-center gap-3">
              <ToggleSwitch
                label="Cobrança Pendente"
                toggled={hideCharged}
                setToggle={handleHideChargedToggle}
                tip="Mostrar apenas eventos onde o aluno ainda não foi cobrado"
                className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
              <ToggleSwitch
                label="Pagamento Pendente"
                toggled={hidePaid}
                setToggle={handleHidePaidToggle}
                tip="Mostrar apenas eventos onde o colaborador ainda não foi pago"
                className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
            </div>

            {hasFilters ? (
              <Button variant="ghost" onClick={handleClearFilters} className="gap-2 self-start xl:self-auto">
                <RotateCcw className="h-4 w-4" />
                Limpar filtros
              </Button>
          ) : null}
          </div>

      </section>
    <AppointmentsTable
      appointments={eventsQuery.data}
      currentPage={page}
      onPageChange={onPageChange}
      isLoading={eventsQuery.isPending}
      error={eventsQuery.error}
      />
    </>
  );
}
