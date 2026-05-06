import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOEmployeeResponseDTO } from "@/kubb";
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
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {employees?.content.map((employee) => (
            <tr
              key={employee.id}
              className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
              onClick={() => navigate(`/employees/${employee.id}`)}
            >
              <td>{employee.name}</td>
              <td>{dutyLabels[employee.duty]}</td>

              <td>{formatCpf(employee.cpf)}</td>
              <td>{formatPhone(employee.contact)}</td>

              <td>{formatDateShortYear(employee.createdAt)}</td>
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
