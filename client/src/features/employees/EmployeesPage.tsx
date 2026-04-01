import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import styles from "@/features/employees/EmployeesPage.module.css";
import { useDebounce } from "@/lib/shared/use-debounce";
import { UserCog } from "lucide-react";
import { useState } from "react";
import { useEmployees } from "./query/employeeQueries";
import { employeeRequestDTODutyEnum } from "@/kubb";

export function EmployeesPage() {
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const pageSize = 10;

  //TODO Retirar essa query daqui, verificar StudentsPage
  const {
    data: employees,
    isLoading: isEmployeesLoading,
    error: employeesError,
    refetch: refetchEmployees,
  } = useEmployees({
    page: currentPage,
    size: pageSize,
    search: debouncedSearchTerm,
  });


  if (isEmployeesLoading) {
    return <PageLoading message="Carregando colaboradores..." />;
  }

  //TODO ver se esse erro de página tá padronizado em todos os componentes + adicionar actions
  if (employeesError) {
    return (
      <div className={styles.page}>
        <PageHeader
          description="Gerencie professores e equipe."
          title="Colaboradores"
          Icon={UserCog}
        ></PageHeader>


      </div>
    );
  }

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie professores e equipe."
        title="Colaboradores"
        Icon={UserCog}
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar colaborador por nome, função ou email"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink
            className="sm:ml-auto"
            to="/employees/new"
            variant="success"
          >
            Novo colaborador
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th">Função</th>
                <th className="app-th hidden lg:table-cell">PIX</th>
                <th className="app-th hidden lg:table-cell">CPF</th>
                <th className="app-th hidden lg:table-cell">Contato</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {employees?.content?.map((employee) => (
                <tr
                  className="transition-colors hover:bg-base-200/70"
                  key={employee.id}
                >
                  <td>{employee.name}</td>
                  <td>{employeeRequestDTODutyEnum[employee.duty]}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">
                    {employee.pix}
                  </td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">
                    {employee.cpf}
                  </td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">
                    {employee.contact}
                  </td>
                  <td>
                    <ButtonLink
                      size="sm"
                      to={`/employees/${employee.id}`}
                      variant="outline"
                    >
                      Detalhes
                    </ButtonLink>
                  </td>
                  <td className="text-center">
                    <span
                      className={`badge ${employee.archivedAt ? "badge-warning" : "badge-success"}`}
                    >
                      {employee.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
     </div>
  );
}
