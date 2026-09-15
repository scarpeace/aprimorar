import type { AtendimentoResponseTipoEnumKey } from "@/lib/api/generated/types/AtendimentoResponse";
import { atendimentoTipoBadgeClass, tipoAtendimentoLabels } from "@/lib/constants/atendimento-constants";

type AtendimentoTipoBadgeProps = {
  tipo: AtendimentoResponseTipoEnumKey;
};

export function AtendimentoTipoBadge({ tipo }: Readonly<AtendimentoTipoBadgeProps>) {
  return (
    <span className={`badge badge-sm whitespace-nowrap ${atendimentoTipoBadgeClass[tipo] ?? "badge-ghost"}`}>
      {tipoAtendimentoLabels[tipo] ?? tipo}
    </span>
  );
}
