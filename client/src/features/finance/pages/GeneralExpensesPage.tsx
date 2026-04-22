import { useState } from "react";
import { PageLayout } from "@/components/layout/PageLayout";
import { Banknote, Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { GeneralExpensesTable } from "../components/GeneralExpensesTable";
import { GeneralExpenseForm } from "../components/GeneralExpenseForm";
import { useGetGeneralExpenses } from "@/kubb/hooks/general-expenses/useGetGeneralExpenses";
import type { GeneralExpenseResponseDTO } from "@/kubb";
import { useExpenseMutations } from "../hooks/use-expense-mutations";

export function GeneralExpensesPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedExpense, setSelectedExpense] = useState<GeneralExpenseResponseDTO | null>(null);
  const [currentPage, setCurrentPage] = useState(0);

  const { data, isPending, error } = useGetGeneralExpenses({
    pageable: { page: currentPage, size: 20 },
  });

  const { deleteExpense } = useExpenseMutations();

  const handleOpenForm = (expense?: GeneralExpenseResponseDTO) => {
    setSelectedExpense(expense || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedExpense(null);
    setIsFormOpen(false);
  };

  const handleDelete = (id: string) => {
    if (confirm("Tem certeza que deseja excluir esta despesa?")) {
      deleteExpense.mutate({ id });
    }
  };

  return (
    <PageLayout
      title="Despesas Gerais"
      description="Gerencie os gastos fixos e administrativos."
      Icon={Banknote}
      backLink="/finance"
    >
      <div className="flex flex-col gap-4">
        <div className="flex justify-end">
          <Button onClick={() => handleOpenForm()} variant="success">
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
          onEdit={(expense) => handleOpenForm(expense)}
          onDelete={handleDelete}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">
                {selectedExpense ? "Editar Despesa" : "Cadastrar Nova Despesa"}
              </h3>
              <GeneralExpenseForm
                initialData={selectedExpense}
                onSuccess={handleCloseForm}
                onCancel={handleCloseForm}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
