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
import { useGetColaboradores, type ColaboradorResponseDTO } from "@/kubb";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useState } from "react";
import { useDebounce } from "@/lib/hooks/use-debounce";

// type EmployeesTableProps = {};

export function EmployeesTable() {

  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hidePaid, setHidePaid] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<ColaboradorResponseDTO | null>(null);
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const colaboradoresQuery = useGetColaboradores({
    page: currentPage,
    busca: debouncedSearchTerm,
    arquivado: showArchived,
  });

  const onPageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleCloseForm = () => {
    setSelectedEmployee(null);
    setIsFormOpen(false);
  };

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  const handleHidePaidChange = (value: boolean) => {
    setHidePaid(value);
    setCurrentPage(0);
  };


  if (colaboradoresQuery.isPending) {
    return <LoadingSpinner text="Carregando Colaboradores..." />;
  }

  if (colaboradoresQuery.error || !colaboradoresQuery.data) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de Colaboradores"
        error={colaboradoresQuery.error}
      />
    );
  }

  if ((colaboradoresQuery.data.colaboradores?.totalElements ?? 0) === 0) {
    return (
      <EmptyCard
        title="Nenhum colaborador encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">

          <ListSearchInput
            className="grow"
            placeholder="Buscar colaborador por nome, email ou CPF"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <div className="flex w-full flex-row gap-3 xl:w-auto xl:justify-end">
            <ToggleSwitch
              label="Mostrar Arquivados"
              tip="Mostrar colaboradores arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />
            <ToggleSwitch
              label="Ocultar pagos"
              tip="Mostrar apenas colaboradores com pendencias no periodo"
              toggled={hidePaid}
              setToggle={handleHidePaidChange}
              className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
            />
          </div>
        </div>
      </section>


      {/*TABELA*/}
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
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {colaboradoresQuery.data.colaboradores?.content?.map((employee) => {

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
        size={colaboradoresQuery.data.colaboradores?.size ?? 10}
        totalElements={colaboradoresQuery.data.colaboradores?.totalElements ?? 0}
        totalPages={colaboradoresQuery.data.colaboradores?.totalPages ?? 1}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
