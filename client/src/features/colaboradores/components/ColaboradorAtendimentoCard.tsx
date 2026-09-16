import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type ColaboradorAtendimentoCardProps = {
  atendimento: AtendimentoIndividualResponse;
  selectionMode: boolean;
  selected: boolean;
  onOpen: () => void;
  onToggle: () => void;
};

export function ColaboradorAtendimentoCard({
  atendimento,
  selectionMode,
  selected,
  onOpen,
  onToggle,
}: Readonly<ColaboradorAtendimentoCardProps>) {
  const canSelect = atendimento.repasse.status === "PENDENTE" && atendimento.repasse.id !== undefined;

  function handleClick() {
    if (!selectionMode) {
      onOpen();
      return;
    }

    if (canSelect) {
      onToggle();
    }
  }

  return (
    <button
      type="button"
      className={`card w-full border bg-base-100 text-left shadow-sm transition-colors ${
        selected ? "border-primary bg-primary/5" : "border-base-300 hover:bg-base-200/40"
      }`}
      onClick={handleClick}
    >
      <div className="card-body gap-4 p-4">
        <div className="flex items-start gap-3">
          {selectionMode ? (
            <input
              type="checkbox"
              className="checkbox checkbox-sm mt-1"
              checked={selected}
              disabled={!canSelect}
              onClick={(event) => event.stopPropagation()}
              onChange={onToggle}
              aria-label={`Selecionar repasse do atendimento ${atendimento.id}`}
            />
          ) : null}

          <div className="min-w-0 flex-1">
            <AtendimentoTipoBadge tipo={atendimento.tipo} />
            <h3 className="mt-2 font-semibold text-base-content">
              {atendimento.alunoResumo.nome ?? "Aluno não informado"}
            </h3>
          </div>
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
          <p className="flex items-center gap-2">
            <span className="font-medium text-base-content">Repasse:</span>
            <PaymentStatusIndicator status={atendimento.repasse.status} />
            <span>{brl.format(atendimento.repasse.valor ?? 0)}</span>
          </p>
        </div>
      </div>
    </button>
  );
}
