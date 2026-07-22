"use client";

import { PencilLine, Plus, Trash2 } from "lucide-react";
import { useState, type MouseEvent } from "react";
import { Button } from "@/components/ui/Button";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { DespesaForm } from "@/components/despesas/DespesaForm";
import { useDebounce } from "@/hooks/useDebounce";
import { useDespesaMutations } from "@/hooks/use-despesa-mutations";
import { useGetDespesas } from "@/lib/api/generated/hooks/despesa/useGetDespesas";
import type {
  DespesaResponse,
  DespesaResponseCategoriaEnumKey,
  DespesaResponseFormaPagamentoEnumKey,
} from "@/lib/api/generated/types/DespesaResponse";
import {
  categoriaDespesaLabels,
  categoriaDespesaOptions,
  formaPagamentoDespesaLabels,
  formaPagamentoDespesaOptions,
} from "@/lib/constants/despesa-constants";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

export function DespesasOverview() {
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [categoria, setCategoria] = useState<DespesaResponseCategoriaEnumKey | "">("");
  const [formaPagamento, setFormaPagamento] = useState<DespesaResponseFormaPagamentoEnumKey | "">("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [editingDespesa, setEditingDespesa] = useState<DespesaResponse | null>(null);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const search = useDebounce(searchInput.trim(), 300);
  const { deleteDespesa, togglePagamentoDespesa } = useDespesaMutations();

  const despesas = useGetDespesas({
    page,
    size: PAGE_SIZE,
    busca: search || undefined,
    categoria: categoria || undefined,
    formaPagamento: formaPagamento || undefined,
    dataInicio: dataInicio || undefined,
    dataFim: dataFim || undefined,
    sort: ["createdAt,desc", "id,desc"],
  });

  const content = despesas.data?.content ?? [];
  const metadata = despesas.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  function handleDelete(event: MouseEvent<HTMLButtonElement>, despesa: DespesaResponse) {
    event.stopPropagation();

    if (!despesa.id || !window.confirm("Deseja mesmo excluir esta despesa?")) {
      return;
    }

    deleteDespesa.mutate({ despesaId: despesa.id });
  }

  function handleTogglePagamento(despesa: DespesaResponse) {
    if (!despesa.id) {
      return;
    }

    togglePagamentoDespesa.mutate({ despesaId: despesa.id });
  }

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <div>
          <h2 className="text-2xl font-bold text-base-content">Despesas</h2>
          <p className="mt-2 text-sm text-base-content/65">Gerencie os gastos operacionais da instituição.</p>
        </div>

        <div className="flex items-end gap-3">
          <SearchInput label="Buscar" value={searchInput} onChange={handleSearchChange} placeholder="Digite título ou descrição" />

          <Button
            type="button"
            size="sm"
            className="btn-square mb-1 shrink-0"
            aria-label="Nova despesa"
            title="Nova despesa"
            onClick={() => setIsCreateOpen(true)}
          >
            <Plus size={18} />
          </Button>
        </div>
      </div>

      <div className="mt-4 grid gap-3 md:grid-cols-4">
        <label className="form-control min-w-0">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Categoria</span>
          <select
            className="select select-bordered w-full"
            value={categoria}
            onChange={(event) => {
              setPage(0);
              setCategoria(event.target.value as DespesaResponseCategoriaEnumKey | "");
            }}
          >
            {categoriaDespesaOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>

        <label className="form-control min-w-0">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Forma</span>
          <select
            className="select select-bordered w-full"
            value={formaPagamento}
            onChange={(event) => {
              setPage(0);
              setFormaPagamento(event.target.value as DespesaResponseFormaPagamentoEnumKey | "");
            }}
          >
            {formaPagamentoDespesaOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>

        <label className="form-control min-w-0">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Início</span>
          <input
            type="date"
            className="input input-bordered w-full"
            value={dataInicio}
            onChange={(event) => {
              setPage(0);
              setDataInicio(event.target.value);
            }}
          />
        </label>

        <label className="form-control min-w-0">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Fim</span>
          <input
            type="date"
            className="input input-bordered w-full"
            value={dataFim}
            onChange={(event) => {
              setPage(0);
              setDataFim(event.target.value);
            }}
          />
        </label>
      </div>

      <div className="mt-6">
        {despesas.isLoading ? (
          <PageLoading message="Carregando despesas..." />
        ) : despesas.error ? (
          <ErrorCard
            title="Não foi possível carregar as despesas"
            description="A consulta paginada da API falhou para os filtros selecionados."
            error={despesas.error}
          />
        ) : content.length === 0 ? (
          <EmptyCard title="Nenhuma despesa encontrada" description="Ajuste os filtros ou cadastre uma nova despesa." />
        ) : (
          <div className="space-y-4">
            <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100">
              <table className="table table-zebra">
                <thead className="bg-base-200/80">
                  <tr>
                    <th>Título</th>
                    <th>Categoria</th>
                    <th>Forma</th>
                    <th className="text-right">Valor</th>
                    <th>Status pagamento</th>
                    <th className="w-24">Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {content.map((despesa) => (
                    <tr
                      key={despesa.id}
                      className="cursor-pointer transition-colors hover:bg-base-200/70"
                      onClick={() => setEditingDespesa(despesa)}
                    >
                      <td>
                        <p className="font-semibold text-base-content">{despesa.titulo}</p>
                        {despesa.descricao ? <p className="mt-1 max-w-md truncate text-xs text-base-content/55">{despesa.descricao}</p> : null}
                      </td>
                      <td>{despesa.categoria ? categoriaDespesaLabels[despesa.categoria] : "—"}</td>
                      <td>{despesa.formaPagamento ? formaPagamentoDespesaLabels[despesa.formaPagamento] : "—"}</td>
                      <td className="text-right font-semibold">{brl.format(despesa.valor ?? 0)}</td>
                      <td>
                        <div className="flex items-center gap-2">
                          <AtendimentoPaymentBadge label={despesa.dataPagamento ? "Pago" : "Pendente"} paidAt={despesa.dataPagamento} />
                          <input
                            type="checkbox"
                            className={`toggle toggle-sm ${despesa.dataPagamento ? "toggle-success" : "toggle-warning"}`}
                            checked={!!despesa.dataPagamento}
                            disabled={togglePagamentoDespesa.isPending}
                            aria-label="Alternar pagamento da despesa"
                            onClick={(event) => event.stopPropagation()}
                            onChange={(event) => {
                              event.stopPropagation();
                              handleTogglePagamento(despesa);
                            }}
                          />
                        </div>
                      </td>
                      <td>
                        <div className="flex gap-2">
                          <Button
                            type="button"
                            variant="outline"
                            size="xs"
                            className="btn-square"
                            aria-label="Editar despesa"
                            title="Editar despesa"
                            onClick={(event) => {
                              event.stopPropagation();
                              setEditingDespesa(despesa);
                            }}
                          >
                            <PencilLine size={14} />
                          </Button>
                          <Button
                            type="button"
                            variant="error"
                            size="xs"
                            className="btn-square"
                            aria-label="Excluir despesa"
                            title="Excluir despesa"
                            disabled={deleteDespesa.isPending}
                            onClick={(event) => handleDelete(event, despesa)}
                          >
                            <Trash2 size={14} />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <TablePagination
              summary={`${totalElements} despesa(s) encontrada(s) • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
              hasPrevious={hasPrevious}
              hasNext={hasNext}
              onPrevious={() => setPage((value) => value - 1)}
              onNext={() => setPage((value) => value + 1)}
            />
          </div>
        )}
      </div>

      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Cadastrar despesa"
        description="Preencha os dados para registrar uma despesa operacional."
        size="lg"
      >
        <DespesaForm onSuccess={() => setIsCreateOpen(false)} onCancel={() => setIsCreateOpen(false)} />
      </Modal>

      <Modal
        isOpen={!!editingDespesa}
        onClose={() => setEditingDespesa(null)}
        title="Editar despesa"
        description="Atualize os dados da despesa selecionada."
        size="lg"
      >
        {editingDespesa ? (
          <DespesaForm initialData={editingDespesa} onSuccess={() => setEditingDespesa(null)} onCancel={() => setEditingDespesa(null)} />
        ) : null}
      </Modal>
    </section>
  );
}
