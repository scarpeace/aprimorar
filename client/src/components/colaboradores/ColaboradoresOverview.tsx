"use client";

import { useMemo, useState, type FormEvent } from "react";
import { useRouter } from "next/navigation";
import { ColaboradorForm } from "@/components/colaboradores/ColaboradorForm";
import { useGetColaboradores } from "@/lib/api/generated/hooks/colaborador/useGetColaboradores";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";
import { Plus } from "lucide-react";

const PAGE_SIZE = 10;

function StatusBadge({ active }: Readonly<{ active?: boolean }>) {
  const archived = active === false;

  return (
    <span className={`badge badge-sm ${archived ? "badge-ghost" : "badge-success"}`}>
      {archived ? "Arquivado" : "Ativo"}
    </span>
  );
}

export function ColaboradoresOverview() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [showArchived, setShowArchived] = useState(false);
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  const colaboradores = useGetColaboradores({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    ativos: showArchived ? undefined : true,
    sort: ["nome,asc"],
  });

  const content = useMemo(() => colaboradores.data?.content ?? [], [colaboradores.data?.content]);
  const metadata = colaboradores.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const pageSize = metadata?.size ?? PAGE_SIZE;
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

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <div className="flex justify-between items-center">
          <h2 className="text-2xl font-bold text-base-content">Colaboradores</h2>
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

          <Button type="button" onClick={() => setIsCreateOpen(true)}>
            <Plus size={18}/>
          </Button>
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
                  <th className="hidden">CPF</th>
                  <th className="hidden">Telefone</th>
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
                    <td className="hidden">{formatCpf(colaborador.cpf)}</td>
                    <td className="hidden">{formatPhone(colaborador.telefone)}</td>
                    <td>{colaborador.funcao}</td>
                    <td>
                      <StatusBadge active={colaborador.active} />
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

      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Cadastrar colaborador"
        description="Preencha os dados para criar um novo colaborador."
        size="lg"
      >
        <ColaboradorForm onSuccess={() => setIsCreateOpen(false)} onCancel={() => setIsCreateOpen(false)} />
      </Modal>
    </section>
  );
}
