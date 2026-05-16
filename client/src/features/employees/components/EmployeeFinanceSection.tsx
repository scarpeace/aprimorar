import { ToggleSwitch } from "@/components/ui/toggle-switch";
import type { PageDTOEmployeeResponseDTO } from "@/kubb";
import { useGetEmployeesAppointmentsFinanceReport } from "@/kubb";
import { useMemo, useState } from "react";
import { EmployeeKPIs } from "./EmployeeKPIs";
import { EmployeesTable } from "./EmployeesTable";

type EmployeeFinanceSectionProps = {
  employees?: PageDTOEmployeeResponseDTO;
  employeesIsPending: boolean;
  employeesError: unknown;
  startDate?: Date | null;
  endDate?: Date | null;
  currentPage: number;
  onPageChange: (page: number) => void;
};

export function EmployeeFinanceSection({
  employees,
  employeesIsPending,
  employeesError,
  startDate,
  endDate,
  currentPage,
  onPageChange,
}: Readonly<EmployeeFinanceSectionProps>) {
  const [hidePaid, setHidePaid] = useState(false);

  const employeesFinanceQuery = useGetEmployeesAppointmentsFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

  const financeRows = useMemo(
    () => employeesFinanceQuery.data?.employees ?? [],
    [employeesFinanceQuery.data?.employees],
  );

  const paidByEmployeeId = useMemo(
    () =>
      new Map<string, number>(
        financeRows.flatMap((employee) =>
          employee.employeeId
            ? [[employee.employeeId, employee.totalPaid ?? 0] as const]
            : [],
        ),
      ),
    [financeRows],
  );

  const pendingByEmployeeId = useMemo(
    () =>
      new Map<string, number>(
        financeRows.flatMap((employee) =>
          employee.employeeId
            ? [[employee.employeeId, employee.totalPending ?? 0] as const]
            : [],
        ),
      ),
    [financeRows],
  );

  const employeeFinanceSummary = useMemo(() => {
    return financeRows.reduce(
      (summary, employee) => ({
        totalEvents: summary.totalEvents + (employee.totalEvents ?? 0),
        totalPaid: summary.totalPaid + (employee.totalPaid ?? 0),
        totalPending: summary.totalPending + (employee.totalPending ?? 0),
      }),
      { totalEvents: 0, totalPaid: 0, totalPending: 0 } as {
        totalEvents: number;
        totalPaid: number;
        totalPending: number;
      },
    );
  }, [financeRows]);

  const displayedEmployees = useMemo(() => {
    if (!employees || !hidePaid) {
      return employees;
    }

    return {
      ...employees,
      content: employees.content.filter(
        (employee) => (pendingByEmployeeId.get(employee.id) ?? 0) > 0,
      ),
    };
  }, [employees, hidePaid, pendingByEmployeeId]);

  const handleHidePaidChange = (value: boolean) => {
    setHidePaid(value);
    onPageChange(0);
  };

  return (
    <>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_260ms_ease-out_both]">
        <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
          <div>
            <h3 className="text-lg font-bold text-base-content">
              Resumo financeiro dos colaboradores
            </h3>
            <p className="text-sm text-base-content/60">
              Indicadores consolidados respeitando o periodo selecionado nos filtros.
            </p>
          </div>

          <ToggleSwitch
            label="Ocultar pagos"
            tip="Mostrar apenas colaboradores com pendencias no periodo"
            toggled={hidePaid}
            setToggle={handleHidePaidChange}
            className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
          />
        </div>

        <EmployeeKPIs
          totalEvents={employeeFinanceSummary.totalEvents}
          totalPaid={employeeFinanceSummary.totalPaid}
          totalUnpaid={employeeFinanceSummary.totalPending}
        />
      </section>

      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h3 className="text-lg font-bold text-base-content">
              Colaboradores cadastrados
            </h3>
            <p className="text-sm text-base-content/60">
              Clique na linha para abrir os detalhes do cadastro.
            </p>
          </div>
        </div>

        <EmployeesTable
          employees={displayedEmployees}
          paidByEmployeeId={paidByEmployeeId}
          pendingByEmployeeId={pendingByEmployeeId}
          onPageChange={onPageChange}
          currentPage={currentPage}
          isPending={employeesIsPending || employeesFinanceQuery.isPending}
          error={employeesError ?? employeesFinanceQuery.error}
        />
      </section>
    </>
  );
}
