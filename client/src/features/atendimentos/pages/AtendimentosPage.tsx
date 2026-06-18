import { useState } from "react";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { BellElectric, Plus } from "lucide-react";
import { AtendimentosTable } from "../components/AtendimentosTable";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { usePageDateFilter } from "@/lib/shared/use-page-date-filter";

export function AtendimentosPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const { startDate, endDate, ...dateFilter } = usePageDateFilter();


  const headerProps = {
    description: "Gerencie aulas e atendimentos.",
    title: "Atendimentos",
    Icon: BellElectric,
    iconBg: "accent",
  } as const;

  const handleOpenForm = () => {
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-3">
          <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
            <div className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <h3 className="text-2xl font-bold text-base-content">Atendimentos cadastrados</h3>
                <p className="text-sm text-base-content/60">
                  Clique na linha para abrir os detalhes do cadastro.
                </p>
              </div>

              <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
                <Plus className="mr-2 h-4 w-4" />
                Novo Atendimento
              </Button>
            </div>

            <AtendimentosTable startDate={startDate} endDate={endDate}/>
          </section>
      </div>

      <PageDateFilterWidget startDate={startDate} endDate={endDate} {...dateFilter} />
    </PageLayout>
  );
}
