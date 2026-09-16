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

import { useGetColaboradores } from "@/lib/api/generated/hooks/colaborador/useGetColaboradores";
import { useDebounce } from "@/lib/hooks/use-debounce";

const PAGE_SIZE = 10;

export function ColaboradoresTable() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [mostrarInativos, setMostrarInativos] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);

  const colaboradores = useGetColaboradores({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: !mostrarInativos,
  });

  const content = colaboradores.data?.content ?? [];
  const metadata = colaboradores.data?.page;
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
          <CardTitle>Colaboradores cadastrados</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os colaboradores registrados e acesse seus detalhes.
          </p>
        </div>
      </CardHeader>

      <div className="flex flex-wrap items-end gap-3">
        <SearchInput
          label="Buscar"
          value={searchInput}
          onChange={handleSearchChange}
          placeholder="Digite o nome do colaborador"
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

      {colaboradores.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : colaboradores.error ? (
        <ErrorCard
          title="Não foi possível carregar os colaboradores"
          description="A consulta paginada falhou. Tente novamente."
          error={colaboradores.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard
          title="Nenhum colaborador encontrado"
          description="Ajuste os filtros para localizar os colaboradores desejados."
        />
      ) : (
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="table table-zebra">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>Função</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {content.map((colaborador) => (
                  <tr
                    key={colaborador.id ?? colaborador.nome}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => {
                      if (colaborador.id) {
                        router.push(`/colaboradores/${colaborador.id}`);
                      }
                    }}
                  >
                    <td className="font-semibold">{colaborador.nome ?? "Não informado"}</td>
                    <td>{colaborador.funcao ?? "Não informada"}</td>
                    <td>
                      <ActiveStatusIndicator active={colaborador.ativo ?? false} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            summary={<>Mostrando {content.length} de {totalElements} colaborador(es)</>}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </Card>
  );
}
