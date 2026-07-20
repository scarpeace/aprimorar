"use client";

import { useState } from "react";
import { endOfMonth, format, startOfMonth } from "date-fns";
import { useRouter } from "next/navigation";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { AtendimentoStatusBadge } from "@/components/atendimentos/AtendimentoStatusBadge";
import { AtendimentoTipoBadge } from "@/components/atendimentos/AtendimentoTipoBadge";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { useDebounce } from "@/hooks/useDebounce";
import { useAtendimentoMutations } from "@/hooks/use-atendimento-mutations";
import { useGetAtendimentos } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentos";
import { useGetResumoFinanceiroColaborador } from "@/lib/api/generated/hooks/atendimento/useGetResumoFinanceiroColaborador";
import type {
  AtendimentoResponseStatusEnumKey,
  AtendimentoResponseTipoEnumKey,
} from "@/lib/api/generated/types/AtendimentoResponse";
import { atendimentoStatusOptions, atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 20;
const DATE_INPUT_FORMAT = "yyyy-MM-dd";

function getCurrentMonthPeriod() {
  const today = new Date();

  return {
    dataInicio: format(startOfMonth(today), DATE_INPUT_FORMAT),
    dataFim: format(endOfMonth(today), DATE_INPUT_FORMAT),
  };
}

function toStartDateTime(date: string) {
  return date ? `${date}T00:00:00` : undefined;
}

function toEndDateTime(date: string) {
  return date ? `${date}T23:59:59` : undefined;
}

function SummaryCard({
  label,
  value,
  valueClassName = "text-base-content",
}: Readonly<{
  label: string;
  value: string | number;
  valueClassName?: string;
}>) {
  return (
    <div className="rounded-xl border border-base-300 bg-base-200/25 p-4">
      <p className="text-xs font-bold uppercase tracking-wider text-base-content/50">{label}</p>
      <p className={`mt-2 text-xl font-bold ${valueClassName}`}>{value}</p>
    </div>
  );
}

function CollaboratorRepasseToggle({
  paid,
  disabled,
  onToggle,
}: Readonly<{
  paid: boolean;
  disabled: boolean;
  onToggle: () => void;
}>) {
  return (
    <input
      type="checkbox"
      className="toggle toggle-success toggle-sm"
      checked={paid}
      disabled={disabled}
      aria-label="Alternar repasse do colaborador"
      onClick={(event) => event.stopPropagation()}
      onChange={onToggle}
    />
  );
}

type ColaboradorAtendimentosProps = {
  colaboradorId: string;
};

export function ColaboradorAtendimentos({ colaboradorId }: Readonly<ColaboradorAtendimentosProps>) {
  const router = useRouter();
  const { toggleRepasseColaborador } = useAtendimentoMutations();
  const [page, setPage] = useState(0);
  const [periodo, setPeriodo] = useState(getCurrentMonthPeriod);
  const [searchInput, setSearchInput] = useState("");
  const [status, setStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [tipo, setTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");
  const search = useDebounce(searchInput.trim(), 300);
  const { dataInicio, dataFim } = periodo;
  const hasPeriod = !!dataInicio && !!dataFim;

  const atendimentos = useGetAtendimentos(
    {
      page,
      size: PAGE_SIZE,
      colaboradorId,
      inicio: toStartDateTime(dataInicio),
      fim: toEndDateTime(dataFim),
      busca: search || undefined,
      status: status || undefined,
      tipo: tipo || undefined,
      sort: ["dataHoraInicio,desc", "id,asc"],
    },
    {
      query: {
        enabled: hasPeriod,
      },
    },
  );
  const resumo = useGetResumoFinanceiroColaborador(
    colaboradorId,
    { dataInicio, dataFim },
    {
      query: {
        enabled: hasPeriod,
      },
    },
  );

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleStatusChange(value: string) {
    setPage(0);
    setStatus(value as AtendimentoResponseStatusEnumKey | "");
  }

  function handleTipoChange(value: string) {
    setPage(0);
    setTipo(value as AtendimentoResponseTipoEnumKey | "");
  }

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  function handleDataInicioChange(value: string) {
    setPage(0);
    setPeriodo((current) => ({ ...current, dataInicio: value }));
  }

  function handleDataFimChange(value: string) {
    setPage(0);
    setPeriodo((current) => ({ ...current, dataFim: value }));
  }

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
      <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between">
        <div>
          <h2 className="text-xl font-bold text-base-content">Eventos vinculados</h2>
          <p className="mt-2 text-sm text-base-content/65">
            Veja os atendimentos ligados a este colaborador por período, busca e filtros.
          </p>
        </div>

        <div className="flex flex-col gap-3 md:flex-row md:items-end">
          <label className="form-control w-full md:w-44">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Início</span>
            <input
              type="date"
              className="input input-bordered w-full"
              value={dataInicio}
              onChange={(event) => handleDataInicioChange(event.target.value)}
            />
          </label>

          <label className="form-control w-full md:w-44">
            <span className="label-text mb-2 text-sm font-medium text-base-content/70">Fim</span>
            <input
              type="date"
              className="input input-bordered w-full"
              value={dataFim}
              onChange={(event) => handleDataFimChange(event.target.value)}
            />
          </label>
        </div>
      </div>

      <div className="mt-5 flex flex-col gap-3 md:flex-row md:items-end">
        <SearchInput label="Buscar" value={searchInput} onChange={handleSearchChange} placeholder="Digite aluno ou tipo" />

        <label className="form-control w-full md:w-56">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Status</span>
          <select
            className="select select-bordered w-full"
            value={status}
            onChange={(event) => handleStatusChange(event.target.value)}
          >
            {atendimentoStatusOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>

        <label className="form-control w-full md:w-64">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Tipo</span>
          <select
            className="select select-bordered w-full"
            value={tipo}
            onChange={(event) => handleTipoChange(event.target.value)}
          >
            {atendimentoTipoOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="mt-6 grid gap-3 md:grid-cols-4">
        <SummaryCard label="Atendimentos" value={resumo.data?.totalAtendimentos ?? 0} />
        <SummaryCard label="Total" value={brl.format(resumo.data?.valorTotal ?? 0)} />
        <SummaryCard label="Pago" value={brl.format(resumo.data?.valorPago ?? 0)} valueClassName="text-success" />
        <SummaryCard label="Pendente" value={brl.format(resumo.data?.valorPendente ?? 0)} valueClassName="text-warning" />
      </div>

      {resumo.error ? (
        <div className="mt-6">
          <ErrorCard
            title="Não foi possível carregar o resumo financeiro"
            description="A consulta do resumo falhou para o período selecionado."
            error={resumo.error}
          />
        </div>
      ) : null}

      <div className="mt-6">
        {atendimentos.isLoading ? (
          <PageLoading message="Carregando eventos do colaborador..." />
        ) : atendimentos.error ? (
          <ErrorCard
            title="Não foi possível carregar os eventos do colaborador"
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
                    <th>Aluno</th>
                    <th>Data</th>
                    <th>Horário</th>
                    <th>Tipo</th>
                    <th>Status</th>
                    <th className="text-right">Repasse</th>
                    <th>Pago</th>
                  </tr>
                </thead>

                <tbody>
                  {content.map((atendimento) => (
                    <tr
                      key={atendimento.id}
                      className="cursor-pointer transition-colors hover:bg-base-200/70"
                      onClick={() => router.push(`/atendimentos/${atendimento.id}`)}
                    >
                      <td className="font-semibold text-base-content">{atendimento.nomeAluno}</td>
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
                      <td className="text-right">{brl.format(atendimento.repasseColaborador)}</td>
                      <td>
                        <div className="flex items-center gap-2">
                          <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataRepasseColaborador} />
                          <CollaboratorRepasseToggle
                            paid={!!atendimento.dataRepasseColaborador}
                            disabled={toggleRepasseColaborador.isPending || atendimento.status === "CANCELADO"}
                            onToggle={() => toggleRepasseColaborador.mutate({ id: atendimento.id })}
                          />
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
                      <h3 className="mt-1 font-semibold text-base-content">{atendimento.nomeAluno}</h3>
                    </div>

                    <AtendimentoStatusBadge status={atendimento.status} />
                  </div>

                  <div className="mt-4 grid gap-2 rounded-2xl border border-base-200 bg-base-200/35 p-3 text-sm">
                    <p>
                      <span className="font-medium text-base-content">Data:</span>{" "}
                      {formatDateShortYear(atendimento.dataHoraInicio)}
                    </p>
                    <p>
                      <span className="font-medium text-base-content">Horário:</span> {formatTime(atendimento.dataHoraInicio)} -{" "}
                      {formatTime(atendimento.dataHoraFim)}
                    </p>
                    <p>
                      <span className="font-medium text-base-content">Repasse:</span> {brl.format(atendimento.repasseColaborador)}
                    </p>
                  </div>

                  <div className="mt-3 flex flex-wrap items-center gap-2">
                    <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataRepasseColaborador} />
                    <CollaboratorRepasseToggle
                      paid={!!atendimento.dataRepasseColaborador}
                      disabled={toggleRepasseColaborador.isPending || atendimento.status === "CANCELADO"}
                      onToggle={() => toggleRepasseColaborador.mutate({ id: atendimento.id })}
                    />
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
      </div>
    </section>
  );
}
