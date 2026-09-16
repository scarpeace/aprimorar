"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { ActiveStatusIndicator } from "@/components/ui/ActiveStatusIndicator";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { SearchInput } from "@/components/ui/SearchInput";
import { useGetAlunos } from "@/lib/api/generated/hooks/aluno/useGetAlunos";
import { useDebounce } from "@/lib/hooks/use-debounce";

const PAGE_SIZE = 10;

export function AlunosTable() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [mostrarInativos, setMostrarInativos] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);

  const alunos = useGetAlunos({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: !mostrarInativos,
  });

  const content = alunos.data?.content ?? [];
  const metadata = alunos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  function handleInativosChange(checked: boolean) {
    setPage(0);
    setMostrarInativos(checked);
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Alunos cadastrados</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Consulte os alunos registrados e acesse seus detalhes.</p>
        </div>
      </CardHeader>

      <div className="flex flex-wrap items-end gap-3">
        <SearchInput
          label="Buscar"
          value={searchInput}
          onChange={handleSearchChange}
          placeholder="Digite o nome do aluno"
          className="min-w-60 flex-1"
        />

        <div className="flex flex-col items-center gap-1">
          <span className="text-sm font-bold text-base-content/70">Inativos</span>
          <input
            type="checkbox"
            className="checkbox checkbox-sm"
            checked={mostrarInativos}
            onChange={(event) => handleInativosChange(event.target.checked)}
          />
        </div>
      </div>

      {alunos.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : alunos.error ? (
        <ErrorCard
          title="Não foi possível carregar os alunos"
          description="A consulta paginada falhou. Tente novamente."
          error={alunos.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhum aluno encontrado" description="Ajuste os filtros para localizar os alunos desejados." />
      ) : (
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="table table-zebra">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>Escola</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {content.map((aluno) => (
                  <tr
                    key={aluno.id ?? aluno.nome}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => {
                      if (aluno.id) {
                        router.push(`/alunos/${aluno.id}`);
                      }
                    }}
                  >
                    <td className="font-semibold">{aluno.nome ?? "Não informado"}</td>
                    <td>{aluno.escola ?? "Não informada"}</td>
                    <td>
                      <ActiveStatusIndicator active={aluno.ativo ?? false} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <p className="text-sm text-base-content/70">
              Mostrando {content.length} de {totalElements} aluno(s)
            </p>

            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPrevious={() => setPage((value) => value - 1)}
              onNext={() => setPage((value) => value + 1)}
            />
          </div>
        </div>
      )}
    </Card>
  );
}
