"use client";

import Link from "next/link";
import { useMemo, useState, type FormEvent } from "react";
import { ResponsavelForm } from "@/components/responsaveis/ResponsavelForm";
import type { ResponsavelResponseDTO } from "@/lib/api/generated/types/ResponsavelResponseDTO";
import { useGetResponsaveis } from "@/lib/api/generated/hooks/responsavel/useGetResponsaveis";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

export function ResponsaveisOverview() {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [editingResponsavel, setEditingResponsavel] = useState<ResponsavelResponseDTO | null>(null);

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
          <SearchInput label="Buscar por nome" value={searchInput} onChange={setSearchInput} placeholder="Digite o nome do responsável" />

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
                  <th className="w-1">Ações</th>
                </tr>
              </thead>

              <tbody>
                {content.map((responsavel) => (
                  <tr key={responsavel.id}>
                    <td>
                      <Link className="font-semibold text-base-content hover:underline" href={`/responsaveis/${responsavel.id}`}>
                        {responsavel.nome}
                      </Link>
                    </td>
                    <td>{formatCpf(responsavel.cpf)}</td>
                    <td>{formatPhone(responsavel.telefone)}</td>
                    <td>{responsavel.email}</td>
                    <td>
                      <Button type="button" size="sm" variant="outline" onClick={() => setEditingResponsavel(responsavel)}>
                        Editar
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <TablePagination
            summary={`${totalElements} responsável(is) encontrado(s) • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
            hasPrevious={hasPrevious}
            hasNext={hasNext}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
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

      <Modal
        isOpen={!!editingResponsavel}
        onClose={() => setEditingResponsavel(null)}
        title="Editar responsável"
        description="Atualize os dados do responsável sem sair da listagem."
        size="md"
      >
        {editingResponsavel ? (
          <ResponsavelForm
            initialData={editingResponsavel}
            onSuccess={() => setEditingResponsavel(null)}
            onCancel={() => setEditingResponsavel(null)}
          />
        ) : null}
      </Modal>
    </section>
  );
}
