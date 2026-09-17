import { CheckCircle2, Clock3 } from "lucide-react";

type PaymentStatusBadgeProps = {
  status: string;
};

export function PaymentStatusBadge({ status }: Readonly<PaymentStatusBadgeProps>) {
  const paid = status === "PAGO";
  const Icon = paid ? CheckCircle2 : Clock3;
  const label = paid ? "Pago" : "Pendente";
  const variant = paid ? "success" : "warning";

  return (
    <span className={`badge badge-${variant} badge-soft badge-sm gap-1`}>
      <Icon size={14} aria-hidden="true" />
      {label}
    </span>
  );
}
