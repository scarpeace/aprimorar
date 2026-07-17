"use client";

import { useRouter } from "next/navigation";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { AtendimentoStatusBadge } from "@/components/atendimentos/AtendimentoStatusBadge";
import { AtendimentoTipoBadge } from "@/components/atendimentos/AtendimentoTipoBadge";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { TablePagination } from "@/components/ui/TablePagination";
import type { AtendimentoResponse } from "@/lib/api/generated/types/AtendimentoResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type AtendimentosPagination = {
  totalElements: number;
  currentPage: number;
  totalPages: number;
  hasPrevious: boolean;
  hasNext: boolean;
  onPrevious: () => void;
  onNext: () => void;
};

type AtendimentosResultsProps = {
  atendimentos: AtendimentoResponse[];
  isLoading: boolean;
  error?: unknown;
  pagination: AtendimentosPagination;
};

export function AtendimentosResults({ atendimentos, isLoading, error, pagination }: Readonly<AtendimentosResultsProps>) {
  const router = useRouter();

  if (isLoading) {
    return <PageLoading message="Carregando atendimentos..." />;
  }

  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de atendimentos"
        description="A consulta paginada da API falhou para os filtros selecionados."
        error={error}
      />
    );
  }

  if (atendimentos.length === 0) {
    return <EmptyCard title="Nenhum atendimento encontrado" description="Ajuste os filtros ou selecione outro período." />;
  }

  return (
    <div className="space-y-4">
      <div className="hidden overflow-x-auto rounded-2xl border border-base-300 bg-base-100 md:block">
        <table className="table table-zebra">
          <thead className="bg-base-200/80">
            <tr>
              <th>Aluno</th>
              <th>Colaborador</th>
              <th>Data</th>
              <th>Horário</th>
              <th>Tipo</th>
              <th>Status</th>
              <th className="text-right">Valor</th>
              <th className="text-right">Repasse</th>
              <th>Pagamento</th>
            </tr>
          </thead>

          <tbody>
            {atendimentos.map((atendimento) => (
              <tr
                key={atendimento.id}
                className="cursor-pointer transition-colors hover:bg-base-200/70"
                onClick={() => router.push(`/atendimentos/${atendimento.id}`)}
              >
                <td className="font-semibold text-base-content">{atendimento.nomeAluno}</td>
                <td>{atendimento.nomeColaborador}</td>
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
                <td className="text-right">{brl.format(atendimento.repasseColaborador)}</td>
                <td>
                  <div className="flex flex-col gap-1">
                    <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
                    <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataRepasseColaborador} />
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="grid mt-3 gap-4 md:hidden">
        {atendimentos.map((atendimento) => (
          <article
            key={atendimento.id}
            className="cursor-pointer rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm transition-colors hover:bg-base-200/40"
            onClick={() => router.push(`/atendimentos/${atendimento.id}`)}
          >
            <div className="flex items-start justify-between gap-3">
              <div>
                <AtendimentoTipoBadge tipo={atendimento.tipo} />
                <h3 className="mt-1 font-semibold text-base-content">{atendimento.nomeAluno}</h3>
                <p className="text-sm text-base-content/65">{atendimento.nomeColaborador}</p>
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
              <p>
                <span className="font-medium text-base-content">Repasse:</span> {brl.format(atendimento.repasseColaborador)}
              </p>
            </div>

            <div className="mt-3 flex flex-wrap gap-2">
              <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
              <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataRepasseColaborador} />
            </div>
          </article>
        ))}
      </div>

      <TablePagination
        summary={`${pagination.totalElements} atendimento(s) encontrado(s) • página ${pagination.currentPage + 1}${pagination.totalPages > 0 ? ` de ${pagination.totalPages}` : ""}`}
        hasPrevious={pagination.hasPrevious}
        hasNext={pagination.hasNext}
        onPrevious={pagination.onPrevious}
        onNext={pagination.onNext}
      />
    </div>
  );
}
