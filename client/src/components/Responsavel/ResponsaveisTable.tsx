import { Button } from "@/components/Ui/Button.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { useGetResponsaveis } from "@/kubb";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { formatCpf, formatPhone } from "@/utils/formatter.ts";
import { BrushCleaning, Plus, UserPlus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function ResponsaveisTable({ openForm }: { openForm: () => void }) {
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
          <div className="flex justify-between items-center">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Responsáveis</h3>
              <p className="text-sm text-base-content/60">Selecione um responsável para ver os detalhes.</p>
            </div>
          </div>
          {/*<ListSearchInput*/}
          {/*  className="grow"*/}
          {/*  placeholder="Nome, email ou CPF"*/}
          {/*  ariaLabel="Buscar responsável"*/}
          {/*  value={searchTerm}*/}
          {/*  onChange={setSearchTerm}*/}
          {/*/>*/}
            <Button onClick={() => openForm()} variant="success"><UserPlus size={21} /></Button>
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

      {/*{!responsaveisQuery.isLoading && !responsaveisQuery.isError && !hasResponsaveis && (*/}
      {/*  <div*/}
      {/*    title="Nenhum responsável encontrado"*/}
      {/*    description="Ajuste a busca para localizar os cadastros desejados."*/}
      {/*    action={*/}
      {/*      <Button variant="outline" onClick={handleCleanFilter}>*/}
      {/*        Limpar filtros*/}
      {/*        <BrushCleaning size={18} />*/}
      {/*      </Button>*/}
      {/*    }*/}
      {/*  />*/}
      {/*)}*/}

      {hasResponsaveis && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">Nome</th>
                <th className="text-left font-semibold text-base-content/80">Contato</th>
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
