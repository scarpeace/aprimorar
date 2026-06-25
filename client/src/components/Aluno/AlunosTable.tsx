import { Button } from "@/components/Ui/Button.tsx";
import { EmptyCard } from "@/components/Ui/EmptyCard";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { TextSearchInput } from "@/components/Ui/TextSearchInput";
import { ToggleSwitch } from "@/components/Ui/ToggleSwitch";
import { useGetAlunos } from "@/kubb";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { formatCpf } from "@/utils/formatter.ts";
import { UserPlus } from "lucide-react";
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
    ativos: showArchived ? undefined : true,
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

  return (
    <main>
      <section className="my-3 animate-[fade-up_220ms_ease-out_both]">
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h3 className="text-2xl font-bold text-base-content">Alunos</h3>
            <p className="text-sm text-base-content/60">Selecione um aluno para ver os detalhes.</p>
          </div>

          <TextSearchInput
            label="Pesquisar"
            className="grow"
            placeholder="Nome, email ou CPF"
            onChange={(value) => {
              setSearchTerm(value);
              setCurrentPage(0);
            }}
          />

          <ToggleSwitch
            label="Mostrar Arquivados"
            tip="Exibe alunos arquivados junto com os ativos"
            checked={showArchived}
            setToggle={handleShowArchived}
            variant="info"
          />

          <Button onClick={() => openForm()} variant="success">
            <UserPlus size={21} />
          </Button>
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
          description="Ajuste a busca para localizar os cadastros desejados."
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
