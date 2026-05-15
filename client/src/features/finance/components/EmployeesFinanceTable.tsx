import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { EmployeeFinanceSummaryDTO } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

type EmployeesFinanceTableProps = {
  employees?: EmployeeFinanceSummaryDTO[];
  isPending: boolean;
  error: unknown;
};

export function EmployeesFinanceTable({
  employees,
  isPending,
  error,
}: Readonly<EmployeesFinanceTableProps>) {
  const navigate = useNavigate();

  if (error) {
    return (
      <ErrorCard
        title="Nao foi possivel carregar o financeiro dos colaboradores"
        error={error}
      />
    );
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando financeiro dos colaboradores..." />;
  }

  if (!employees || employees.length === 0) {
    return (
      <EmptyCard
        title="Nenhum colaborador encontrado"
        description="Nao ha colaboradores com eventos financeiros no periodo selecionado."
      />
    );
  }

  return (
    <div className="max-h-120 overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Colaborador
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Total pago
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Total pendente
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {employees.map((employee) => {
            const employeeId = employee.employeeId;
            const totalPaid = employee.totalPaid ?? 0;
            const totalPending = employee.totalPending ?? 0;

            return (
              <tr
                key={employeeId ?? employee.employeeName}
                className="transition-colors hover:cursor-pointer hover:bg-base-200/70"
                onClick={() => employeeId && navigate(`/employees/${employeeId}`)}
              >
                <td className="font-bold text-base-content">
                  {employee.employeeName ?? "Colaborador sem nome"}
                </td>
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
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
