import { AtendimentoPaymentBadge } from "@/features/atendimentos/components/AtendimentoPaymentBadge";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type ColaboradorAtendimentoCardProps = {
  atendimento: AtendimentoIndividualResponse;
  onClick: () => void;
};

export function ColaboradorAtendimentoCard({
  atendimento,
  onClick,
}: Readonly<ColaboradorAtendimentoCardProps>) {
  return (
    <button
      type="button"
      className="card w-full border border-base-300 bg-base-100 text-left shadow-sm transition-colors hover:bg-base-200/40"
      onClick={onClick}
    >
      <div className="card-body gap-4 p-4">
        <div>
          <AtendimentoTipoBadge tipo={atendimento.tipo} />
          <h3 className="mt-2 font-semibold text-base-content">{atendimento.alunoResumo.nome ?? "Aluno não informado"}</h3>
        </div>

        <div className="grid gap-2 text-sm">
          <p>
            <span className="font-medium text-base-content">Data:</span>{" "}
            {formatDateShortYear(atendimento.dataHoraInicio)}
          </p>
          <p>
            <span className="font-medium text-base-content">Horário:</span>{" "}
            {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
          </p>
          <p>
            <span className="font-medium text-base-content">Repasse:</span>{" "}
            {brl.format(atendimento.repasse.valor ?? 0)}
          </p>
        </div>

        <div className="card-actions">
          <AtendimentoPaymentBadge label="Repasse" paidAt={atendimento.repasse.dataRepasse} />
        </div>
      </div>
    </button>
  );
}
