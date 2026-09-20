"use client";

import { ExternalLink, SquareArrowOutUpRight } from "lucide-react";
import Link from "next/link";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { DetailField } from "@/components/ui/DetailField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import { useBuscarLoteDeCobrancaPorId } from "@/lib/api/generated/hooks/cobranças de alunos/useBuscarLoteDeCobrancaPorId";
import type { CobrancaLoteDetalheResponse } from "@/lib/api/generated/types/CobrancaLoteDetalheResponse";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";


//TODO: isso aqui não tá bom. cabe refatoração nesse código
type CobrancaLoteDetailsProps = {
  loteId: string;
};

type CobrancaLoteContentProps = {
  lote: CobrancaLoteDetalheResponse;
};

function CobrancaLoteContent({ lote }: Readonly<CobrancaLoteContentProps>) {
  return (
    <div className="space-y-6">
      <section className="space-y-4 rounded-2xl border border-base-300 bg-base-200/25 p-4">
        <h2 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados do pagamento</h2>

        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          <DetailField label="ID do lote" value={lote.loteId} />
          <DetailField
            label="Data do pagamento"
            value={`${formatDateShortYear(lote.dataPagamento)} às ${formatTime(lote.dataPagamento)}`}
          />
          <DetailField label="Forma de pagamento" value={formaPagamentoLabels[lote.formaPagamento]} />
          <DetailField label="Valor total" value={brl.format(lote.valorTotal)} />
          <DetailField label="Quantidade de cobranças" value={String(lote.quantidadeCobrancas)} />
          <div className="space-y-1">
            <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Aluno</p>
            <Link
              className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0 text-sm"
              href={`/alunos/${lote.alunoId}`}
            >
              Ver aluno
              <SquareArrowOutUpRight size={14} aria-hidden="true" />
            </Link>
          </div>
        </div>

        {lote.comprovanteUrl ? (
          <a
            className="btn btn-outline btn-sm"
            href={lote.comprovanteUrl}
            target="_blank"
            rel="noreferrer"
          >
            <ExternalLink size={16} aria-hidden="true" />
            Abrir comprovante
          </a>
        ) : null}
      </section>

      <section className="space-y-4 border-t border-base-300 pt-6">
        <div>
          <h2 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Cobranças incluídas</h2>
          <p className="mt-1 text-sm text-base-content/60">
            Lançamentos individuais que compõem este pagamento.
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="table table-zebra">
            <thead>
              <tr>
                <th>Cobrança</th>
                <th>Atendimento</th>
                <th className="text-right">Valor</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {lote.cobrancas.map((cobranca) => (
                <tr key={cobranca.id}>
                  <td>#{cobranca.id}</td>
                  <td>
                    <Link
                      className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0"
                      href={`/atendimentos/${cobranca.atendimentoId}`}
                    >
                      Atendimento #{cobranca.atendimentoId}
                      <SquareArrowOutUpRight size={14} aria-hidden="true" />
                    </Link>
                  </td>
                  <td className="text-right font-semibold">{brl.format(cobranca.valor)}</td>
                  <td>
                    <PaymentStatusBadge status={cobranca.status} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}

export function CobrancaLoteDetails({ loteId }: Readonly<CobrancaLoteDetailsProps>) {
  const lote = useBuscarLoteDeCobrancaPorId(loteId);

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Registro de pagamento</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte o pagamento e as cobranças incluídas neste lote.
          </p>
        </div>
      </CardHeader>

      {lote.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : lote.error ? (
        <ErrorCard
          title="Não foi possível carregar o registro de pagamento"
          description="A consulta do lote falhou para o identificador informado."
          error={lote.error}
        />
      ) : !lote.data ? (
        <EmptyCard
          title="Registro de pagamento não encontrado"
          description="A API respondeu sem conteúdo para este lote de cobrança."
        />
      ) : (
        <div className="space-y-6">
          <div className="flex flex-wrap items-center gap-3">
            <PaymentStatusBadge status="PAGO" />
            <span className="text-xl font-bold text-base-content">{brl.format(lote.data.valorTotal)}</span>
            <span className="text-sm text-base-content/60">
              {lote.data.quantidadeCobrancas} cobrança(s)
            </span>
          </div>

          <CobrancaLoteContent lote={lote.data} />
        </div>
      )}
    </Card>
  );
}
