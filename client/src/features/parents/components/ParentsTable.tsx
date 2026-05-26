import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetResponsaveis, type PageDTOResponsavelResponseDTO, type ResponsavelResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function ParentsTable() {
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const responsaveisQuery = useGetResponsaveis({
    page: currentPage, search: debouncedSearchTerm, archived: showArchived
  });

  const onPageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  if (responsaveisQuery.isError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Responsáveis" error={responsaveisQuery.error}/>;
  }

  if (responsaveisQuery.isLoading) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  if (!responsaveisQuery.data || !responsaveisQuery.data.content || responsaveisQuery.data.totalElements === 0) {
    return (
      <EmptyCard
        title="Nenhum responsável encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
      />
    );
  }

  return (
    <>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex justify-between lg:flex-row lg:items-center lg:justify-between">

          <ListSearchInput
            className="grow"
            placeholder="Buscar aluno por nome, email ou CPF"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />

          <div className="flex flex-row items-start justify-between gap-3 xl:w-auto xl:justify-end">
            <ToggleSwitch
              label="Arquivados"
              tip="Mostrar alunos arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />

          </div>
        </div>
      </section>

      {/*TABELA*/}
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Nome
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Contato
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Email
            </th>
            <th className="text-left font-semibold text-base-content/80">
              CPF
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {responsaveisQuery.data?.content.map((parent) => (
            <tr
              key={parent.parentId}
              className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
              onClick={() => navigate(`/parents/${parent.parentId}`)}
            >
              <td className="font-bold">{parent.name}</td>

              <td>{formatPhone(parent.contact)}</td>
              <td>{parent.email}</td>
              <td>{formatCpf(parent.cpf)}</td>
              <td>
                <span className={`badge ${(parent.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                  {(parent.active ?? true) ? "Ativo" : "Arquivado"}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>

      <Pagination
        totalElements={responsaveisQuery.data?.totalElements ?? 0}
        totalPages={responsaveisQuery.data?.totalPages ?? 0}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
