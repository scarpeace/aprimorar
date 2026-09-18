"use client";

import { ExternalLink, SquareArrowOutUpRight } from "lucide-react";
import Link from "next/link";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { DetailField } from "@/components/ui/DetailField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import { useBuscarLoteDeRepassePorId } from "@/lib/api/generated/hooks/repasses individuais/useBuscarLoteDeRepassePorId";
import type { RepasseLoteDetalheResponse } from "@/lib/api/generated/types/RepasseLoteDetalheResponse";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type RepasseLoteDetailsProps = {
  loteId: string;
};

type RepasseLoteContentProps = {
  lote: RepasseLoteDetalheResponse;
};

function RepasseLoteContent({ lote }: Readonly<RepasseLoteContentProps>) {
  return (
    <div className="space-y-6">
      <section className="space-y-4 rounded-2xl border border-base-300 bg-base-200/25 p-4">
        <h2 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados do repasse</h2>

        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          <DetailField label="ID do lote" value={lote.loteId} />
          <DetailField
            label="Data do repasse"
            value={`${formatDateShortYear(lote.dataRepasse)} às ${formatTime(lote.dataRepasse)}`}
          />
          <DetailField label="Forma de pagamento" value={formaPagamentoLabels[lote.formaPagamento]} />
          <DetailField label="Valor total" value={brl.format(lote.valorTotal)} />
          <DetailField label="Quantidade de repasses" value={String(lote.quantidadeRepasses)} />
          <div className="space-y-1">
            <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Colaborador</p>
            <Link
              className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0 text-sm"
              href={`/colaboradores/${lote.colaboradorId}`}
            >
              Ver colaborador
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
          <h2 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Repasses incluídos</h2>
          <p className="mt-1 text-sm text-base-content/60">
            Lançamentos individuais que compõem este pagamento.
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="table table-zebra">
            <thead>
              <tr>
                <th>Repasse</th>
                <th>Atendimento</th>
                <th className="text-right">Valor</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {lote.repasses.map((repasse) => (
                <tr key={repasse.id}>
                  <td>#{repasse.id}</td>
                  <td>
                    <Link
                      className="btn btn-link btn-sm h-auto min-h-0 justify-start gap-1 p-0"
                      href={`/atendimentos/${repasse.atendimentoId}`}
                    >
                      Atendimento #{repasse.atendimentoId}
                      <SquareArrowOutUpRight size={14} aria-hidden="true" />
                    </Link>
                  </td>
                  <td className="text-right font-semibold">{brl.format(repasse.valor)}</td>
                  <td>
                    <PaymentStatusBadge status={repasse.status} />
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

export function RepasseLoteDetails({ loteId }: Readonly<RepasseLoteDetailsProps>) {
  const lote = useBuscarLoteDeRepassePorId(loteId);

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Registro de repasse</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte o pagamento e os repasses incluídos neste lote.
          </p>
        </div>
      </CardHeader>

      {lote.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : lote.error ? (
        <ErrorCard
          title="Não foi possível carregar o registro de repasse"
          description="A consulta do lote falhou para o identificador informado."
          error={lote.error}
        />
      ) : !lote.data ? (
        <EmptyCard
          title="Registro de repasse não encontrado"
          description="A API respondeu sem conteúdo para este lote de repasse."
        />
      ) : (
        <div className="space-y-6">
          <div className="flex flex-wrap items-center gap-3">
            <PaymentStatusBadge status="PAGO" />
            <span className="text-xl font-bold text-base-content">{brl.format(lote.data.valorTotal)}</span>
            <span className="text-sm text-base-content/60">
              {lote.data.quantidadeRepasses} repasse(s)
            </span>
          </div>

          <RepasseLoteContent lote={lote.data} />
        </div>
      )}
    </Card>
  );
}
