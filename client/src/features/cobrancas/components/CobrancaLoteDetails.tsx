import { ExternalLink, SquareArrowOutUpRight } from "lucide-react";
import Link from "next/link";
import { DetailField } from "@/components/ui/DetailField";
import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import type { CobrancaLoteDetalheResponse } from "@/lib/api/generated/types/CobrancaLoteDetalheResponse";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type CobrancaLoteDetailsProps = {
  lote: CobrancaLoteDetalheResponse;
};

export function CobrancaLoteDetails({ lote }: Readonly<CobrancaLoteDetailsProps>) {
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
