import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetResponsaveis } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatPhone } from "@/lib/utils/formatter";
import { BrushCleaning } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function ResponsaveisTable() {
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

  const handleCleanFilter = () => {
    setSearchTerm("");
    setShowArchived(false);
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
        action={<Button variant="outline" onClick={handleCleanFilter}>Limpar filtros<BrushCleaning size={18} /></Button>}
      />
    );
  }

  return (
    <main className="">
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <ListSearchInput
            className="grow"
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />

            <ToggleSwitch
              label="Arquivados"
              tip="Mostrar responsáveis arquivados"
              toggled={showArchived}
              setToggle={handleShowArchivedChange}
              className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
            />

        </div>
      </section>

      {/*TABELA*/}
      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-200/80">
          <tr>
            <th className="text-left font-semibold text-base-content/80">Nome</th>
            <th className="text-left font-semibold text-base-content/80">Contato</th>
            {/*<th className="text-left font-semibold text-base-content/80">Email</th>*/}
            {/*<th className="text-left font-semibold text-base-content/80">CPF</th>*/}
            <th className="text-left font-semibold text-base-content/80">Status</th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {responsaveisQuery.data?.content.map((responsavel) => (
            <tr
              key={responsavel.parentId}
              className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
              onClick={() => navigate(`/parents/${responsavel.parentId}`)}
            >
              <td className="font-bold">{responsavel.name}</td>

              <td>{formatPhone(responsavel.contact)}</td>
              {/*<td>{responsavel.email}</td>*/}
              {/*<td>{formatCpf(responsavel.cpf)}</td>*/}
              <td>
                <span className={`badge ${(responsavel.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                  {(responsavel.active ?? true) ? "Ativo" : "Arquivado"}
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
        size={responsaveisQuery.data.size}
      />
    </main>
  );
}
