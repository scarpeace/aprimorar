import { useState } from "react";
import { PageLayout } from "@/components/layout/PageLayout";
import { Banknote, Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { GeneralExpensesTable } from "../components/GeneralExpensesTable";
import { GeneralExpenseForm } from "../components/GeneralExpenseForm";
import { useGetGeneralExpenses } from "@/kubb";

export function GeneralExpensesPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);

  const { data, isPending, error } = useGetGeneralExpenses({ page: currentPage, size: 20 });

  return (
    <PageLayout
      title="Despesas Gerais"
      description="Gerencie os gastos fixos e administrativos."
      Icon={Banknote}
      backLink="/finance"
    >
      <div className="flex flex-col gap-4">
        <div className="flex justify-end">
          <Button onClick={() => setIsFormOpen(true)} variant="success">
            <Plus className="h-4 w-4 mr-2" />
            Nova Despesa
          </Button>
        </div>

        <GeneralExpensesTable
          expenses={data}
          isPending={isPending}
          error={error}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          onEdit={(expense) => console.log("Edit não implementado", expense)}
          onDelete={(id) => console.log("Delete não implementado", id)}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">Cadastrar Nova Despesa</h3>
              <GeneralExpenseForm onSuccess={() => setIsFormOpen(false)} onCancel={() => setIsFormOpen(false)} />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
