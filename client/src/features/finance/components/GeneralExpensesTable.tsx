import { Pagination } from "@/components/ui/pagination";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorCard } from "@/components/ui/error-card";
import { EmptyCard } from "@/components/ui/empty-card";
import type { PagedModelGeneralExpenseResponseDTO } from "@/kubb/types/PagedModelGeneralExpenseResponseDTO";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Pencil, Trash2 } from "lucide-react";
import type { GeneralExpenseResponseDTO } from "@/kubb";

interface GeneralExpensesTableProps {
  expenses?: PagedModelGeneralExpenseResponseDTO;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: Error | null;
  onEdit: (expense: GeneralExpenseResponseDTO) => void;
  onDelete: (id: string) => void;
}

const CATEGORY_LABELS: Record<string, string> = {
  CONTAS: "Contas",
  ADMINISTRATIVO: "Administrativo",
  DESPENSA: "Despensa",
  MANUTENCAO: "Manutenção",
  SERVICOS: "Serviços",
  MATERIAIS: "Materiais",
};

export function GeneralExpensesTable({
  expenses,
  onPageChange,
  currentPage,
  isPending,
  error,
  onEdit,
  onDelete,
}: GeneralExpensesTableProps) {
  if (isPending) return <LoadingSpinner text={"Carregando despesas..."} />;
  if (error) return <ErrorCard title="Erro ao carregar despesas" error={error.message} />;
  if (!expenses?.content || expenses.content.length === 0) {
    return <EmptyCard title="Nenhuma despesa encontrada" description="Tente ajustar os filtros ou cadastrar uma nova despesa." />;
  }

  return (
    <div className="overflow-x-auto bg-base-100 rounded-lg shadow">
      <table className="table w-full">
        <thead>
          <tr>
            <th>Data</th>
            <th>Descrição</th>
            <th>Categoria</th>
            <th>Valor</th>
            <th className="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          {expenses.content.map((expense) => (
            <tr key={expense.id} className="hover">
              <td>{expense.date ? format(new Date(expense.date), "dd/MM/yyyy", { locale: ptBR }) : "-"}</td>
              <td className="font-medium">{expense.description}</td>
              <td>
                <span className="badge badge-ghost">
                  {expense.category ? CATEGORY_LABELS[expense.category] || expense.category : "-"}
                </span>
              </td>
              <td className="text-error font-semibold">
                {expense.amount?.toLocaleString("pt-BR", {
                  style: "currency",
                  currency: "BRL",
                })}
              </td>
              <td className="text-right flex justify-end gap-2">
                <button
                  className="btn btn-ghost btn-xs text-info"
                  onClick={() => onEdit(expense)}
                  title="Editar"
                >
                  <Pencil size={16} />
                </button>
                <button
                  className="btn btn-ghost btn-xs text-error"
                  onClick={() => expense.id && onDelete(expense.id)}
                  title="Excluir"
                >
                  <Trash2 size={16} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {expenses.page && expenses.page.totalPages && expenses.page.totalPages > 1 && (
        <div className="p-4 border-t">
          <Pagination
            currentPage={currentPage}
            onPageChange={onPageChange}
          />
        </div>
      )}
    </div>
  );
}
