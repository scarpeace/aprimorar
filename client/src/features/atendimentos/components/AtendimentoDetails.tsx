"use client";

import Link from "next/link";
import { SquareArrowOutUpRight } from "lucide-react";

import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { DetailField } from "@/components/ui/DetailField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { AtendimentoFinancialCard } from "@/features/atendimentos/components/AtendimentoFinancialCard";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import { useBuscarAtendimentoIndividualPorId } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentoIndividualPorId";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { formatDate } from "@/lib/utils/formatter";

type AtendimentoDetailsProps = {
  atendimentoId: string;
};


export function AtendimentoDetails({ atendimentoId }: Readonly<AtendimentoDetailsProps>) {
  const atendimento = useBuscarAtendimentoIndividualPorId(Number(atendimentoId));

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Dados do atendimento</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os participantes, horários e valores deste atendimento.
          </p>
        </div>
      </CardHeader>

      {atendimento.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : atendimento.error ? (
        <ErrorCard
          title="Não foi possível carregar o atendimento"
          description="A consulta do detalhe falhou para o identificador informado."
          error={atendimento.error}
        />
      ) : !atendimento.data ? (
        <EmptyCard title="Atendimento não encontrado" description="A API respondeu sem conteúdo para este atendimento." />
      ) : (
        <div className="space-y-6">
          <div className="flex flex-wrap items-center gap-2">
            <AtendimentoTipoBadge tipo={atendimento.data.tipo} />
            <span className="text-sm text-base-content/60">#{atendimento.data.id}</span>
          </div>

          <div className="grid gap-4 rounded-2xl border border-base-300 bg-base-200/25 p-4 md:grid-cols-2">
            <div className="space-y-1">
              <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Aluno</p>
              <Link
                className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0 text-sm"
                href={`/alunos/${atendimento.data.alunoResumo.id}`}
              >
                {atendimento.data.alunoResumo.nome}
                <SquareArrowOutUpRight size={14} aria-hidden="true" />
              </Link>
            </div>
            <div className="space-y-1">
              <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Colaborador</p>
              <Link
                className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0 text-sm"
                href={`/colaboradores/${atendimento.data.colaboradorResumo.id}`}
              >
                {atendimento.data.colaboradorResumo.nome}
                <SquareArrowOutUpRight size={14} aria-hidden="true" />
              </Link>
            </div>
            <DetailField label="Data" value={formatDateShortYear(atendimento.data.dataHoraInicio)} />
            <DetailField
              label="Horário"
              value={`${formatTime(atendimento.data.dataHoraInicio)} - ${formatTime(atendimento.data.dataHoraFim)}`}
            />
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <AtendimentoFinancialCard
              title="Cobrança"
              value={atendimento.data.cobranca.valor}
              status={atendimento.data.cobranca.status}
              loteId={atendimento.data.cobranca.loteId}
              paymentDate={atendimento.data.cobranca.dataPagamento}
              formaPagamento={atendimento.data.cobranca.formaPagamento}
              paymentRecordHref={
                atendimento.data.cobranca.loteId
                  ? `/financeiro/cobrancas/lotes/${atendimento.data.cobranca.loteId}`
                  : undefined
              }
            />
            <AtendimentoFinancialCard
              title="Repasse"
              value={atendimento.data.repasse.valor}
              status={atendimento.data.repasse.status}
              loteId={atendimento.data.repasse.loteId}
              paymentDate={atendimento.data.repasse.dataRepasse}
              formaPagamento={atendimento.data.repasse.formaPagamento}
            />
          </div>

          <div className="grid gap-4 border-t border-base-300 pt-4 md:grid-cols-2">
            <DetailField label="Criado em" value={formatDate(atendimento.data.createdAt)} />
            <DetailField label="Atualizado em" value={formatDate(atendimento.data.updatedAt)} />
          </div>
        </div>
      )}
    </Card>
  );
}
