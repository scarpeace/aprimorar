import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { dutyLabels } from "../lib/dutyLabels";
import {
  brl,
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

type EmployeesTableProps = {
  employees?: EmployeesWithFinanceResponseDTO;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
};

export function EmployeesTable({
  employees,
  onPageChange,
  currentPage,
  isPending,
  error,
}: Readonly<EmployeesTableProps>) {

  const navigate = useNavigate();

  if (isPending) {
    return <LoadingSpinner text="Carregando Colaboradores..." />;
  }

  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de Colaboradores"
        error={error}
      />
    );
  }

  if (!employees || !employees.content || employees.content.length === 0) {
    return (
      <EmptyCard
        title="Nenhum colaborador encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  const paginationData = {
    size: employees.size ?? employees.content.length,
    totalElements: employees.totalElements ?? employees.content.length,
    totalPages: employees.totalPages ?? 1,
    content: employees.content,
  };

  return (
    <>
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Nome
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Cargo
            </th>
            <th className="text-left font-semibold text-base-content/80">
              CPF
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Contato
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Cadastro
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Pago
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Pendente
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {employees.content.map((employee) => {
            const totalPaid = employee.totalPaid ?? 0;
            const totalPending = employee.totalPending ?? 0;

            return (
              <tr
                key={employee.id}
                className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
                onClick={() => employee.id && navigate(`/employees/${employee.id}`)}
              >
                <td className="font-bold">{employee.name}</td>
                <td>
                  {employee.duty
                    ? dutyLabels[employee.duty as keyof typeof dutyLabels]
                    : "-"}
                </td>

                <td>{formatCpf(employee.cpf ?? "")}</td>
                <td>{formatPhone(employee.contact ?? "")}</td>

                <td>{formatDateShortYear(employee.createdAt ?? "")}</td>
                <td className="text-right font-mono font-semibold text-success">
                  {brl.format(totalPaid)}
                </td>
                <td
                  className={`text-right font-mono font-semibold ${
                    totalPending > 0 ? "text-warning" : "text-base-content/45"
                  }`}
                >
                  {brl.format(totalPending)}
                </td>
                <td>
                  <span className={`badge ${(employee.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                    {(employee.active ?? true) ? "Ativo" : "Arquivado"}
                  </span>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
      </div>
      <Pagination
        paginationData={paginationData}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
