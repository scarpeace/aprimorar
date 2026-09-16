"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useGetColaboradores } from "@/lib/api/generated/hooks/colaborador/useGetColaboradores";
import { ActiveStatusBadge } from "@/components/ui/ActiveStatusBadge";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { Toggle } from "@/components/ui/Toggle";
import { useDebounce } from "@/lib/hooks/use-debounce";

const PAGE_SIZE = 10;

export function ColaboradoresTable() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [somenteAtivos, setSomenteAtivos] = useState(true);
  const search = useDebounce(searchInput.trim(), 300);

  const colaboradores = useGetColaboradores({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: somenteAtivos ? true : undefined,
    sort: ["nome,asc"],
  });

  const content = colaboradores.data?.content ?? [];
  const metadata = colaboradores.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleAtivosChange(checked: boolean) {
    setPage(0);
    setSomenteAtivos(checked);
  }

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-end">
        <div className="flex w-full items-center gap-3 lg:w-auto">
          <SearchInput
            className="min-w-0 flex-1 sm:w-80 sm:flex-none"
            value={searchInput}
            onChange={handleSearchChange}
            placeholder="Digite o nome do colaborador"
          />

          <span className="text-base-500 text-sm">Ativos</span>
          <Toggle
            checked={somenteAtivos}
            ariaLabel="Mostrar somente colaboradores ativos"
            onChange={(event) => handleAtivosChange(event.target.checked)}
          />
        </div>
      </div>

      {colaboradores.isLoading ? (
        <PageLoading message="Carregando colaboradores..." />
      ) : colaboradores.error ? (
        <div className="mt-6">
          <ErrorCard
            title="Não foi possível carregar a listagem de colaboradores"
            description="A consulta paginada da API falhou para o filtro selecionado."
            error={colaboradores.error}
          />
        </div>
      ) : content.length === 0 ? (
        <EmptyCard
          title="Nenhum colaborador encontrado"
          description="Ajuste o filtro atual para localizar os colaboradores desejados."
        />
      ) : (
        <div className="mt-6 space-y-4">
          <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100">
            <table className="table table-zebra">
              <thead className="bg-base-200/80">
                <tr>
                  <th>Nome</th>
                  <th>Função</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {content.map((colaborador) => (
                  <tr
                    key={colaborador.id}
                    className="cursor-pointer transition-colors hover:bg-base-200/70"
                    onClick={() => router.push(`/colaboradores/${colaborador.id}`)}
                  >
                    <td className="font-semibold text-base-content">{colaborador.nome}</td>
                    <td>{colaborador.funcao}</td>
                    <td>
                      <ActiveStatusBadge active={colaborador.ativo ?? false} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <TablePagination
            summary={`${totalElements} registros encontrados • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
            hasPrevious={hasPrevious}
            hasNext={hasNext}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </section>
  );
}
