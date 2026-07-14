"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { AlunoForm } from "@/components/alunos/AlunoForm";
import { useGetAlunos } from "@/lib/api/generated/hooks/aluno/useGetAlunos";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { useDebounce } from "@/hooks/useDebounce";
import { formatCpf } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

function StatusBadge({ active }: Readonly<{ active?: boolean }>) {
  const archived = active === false;

  return (
    <span
      className={`badge badge-sm ${archived ? "badge-ghost" : "badge-success"}`}
    >
      {archived ? "Arquivado" : "Ativo"}
    </span>
  );
}

export function AlunosOverview() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [showArchived, setShowArchived] = useState(false);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);

  const alunos = useGetAlunos({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: showArchived ? undefined : true,
    sort: ["nome,asc"],
  });

  const content = alunos.data?.content ?? [];
  const metadata = alunos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleArchivedChange(checked: boolean) {
    setPage(0);
    setShowArchived(checked);
  }

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  return (
    <section className="app-shell-card h-full p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">

        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold text-base-content">Alunos</h2>
          <label className="label cursor-pointer gap-2 rounded-xl border border-base-300 px-3 py-2">
            <span className="label-text text-sm text-base-content/70">
              Arquivados
            </span>
            <input
              type="checkbox"
              className="checkbox checkbox-sm"
              checked={showArchived}
              onChange={(event) => handleArchivedChange(event.target.checked)}
            />
          </label>
        </div>

        <div className="flex items-end gap-3">
          <SearchInput
            label="Buscar por nome"
            value={searchInput}
            onChange={handleSearchChange}
            placeholder="Digite o nome do aluno"
          />

          <Button
            type="button"
            size="sm"
            className="btn-square mb-1"
            aria-label="Novo aluno"
            title="Novo aluno"
            onClick={() => setIsCreateOpen(true)}
          >
            <Plus size={18} />
          </Button>
        </div>
      </div>

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
                  <th className="hidden">CPF</th>
                  <th className="hidden">E-mail</th>
                  <th>Escola</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {content.map((aluno) => (
                  <tr
                    key={aluno.id}
                    className="cursor-pointer transition-colors hover:bg-base-200/70"
                    onClick={() => router.push(`/alunos/${aluno.id}`)}
                  >
                    <td className="font-semibold text-base-content">
                      {aluno.nome}
                    </td>
                    <td className="hidden">{formatCpf(aluno.cpf)}</td>
                    <td className="hidden">{aluno.email}</td>
                    <td>{aluno.escola || "—"}</td>
                    <td>
                      <StatusBadge active={aluno.active} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <TablePagination
            summary={`${totalElements} aluno(s) encontrado(s) • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
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
        title="Cadastrar aluno"
        description="Preencha os dados para criar um novo aluno."
        size="lg"
      >
        <AlunoForm
          onSuccess={() => setIsCreateOpen(false)}
          onCancel={() => setIsCreateOpen(false)}
        />
      </Modal>
    </section>
  );
}
