import type { AtendimentoIndividualResponseTipoEnumKey } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { atendimentoTipoBadgeClass, tipoAtendimentoLabels } from "@/lib/constants/atendimento-constants";

type AtendimentoTipoBadgeProps = {
  tipo: AtendimentoIndividualResponseTipoEnumKey;
};

export function AtendimentoTipoBadge({ tipo }: Readonly<AtendimentoTipoBadgeProps>) {
  return (
    <span className={`badge badge-sm whitespace-nowrap ${atendimentoTipoBadgeClass[tipo] ?? "badge-ghost"}`}>
      {tipoAtendimentoLabels[tipo] ?? tipo}
    </span>
  );
}
