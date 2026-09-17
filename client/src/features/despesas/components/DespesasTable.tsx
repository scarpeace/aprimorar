"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { DateField } from "@/components/ui/DateField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { SearchInput } from "@/components/ui/SearchInput";
import { SelectField } from "@/components/ui/SelectField";
import { DespesaStatusBadge } from "@/features/despesas/components/DespesaStatusBadge";
import { useGetDespesas } from "@/lib/api/generated/hooks/despesa/useGetDespesas";
import type {
  DespesaResponseCategoriaEnumKey,
  DespesaResponseFormaPagamentoEnumKey,
} from "@/lib/api/generated/types/DespesaResponse";
import {
  categoriaDespesaLabels,
  categoriaDespesaOptions,
  formaPagamentoDespesaOptions,
} from "@/lib/constants/despesa-constants";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatDateShortYear } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;


export function DespesasTable() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [categoria, setCategoria] = useState<DespesaResponseCategoriaEnumKey | "">("");
  const [formaPagamento, setFormaPagamento] = useState<DespesaResponseFormaPagamentoEnumKey | "">("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const search = useDebounce(searchInput.trim(), 300);

  const despesas = useGetDespesas({
    page,
    size: PAGE_SIZE,
    busca: search || undefined,
    categoria: categoria || undefined,
    formaPagamento: formaPagamento || undefined,
    dataInicio: dataInicio || undefined,
    dataFim: dataFim || undefined,
  });

  const content = despesas.data?.content ?? [];
  const metadata = despesas.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;

  function changeFilter(updateFilter: () => void) {
    setPage(0);
    updateFilter();
  }

  function openDespesa(despesaId?: number) {
    if (despesaId) {
      router.push(`/financeiro/despesas/${despesaId}`);
    }
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Despesas cadastradas</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os lançamentos registrados e acesse seus detalhes.
          </p>
        </div>
      </CardHeader>

      <div className="flex flex-wrap items-end gap-3">
        <SearchInput
          label="Buscar"
          value={searchInput}
          onChange={(value) => changeFilter(() => setSearchInput(value))}
          placeholder="Digite o título ou a descrição"
          className="min-w-60 flex-1"
        />

        <SelectField
          label="Categoria"
          value={categoria}
          options={categoriaDespesaOptions}
          onChange={(value) => changeFilter(() => setCategoria(value as DespesaResponseCategoriaEnumKey | ""))}
          className="w-full sm:w-48"
        />

        <SelectField
          label="Forma"
          value={formaPagamento}
          options={formaPagamentoDespesaOptions}
          onChange={(value) =>
            changeFilter(() => setFormaPagamento(value as DespesaResponseFormaPagamentoEnumKey | ""))
          }
          className="w-full sm:w-48"
        />

        <DateField
          label="Início"
          value={dataInicio}
          onChange={(value) => changeFilter(() => setDataInicio(value))}
          className="w-full sm:w-44"
        />

        <DateField
          label="Fim"
          value={dataFim}
          onChange={(value) => changeFilter(() => setDataFim(value))}
          className="w-full sm:w-44"
        />
      </div>

      {despesas.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : despesas.error ? (
        <ErrorCard
          title="Não foi possível carregar as despesas"
          description="A consulta paginada falhou. Tente novamente."
          error={despesas.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhuma despesa encontrada" description="Ajuste os filtros para localizar as despesas desejadas." />
      ) : (
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="table table-zebra">
              <thead>
                <tr>
                  <th>Título</th>
                  <th>Categoria</th>
                  <th>Vencimento</th>
                  <th className="text-right">Valor</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {content.map((despesa) => (
                  <tr
                    key={despesa.id ?? despesa.titulo}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => openDespesa(despesa.id)}
                  >
                    <td className="font-semibold">{despesa.titulo ?? "Não informado"}</td>
                    <td>{despesa.categoria ? categoriaDespesaLabels[despesa.categoria] : "Não informada"}</td>
                    <td>{despesa.dataVencimento ? formatDateShortYear(despesa.dataVencimento) : "Não informado"}</td>
                    <td className="text-right font-semibold">{brl.format(despesa.valor ?? 0)}</td>
                    <td>
                      <DespesaStatusBadge status={despesa.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            summary={<>Mostrando {content.length} de {totalElements} despesa(s)</>}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </Card>
  );
}
