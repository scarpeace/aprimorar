import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { dutyLabels } from "../lib/dutyLabels";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { useGetColaboradores } from "@/kubb";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useState } from "react";
import { useDebounce } from "@/lib/hooks/use-debounce";

// type ColaboradoresTableProps = {};

export function ColaboradoresTable() {

  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hidePaid, setHidePaid] = useState(false);
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const colaboradoresQuery = useGetColaboradores({
    page: currentPage,
    busca: debouncedSearchTerm,
    arquivado: showArchived,
  });

  const onPageChange = (page: number) => {
    setCurrentPage(page);
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
      <section className="flex my-3 gap-12 animate-[fade-up_220ms_ease-out_both]">
          <ListSearchInput
            className="grow"
            placeholder="Buscar colaborador por nome, email ou CPF"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
        />
            <ToggleSwitch
              label="Mostrar Arquivados"
              tip="Mostrar colaboradores arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />
      </section>


      {/*TABELA*/}
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra w-full table-fixed bg-base-100 animate-[fade-up_280ms_ease-out_both]">
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
            <th className="text-center font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody>
          {colaboradoresQuery.data.colaboradores?.content?.map((colaborador) => {

            return (
              <tr
                key={colaborador.id}
                className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
                onClick={() => colaborador.id && navigate(`/employees/${colaborador.id}`)}
              >
                <td className="max-w-0 truncate font-bold">{colaborador.name}</td>
                <td>
                  <span className="block truncate">
                    {colaborador.duty
                    ? dutyLabels[colaborador.duty as keyof typeof dutyLabels]
                    : "-"}
                  </span>
                </td>

                <td className="whitespace-nowrap">{formatCpf(colaborador.cpf ?? "")}</td>
                <td className="whitespace-nowrap">{formatPhone(colaborador.contact ?? "")}</td>

                <td className="whitespace-nowrap">{formatDateShortYear(colaborador.createdAt ?? "")}</td>

                <td className="text-center">
                  <span className={`badge ${(colaborador.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                    {(colaborador.active ?? true) ? "Ativo" : "Arquivado"}
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
