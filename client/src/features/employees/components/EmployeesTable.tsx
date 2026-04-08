import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOEmployeeResponseDTO } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";

type EmployeesTableProps = {
  employees?: PageDTOEmployeeResponseDTO;
  children?: ReactNode;
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

  if (isPending) {
    return <LoadingSpinner text="Carregando Colaboradores..." />;
  }

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Colaboradores" error={error} />;
  }

  return (
    <>
      <div className="overflow-x-auto rounded bg-base-100 mt-3 shadow-xl">
        <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
          <thead className="bg-base-300">
            <tr>
              <th className="text-left font-semibold text-base-content/80">
                Nome
              </th>
              <th className="text-left font-semibold text-base-content/80">
                CPF
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Contato
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Matricula
              </th>
              {/*TODO: Trocar esse status por uma bolinha de status e mover as actions pra dentro da row*/}
              <th className="text-left font-semibold text-base-content/80">
                Status
              </th>
              <th className="text-center font-semibold text-base-content/80">
                Ações
              </th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {employees?.content.map((employee) => (
              <tr
                key={employee.id}
                className="transition-colors hover:bg-base-200/70"
              >
                <td>{employee.name}</td>

                <td>{formatCpf(employee.cpf)}</td>
                <td>{formatPhone(employee.contact)}</td>


                <td>{formatDateShortYear(employee.createdAt ?? "")}</td>
                <td>{employee.archivedAt ? "Arquivado" : "Ativo"}</td>

                <td className="text-center">
                  <ButtonLink
                    className="btn btn-outline btn-info"
                    to={`/employees/${employee.id}`}
                  >
                    Detalhes
                  </ButtonLink>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Pagination
            paginationData={employees}
            currentPage={currentPage}
            onPageChange={onPageChange}
          />
    </>
  );
}
