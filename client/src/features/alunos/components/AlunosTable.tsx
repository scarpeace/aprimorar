import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetAlunos } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { formatCpf } from "@/lib/utils/formatter";
import { BrushCleaning, CircleAlert, Eye, EyeClosed, FolderClock, UserPlus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function AlunosTable({ openForm }: { openForm: () => void }) {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const alunosQuery = useGetAlunos({
    page: currentPage,
    nome: debouncedSearchTerm || undefined,
    ativos: showArchived ? false : true,
    sort: ["nome,asc"],
  });

  const alunos = alunosQuery.data?.content ?? [];
  const page = alunosQuery.data?.page;
  const totalElements = page?.totalElements ?? 0;
  const totalPages = page?.totalPages ?? 0;
  const pageSize = page?.size ?? alunos.length;
  const hasAlunos = alunos.length > 0;

  const handleShowArchived = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  const handleCleanFilter = () => {
    setSearchTerm("");
    setShowArchived(false);
    setCurrentPage(0);
  };

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <div className="flex justify-between items-center">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Alunos</h3>
              <p className="text-sm text-base-content/60">Selecione um aluno para ver os detalhes.</p>
            </div>
          </div>

          <ListSearchInput
            className="grow"
            placeholder="Nome, email ou CPF"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />

          {/*<div className="tooltip flex" data-tip={"Mostrar alunos arquivados"}>
          <div className="flex flex-col gap-1 items-center">
            <span className="text-sm text-secondary"><FolderClock size={21}/></span>
            <label className="toggle text-base h-5 p-1">
                <input type="checkbox" checked={showArchived} onChange={() => handleShowArchived(!showArchived)} />
                <EyeClosed size={15} />
                <Eye size={15} />
              </label>
            </div>
          </div>*/}

        <Button onClick={() => openForm()} variant="success"><UserPlus size={21} /></Button>
        </div>
      </section>

      {alunosQuery.isError && (
        <ErrorCard title="Não foi possível carregar a listagem de alunos" error={alunosQuery.error} />
      )}

      {alunosQuery.isLoading && (
        <LoadingSpinner text="Carregando alunos..." />
      )}

      {!alunosQuery.isLoading && !alunosQuery.isError && !hasAlunos && (
        <EmptyCard
          title="Nenhum aluno encontrado"
          description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
          action={
            <Button variant="outline" onClick={handleCleanFilter}>
              Limpar filtros
              <BrushCleaning size={18} />
            </Button>
          }
        />
      )}

      {hasAlunos && (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">Nome</th>
                <th className="text-left font-semibold text-base-content/80">CPF</th>
                <th className="text-left font-semibold text-base-content/80">Idade</th>
                <th className="text-left font-semibold text-base-content/80">Status</th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {alunos.map((aluno) => {
                const isArchived = aluno.active === false;

                return (
                  <tr
                    key={aluno.id}
                    className="transition-colors hover:bg-base-200/70 hover:cursor-pointer"
                    onClick={() => navigate(`/alunos/${aluno.id}`)}
                  >
                    <td className="font-bold">{aluno.nome}</td>
                    <td>{formatCpf(aluno.cpf)}</td>
                    <td>{aluno.idade}</td>
                    <td>
                      <span className={`badge ${isArchived ? "badge-ghost" : "badge-success"} badge-sm`}>
                        {isArchived ? "Arquivado" : "Ativo"}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <Pagination
        totalElements={totalElements}
        totalPages={totalPages}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
        size={pageSize}
      />
    </main>
  );
}
