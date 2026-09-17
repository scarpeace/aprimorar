import { CircleCheck, Clock3, TriangleAlert } from "lucide-react";
import type { DespesaResponseStatusEnumKey } from "@/lib/api/generated/types/DespesaResponse";

type DespesaStatusBadgeProps = {
  status?: DespesaResponseStatusEnumKey;
};

const statusConfig = {
  PENDENTE: {
    label: "Pendente",
    className: "badge-warning",
    icon: Clock3,
  },
  PAGA: {
    label: "Paga",
    className: "badge-success",
    icon: CircleCheck,
  },
  ATRASADA: {
    label: "Atrasada",
    className: "badge-error",
    icon: TriangleAlert,
  },
} as const;

export function DespesaStatusBadge({ status }: Readonly<DespesaStatusBadgeProps>) {
  if (!status) {
    return <span className="badge badge-ghost gap-1">Não informado</span>;
  }

  const config = statusConfig[status];
  const Icon = config.icon;

  return (
    <span className={`badge badge-sm gap-1 ${config.className}`}>
      <Icon size={13} aria-hidden="true" />
      {config.label}
    </span>
  );
}
