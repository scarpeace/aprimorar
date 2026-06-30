"use client";

import Link from "next/link";
import { useMemo, useState, type FormEvent } from "react";
import { useGetAlunos } from "@/lib/api/generated/hooks/aluno/useGetAlunos";
import { useGetAlunosKpis } from "@/lib/api/generated/hooks/aluno/useGetAlunosKpis";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { MetricCard } from "@/components/ui/MetricCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { formatCpf } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

function StatusBadge({ active }: Readonly<{ active?: boolean }>) {
  const archived = active === false;

  return (
    <span className={`badge badge-sm ${archived ? "badge-ghost" : "badge-success"}`}>
      {archived ? "Arquivado" : "Ativo"}
    </span>
  );
}

export function AlunosOverview() {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [showArchived, setShowArchived] = useState(false);

  const alunosKpis = useGetAlunosKpis();
  const alunos = useGetAlunos({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: showArchived ? undefined : true,
    sort: ["nome,asc"],
  });

  const content = useMemo(() => alunos.data?.content ?? [], [alunos.data?.content]);
  const metadata = alunos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setPage(0);
    setSearch(searchInput.trim());
  }

  function handleArchivedChange(checked: boolean) {
    setPage(0);
    setShowArchived(checked);
  }

  return (
    <div className="space-y-6">
      <section className="grid gap-3 md:grid-cols-2">
        <MetricCard label="Alunos cadastrados" value={alunosKpis.data?.totalAlunos} />
        <MetricCard label="Alunos ativos" value={alunosKpis.data?.totalAlunosAtivos} />
      </section>

      <section className="app-shell-card p-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <h2 className="text-2xl font-bold text-base-content">Alunos</h2>
            <p className="mt-2 text-sm text-base-content/65">
              Primeira versão da listagem no Next, já usando o contrato atual da API.
            </p>
          </div>

          <form className="flex flex-col gap-3 md:flex-row md:items-end" onSubmit={handleSubmit}>
            <label className="form-control w-full md:w-80">
              <span className="label-text mb-2 text-sm font-medium text-base-content/70">Buscar por nome</span>
              <input
                className="input input-bordered w-full"
                value={searchInput}
                onChange={(event) => setSearchInput(event.target.value)}
                placeholder="Digite o nome do aluno"
              />
            </label>

            <label className="label cursor-pointer gap-3 rounded-xl border border-base-300 px-4 py-3">
              <span className="label-text text-sm text-base-content/70">Mostrar arquivados</span>
              <input
                type="checkbox"
                className="toggle toggle-sm toggle-primary"
                checked={showArchived}
                onChange={(event) => handleArchivedChange(event.target.checked)}
              />
            </label>

            <Button type="submit">Buscar</Button>
          </form>
        </div>

        {alunosKpis.error ? (
          <div className="mt-4">
            <ErrorCard
              title="Não foi possível carregar os KPIs de alunos"
              description="A listagem pode continuar funcionando, mas o resumo superior não ficou disponível."
              error={alunosKpis.error}
            />
          </div>
        ) : null}

        {alunos.isLoading ? (
          <PageLoading message="Carregando alunos..." />
        ) : alunos.error ? (
          <div className="mt-6">
            <ErrorCard
              title="Não foi possível carregar a listagem de alunos"
              description="A consulta paginada da API falhou para os filtros selecionados."
              error={alunos.error}
            />
          </div>
        ) : content.length === 0 ? (
          <EmptyCard
            title="Nenhum aluno encontrado"
            description="Ajuste o filtro atual para localizar os alunos desejados."
          />
        ) : (
          <div className="mt-6 space-y-4">
            <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100">
              <table className="table table-zebra">
                <thead className="bg-base-200/80">
                  <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>E-mail</th>
                    <th>Escola</th>
                    <th>Status</th>
                  </tr>
                </thead>

                <tbody>
                  {content.map((aluno) => (
                    <tr key={aluno.id}>
                      <td>
                        <Link className="font-semibold text-base-content hover:underline" href={`/alunos/${aluno.id}`}>
                          {aluno.nome}
                        </Link>
                      </td>
                      <td>{formatCpf(aluno.cpf)}</td>
                      <td>{aluno.email}</td>
                      <td>{aluno.escola || "—"}</td>
                      <td>
                        <StatusBadge active={aluno.active} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="flex flex-col gap-3 rounded-xl border border-base-300 bg-base-100 px-4 py-3 text-sm text-base-content/70 md:flex-row md:items-center md:justify-between">
              <p>
                {totalElements} aluno(s) encontrado(s) • página {currentPage + 1}
                {totalPages > 0 ? ` de ${totalPages}` : ""}
              </p>

              <div className="flex gap-2">
                <Button type="button" variant="outline" disabled={!hasPrevious} onClick={() => setPage((value) => value - 1)}>
                  Anterior
                </Button>
                <Button type="button" variant="outline" disabled={!hasNext} onClick={() => setPage((value) => value + 1)}>
                  Próxima
                </Button>
              </div>
            </div>
          </div>
        )}
      </section>
    </div>
  );
}
