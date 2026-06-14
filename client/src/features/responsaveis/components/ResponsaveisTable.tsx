import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { useGetResponsaveis } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";
import { BrushCleaning } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function ResponsaveisTable() {
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const responsaveisQuery = useGetResponsaveis({
    page: currentPage,
    nome: debouncedSearchTerm || undefined,
    sort: ["nome,asc"],
  });

  const responsaveis = responsaveisQuery.data?.content ?? [];
  const page = responsaveisQuery.data?.page;
  const totalElements = page?.totalElements ?? 0;
  const totalPages = page?.totalPages ?? 0;
  const pageSize = page?.size ?? responsaveis.length;
  const hasResponsaveis = responsaveis.length > 0;

  const onPageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleCleanFilter = () => {
    setSearchTerm("");
    setCurrentPage(0);
  };

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <ListSearchInput
            className="grow"
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />
        </div>
      </section>

      {responsaveisQuery.isError && (
        <ErrorCard
          title="Não foi possível carregar a listagem de responsáveis"
          error={responsaveisQuery.error}
        />
      )}

      {responsaveisQuery.isLoading && (
        <LoadingSpinner text="Carregando responsáveis..." />
      )}

      {!responsaveisQuery.isLoading && !responsaveisQuery.isError && !hasResponsaveis && (
        <EmptyCard
          title="Nenhum responsável encontrado"
          description="Ajuste a busca para localizar os cadastros desejados."
          action={
            <Button variant="outline" onClick={handleCleanFilter}>
              Limpar filtros
              <BrushCleaning size={18} />
            </Button>
          }
        />
      )}

      {hasResponsaveis && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">Nome</th>
                <th className="text-left font-semibold text-base-content/80">Contato</th>
                <th className="text-left font-semibold text-base-content/80">E-mail</th>
                <th className="text-left font-semibold text-base-content/80">CPF</th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {responsaveis.map((responsavel) => (
                <tr
                  key={responsavel.id}
                  className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
                  onClick={() => navigate(`/responsaveis/${responsavel.id}`)}
                >
                  <td className="font-bold">{responsavel.nome}</td>
                  <td>{formatPhone(responsavel.telefone)}</td>
                  <td>{responsavel.email}</td>
                  <td>{formatCpf(responsavel.cpf)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Pagination
        totalElements={totalElements}
        totalPages={totalPages}
        currentPage={currentPage}
        onPageChange={onPageChange}
        size={pageSize}
      />
    </main>
  );
}
