"use client";

import { useMemo, useState, type FormEvent } from "react";
import { ResponsavelForm } from "@/components/responsaveis/ResponsavelForm";
import { useGetResponsaveis } from "@/lib/api/generated/hooks/responsavel/useGetResponsaveis";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

export function ResponsaveisOverview() {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  const responsaveis = useGetResponsaveis({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    sort: ["nome,asc"],
  });

  const content = useMemo(() => responsaveis.data?.content ?? [], [responsaveis.data?.content]);
  const metadata = responsaveis.data?.page;
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

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <div>
          <h2 className="text-2xl font-bold text-base-content">Responsáveis</h2>
          <p className="mt-2 text-sm text-base-content/65">Listagem paginada dos responsáveis cadastrados.</p>
        </div>

        <form className="flex flex-col gap-3 md:flex-row md:items-end" onSubmit={handleSubmit}>
          <label className="form-control w-full md:w-80">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Buscar por nome</span>
            <input
              className="input input-bordered w-full"
              value={searchInput}
              onChange={(event) => setSearchInput(event.target.value)}
              placeholder="Digite o nome do responsável"
            />
          </label>

          <Button type="submit" variant="outline">
            Buscar
          </Button>

          <Button type="button" onClick={() => setIsCreateOpen(true)}>
            Novo responsável
          </Button>
        </form>
      </div>

      {responsaveis.isLoading ? (
        <PageLoading message="Carregando responsáveis..." />
      ) : responsaveis.error ? (
        <div className="mt-6">
          <ErrorCard
            title="Não foi possível carregar a listagem de responsáveis"
            description="A consulta paginada da API falhou para o filtro selecionado."
            error={responsaveis.error}
          />
        </div>
      ) : content.length === 0 ? (
        <EmptyCard
          title="Nenhum responsável encontrado"
          description="Ajuste o filtro atual para localizar os responsáveis desejados."
        />
      ) : (
        <div className="mt-6 space-y-4">
          <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100">
            <table className="table table-zebra">
              <thead className="bg-base-200/80">
                <tr>
                  <th>Nome</th>
                  <th>CPF</th>
                  <th>Telefone</th>
                  <th>E-mail</th>
                </tr>
              </thead>

              <tbody>
                {content.map((responsavel) => (
                  <tr key={responsavel.id}>
                    <td className="font-semibold text-base-content">{responsavel.nome}</td>
                    <td>{formatCpf(responsavel.cpf)}</td>
                    <td>{formatPhone(responsavel.telefone)}</td>
                    <td>{responsavel.email}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="flex flex-col gap-3 rounded-xl border border-base-300 bg-base-100 px-4 py-3 text-sm text-base-content/70 md:flex-row md:items-center md:justify-between">
            <p>
              {totalElements} responsável(is) encontrado(s) • página {currentPage + 1}
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

      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Cadastrar responsável"
        description="Preencha os dados para criar um novo responsável."
        size="md"
      >
        <ResponsavelForm onSuccess={() => setIsCreateOpen(false)} onCancel={() => setIsCreateOpen(false)} />
      </Modal>
    </section>
  );
}
