import { Tooltip } from "@/components/ui/Tooltip";

type PaymentStatusIndicatorProps = {
  status?: string;
  paidAt?: string | null;
  label?: string;
};

export function PaymentStatusIndicator({
  status,
  paidAt,
  label,
}: Readonly<PaymentStatusIndicatorProps>) {
  const paid = status === "PAGO" || (!status && !!paidAt);
  const statusLabel = paid ? "Pago" : "Pendente";
  const colorClass = paid ? "status-success" : "status-warning";
  const animationClass = paid ? "" : "animate-bounce";
  const tooltipLabel = label ? `${label}: ${statusLabel}` : statusLabel;

  return (
    <Tooltip content={tooltipLabel}>
      <span className="inline-flex items-center gap-2">
        <span
          className={`status ${colorClass} ${animationClass}`.trim()}
          role="status"
          aria-label={tooltipLabel}
        />
        {label ? <span className="text-sm text-base-content/70">{label}</span> : null}
      </span>
    </Tooltip>
  );
}
