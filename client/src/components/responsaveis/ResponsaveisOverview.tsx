"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { ResponsavelForm } from "@/components/responsaveis/ResponsavelForm";
import { useGetResponsaveis } from "@/lib/api/generated/hooks/responsavel/useGetResponsaveis";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { useDebounce } from "@/hooks/useDebounce";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

export function ResponsaveisOverview() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);

  const responsaveis = useGetResponsaveis({
    page,
    size: PAGE_SIZE,
    nome: search || undefined,
    sort: ["nome,asc"],
  });

  const content = responsaveis.data?.content ?? [];
  const metadata = responsaveis.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  return (
    <section className="app-shell-card h-full p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <h2 className="text-2xl font-bold text-base-content">Responsáveis</h2>

        <div className="flex items-end gap-3">
          <SearchInput
            label="Buscar por nome"
            value={searchInput}
            onChange={handleSearchChange}
            placeholder="Digite o nome do responsável"
          />

          <Button
            type="button"
            size="sm"
            className="btn-square mb-1"
            aria-label="Novo responsável"
            title="Novo responsável"
            onClick={() => setIsCreateOpen(true)}
          >
            <Plus size={18} />
          </Button>
        </div>
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
                  <th className="hidden">CPF</th>
                  <th>Telefone</th>
                  <th className="hidden">E-mail</th>
                </tr>
              </thead>

              <tbody>
                {content.map((responsavel) => (
                  <tr
                    key={responsavel.id}
                    className="cursor-pointer transition-colors hover:bg-base-200/70"
                    onClick={() =>
                      router.push(`/responsaveis/${responsavel.id}`)
                    }
                  >
                    <td className="font-semibold text-base-content">
                      {responsavel.nome}
                    </td>
                    <td className="hidden">{formatCpf(responsavel.cpf)}</td>
                    <td className="text-xs">
                      {formatPhone(responsavel.telefone)}
                    </td>
                    <td className="hidden">{responsavel.email}</td>
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
        <ResponsavelForm
          onSuccess={() => setIsCreateOpen(false)}
          onCancel={() => setIsCreateOpen(false)}
        />
      </Modal>
    </section>
  );
}
