import { DetailField } from "@/components/ui/DetailField";
import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { brl, formatDate } from "@/lib/utils/formatter";

type AtendimentoFinancialCardProps = {
  title: string;
  value: number;
  status: string;
  loteId?: string | null;
  paymentDate?: string | null;
  formaPagamento?: string | null;
};

export function AtendimentoFinancialCard({
  title,
  value,
  status,
  loteId,
  paymentDate,
  formaPagamento,
}: Readonly<AtendimentoFinancialCardProps>) {
  const isPaid = status === "PAGO";

  return (
    <section className="rounded-2xl border border-base-300 p-4">
      <div className="flex items-center justify-between gap-3">
        <h2 className="font-semibold text-base-content">{title}</h2>
        <PaymentStatusBadge status={status} />
      </div>

      <p className="mt-3 text-xl font-bold text-base-content">{brl.format(value)}</p>

      <div className="mt-4 border-t border-base-300 pt-4">
        <h3 className="text-xs font-semibold uppercase tracking-wider text-base-content/50">
          Registro do pagamento
        </h3>

        {isPaid ? (
          <div className="mt-3 grid gap-3">
            <DetailField label="Lote" value={loteId} />
            <DetailField label="Data" value={paymentDate ? formatDate(paymentDate) : null} />
            <DetailField
              label="Forma de pagamento"
              value={formaPagamento ? formaPagamentoLabels[formaPagamento] : null}
            />
          </div>
        ) : (
          <p className="mt-3 text-sm text-base-content/60">Pagamento ainda não registrado.</p>
        )}
      </div>
    </section>
  );
}
