import type { AtendimentoResponseStatusEnumKey } from "@/lib/api/generated/types/AtendimentoResponse";
import { statusAtendimentoLabels } from "@/lib/constants/atendimento-constants";

type AtendimentoStatusBadgeProps = {
  status: AtendimentoResponseStatusEnumKey;
};

export function AtendimentoStatusBadge({ status }: Readonly<AtendimentoStatusBadgeProps>) {
  const variant = status === "CONCLUIDO" ? "success" : status === "CANCELADO" ? "warning" : "primary";

  return <span className={`badge badge-soft badge-sm badge-${variant}`}>{statusAtendimentoLabels[status] ?? status}</span>;
}
