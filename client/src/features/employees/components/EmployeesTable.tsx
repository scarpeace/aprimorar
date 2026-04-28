import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOEmployeeResponseDTO, EmployeeResponseDTO } from "@/kubb";
import { dutyLabels } from "../utils/dutyLabels";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";

type EmployeesTableProps = {
  employees?: PageDTOEmployeeResponseDTO;
  onPageChange: (page: number) => void;
  currentPage: number;
  isPending: boolean;
  error: unknown;
  onEdit: (employee: EmployeeResponseDTO) => void;
};

export function EmployeesTable({
  employees,
  onPageChange,
  currentPage,
  isPending,
  error,
  onEdit,
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

  return (
    <>
      <table className="table table-zebra shadow-2xl bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-300">
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
            {/*TODO: Trocar esse status por uma bolinha de status e mover as actions pra dentro da row*/}
            <th className="text-center font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {employees?.content.map((employee) => (
            <tr
              key={employee.id}
              onClick={() => navigate(`/employees/${employee.id}`)}
              className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
            >
              <td>{employee.name}</td>
              <td>{dutyLabels[employee.duty]}</td>

              <td>{formatCpf(employee.cpf)}</td>
              <td>{formatPhone(employee.contact)}</td>

              <td>{formatDateShortYear(employee.createdAt ?? "")}</td>
              <td className="text-center">
                {employee.archivedAt ? (
                  <div className="badge badge-soft badge-error">Arquivado</div>
                ) : (
                  <div className="badge badge-soft badge-info">Ativo</div>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Pagination
        paginationData={employees}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
