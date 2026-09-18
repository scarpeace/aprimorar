import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type AtendimentoCardProps = {
  atendimento: AtendimentoIndividualResponse;
  onOpen: () => void;
};

export function AtendimentoCard({ atendimento, onOpen }: Readonly<AtendimentoCardProps>) {
  return (
    <button
      type="button"
      className="card w-full border border-base-300 bg-base-100 text-left shadow-sm transition-colors hover:bg-base-200/40"
      onClick={onOpen}
    >
      <div className="card-body gap-4 p-4">
        <div>
          <AtendimentoTipoBadge tipo={atendimento.tipo} />
          <h3 className="mt-2 font-semibold text-base-content">{atendimento.alunoResumo.nome}</h3>
          <p className="text-sm text-base-content/65">{atendimento.colaboradorResumo.nome}</p>
        </div>

        <div className="grid gap-2 text-sm">
          <p>
            <span className="font-medium text-base-content">Data:</span> {formatDateShortYear(atendimento.dataHoraInicio)}
          </p>
          <p>
            <span className="font-medium text-base-content">Horário:</span>{" "}
            {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
          </p>
          <p className="flex items-center gap-2">
            <span className="font-medium text-base-content">Cobrança:</span>
            <PaymentStatusIndicator status={atendimento.cobranca.status} />
            <span>{brl.format(atendimento.cobranca.valor)}</span>
          </p>
          <p className="flex items-center gap-2">
            <span className="font-medium text-base-content">Repasse:</span>
            <PaymentStatusIndicator status={atendimento.repasse.status} />
            <span>{brl.format(atendimento.repasse.valor)}</span>
          </p>
        </div>
      </div>
    </button>
  );
}
