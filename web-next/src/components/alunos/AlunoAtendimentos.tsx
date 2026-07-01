"use client";

import { useState, type FormEvent } from "react";
import { useRouter } from "next/navigation";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { AtendimentoStatusBadge } from "@/components/atendimentos/AtendimentoStatusBadge";
import { AtendimentoTipoBadge } from "@/components/atendimentos/AtendimentoTipoBadge";
import { AtendimentosMonthTabs } from "@/components/atendimentos/AtendimentosMonthTabs";
import { Button } from "@/components/ui/Button";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { useGetAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentos";
import type {
  AtendimentoResponseStatusEnumKey,
  AtendimentoResponseTipoEnumKey,
} from "@/lib/api/generated/types/AtendimentoResponse";
import { atendimentoStatusOptions, atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 20;

export function AlunoAtendimentos({ alunoId }: Readonly<{ alunoId: string }>) {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [tipo, setTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");
  const [selectedYear, setSelectedYear] = useState(() => new Date().getFullYear());
  const [selectedMonthIndex, setSelectedMonthIndex] = useState(() => new Date().getMonth());
  const anoMes = `${selectedYear}-${String(selectedMonthIndex + 1).padStart(2, "0")}`;

  const atendimentos = useGetAtendimentos({
    page,
    size: PAGE_SIZE,
    anoMes,
    alunoId,
    busca: search || undefined,
    status: status || undefined,
    tipo: tipo || undefined,
    sort: ["dataHoraInicio,desc", "id,asc"],
  });

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
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

  function handleStatusChange(value: string) {
    setPage(0);
    setStatus(value as AtendimentoResponseStatusEnumKey | "");
  }

  function handleTipoChange(value: string) {
    setPage(0);
    setTipo(value as AtendimentoResponseTipoEnumKey | "");
  }

  function handlePreviousYear() {
    setSelectedYear((value) => value - 1);
    setPage(0);
  }

  function handleNextYear() {
    setSelectedYear((value) => value + 1);
    setPage(0);
  }

  function handleMonthChange(monthIndex: number) {
    setSelectedMonthIndex(monthIndex);
    setPage(0);
  }

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <div>
          <h2 className="text-xl font-bold text-base-content">Eventos vinculados</h2>
          <p className="mt-2 text-sm text-base-content/65">Veja os atendimentos ligados a este aluno por período, busca e filtros.</p>
        </div>

        <form className="flex flex-col gap-3" onSubmit={handleSubmit}>
          <div className="flex flex-col gap-3 md:flex-row md:items-end">
            <SearchInput label="Buscar" value={searchInput} onChange={setSearchInput} placeholder="Digite colaborador ou tipo" />

            <label className="form-control w-full md:w-56">
              <span className="label-text mb-2 text-sm font-medium text-base-content/70">Status</span>
              <select className="select select-bordered w-full" value={status} onChange={(event) => handleStatusChange(event.target.value)}>
                {atendimentoStatusOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </label>

            <label className="form-control w-full md:w-64">
              <span className="label-text mb-2 text-sm font-medium text-base-content/70">Tipo</span>
              <select className="select select-bordered w-full" value={tipo} onChange={(event) => handleTipoChange(event.target.value)}>
                {atendimentoTipoOptions.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </label>

            <Button type="submit" variant="outline">
              Buscar
            </Button>
          </div>
        </form>
      </div>

      <AtendimentosMonthTabs
        selectedYear={selectedYear}
        selectedMonthIndex={selectedMonthIndex}
        onPreviousYear={handlePreviousYear}
        onNextYear={handleNextYear}
        onMonthChange={handleMonthChange}
      >
        {atendimentos.isLoading ? (
          <PageLoading message="Carregando eventos do aluno..." />
        ) : atendimentos.error ? (
          <ErrorCard
            title="Não foi possível carregar os eventos do aluno"
            description="A consulta paginada da API falhou para os filtros selecionados."
            error={atendimentos.error}
          />
        ) : content.length === 0 ? (
          <EmptyCard title="Nenhum evento encontrado" description="Ajuste os filtros ou selecione outro período." />
        ) : (
          <div className="space-y-4">
            <div className="hidden overflow-x-auto rounded-2xl border border-base-300 bg-base-100 md:block">
              <table className="table table-zebra">
                <thead className="bg-base-200/80">
                  <tr>
                    <th>Colaborador</th>
                    <th>Data</th>
                    <th>Horário</th>
                    <th>Tipo</th>
                    <th>Status</th>
                    <th className="text-right">Valor</th>
                    <th>Pagamento</th>
                  </tr>
                </thead>

                <tbody>
                  {content.map((atendimento) => (
                    <tr
                      key={atendimento.id}
                      className="cursor-pointer transition-colors hover:bg-base-200/70"
                      onClick={() => router.push(`/atendimentos/${atendimento.id}`)}
                    >
                      <td className="font-semibold text-base-content">{atendimento.nomeColaborador}</td>
                      <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                      <td>
                        {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
                      </td>
                      <td>
                        <AtendimentoTipoBadge tipo={atendimento.tipo} />
                      </td>
                      <td>
                        <AtendimentoStatusBadge status={atendimento.status} />
                      </td>
                      <td className="text-right">{brl.format(atendimento.pagamentoAluno)}</td>
                      <td>
                        <div className="flex flex-col gap-1">
                          <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="grid gap-4 md:hidden">
              {content.map((atendimento) => (
                <article
                  key={atendimento.id}
                  className="cursor-pointer rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm transition-colors hover:bg-base-200/40"
                  onClick={() => router.push(`/atendimentos/${atendimento.id}`)}
                >
                  <div className="flex items-start justify-between gap-3">
                    <div>
                      <AtendimentoTipoBadge tipo={atendimento.tipo} />
                      <h3 className="mt-1 font-semibold text-base-content">{atendimento.nomeColaborador}</h3>
                    </div>

                    <AtendimentoStatusBadge status={atendimento.status} />
                  </div>

                  <div className="mt-4 grid gap-2 rounded-2xl border border-base-200 bg-base-200/35 p-3 text-sm">
                    <p>
                      <span className="font-medium text-base-content">Data:</span> {formatDateShortYear(atendimento.dataHoraInicio)}
                    </p>
                    <p>
                      <span className="font-medium text-base-content">Horário:</span> {formatTime(atendimento.dataHoraInicio)} -{" "}
                      {formatTime(atendimento.dataHoraFim)}
                    </p>
                    <p>
                      <span className="font-medium text-base-content">Valor:</span> {brl.format(atendimento.pagamentoAluno)}
                    </p>
                  </div>

                  <div className="mt-3 flex flex-wrap gap-2">
                    <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
                  </div>
                </article>
              ))}
            </div>

            <TablePagination
              summary={`${totalElements} evento(s) encontrado(s) • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
              hasPrevious={hasPrevious}
              hasNext={hasNext}
              onPrevious={() => setPage((value) => value - 1)}
              onNext={() => setPage((value) => value + 1)}
            />
          </div>
        )}
      </AtendimentosMonthTabs>
    </section>
  );
}
