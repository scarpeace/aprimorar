import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import { brl } from "@/lib/utils/formatter";

type AtendimentoFinancialCardProps = {
  title: string;
  value: number;
  status: string;
};

export function AtendimentoFinancialCard({ title, value, status }: Readonly<AtendimentoFinancialCardProps>) {
  return (
    <section className="rounded-2xl border border-base-300 p-4">
      <div className="flex items-center justify-between gap-3">
        <h2 className="font-semibold text-base-content">{title}</h2>
        <PaymentStatusBadge status={status} />
      </div>

      <p className="mt-3 text-xl font-bold text-base-content">{brl.format(value)}</p>
    </section>
  );
}
