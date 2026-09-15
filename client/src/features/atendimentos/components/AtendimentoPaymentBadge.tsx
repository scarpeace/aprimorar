import { Check, Clock3 } from "lucide-react";

type AtendimentoPaymentBadgeProps = {
  label: string;
  paidAt?: string | null;
};

export function AtendimentoPaymentBadge({ label, paidAt }: Readonly<AtendimentoPaymentBadgeProps>) {
  const paid = !!paidAt;
  const Icon = paid ? Check : Clock3;

  return (
    <span className={`badge badge-sm gap-1 whitespace-nowrap ${paid ? "badge-success" : "badge-warning"}`}>
      <span>{label}</span>
      <Icon className="h-3 w-3" />
    </span>
  );
}
