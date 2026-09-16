import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type ColaboradorAtendimentosTableProps = {
  atendimentos: AtendimentoIndividualResponse[];
  selectionMode: boolean;
  selectedIds: number[];
  onToggle: (repasseId: number) => void;
  onTogglePage: (repasseIds: number[]) => void;
  onOpen: (atendimentoId: number) => void;
};

export function ColaboradorAtendimentosTable({
  atendimentos,
  selectionMode,
  selectedIds,
  onToggle,
  onTogglePage,
  onOpen,
}: Readonly<ColaboradorAtendimentosTableProps>) {
  const selectablePageIds = atendimentos.flatMap((atendimento) => {
    const repasseId = atendimento.repasse.id;
    const isPending = atendimento.repasse.status === "PENDENTE";

    return isPending && repasseId !== undefined ? [repasseId] : [];
  });
  const allPageSelected =
    selectablePageIds.length > 0 && selectablePageIds.every((repasseId) => selectedIds.includes(repasseId));

  return (
    <div className="hidden overflow-x-auto md:block">
      <table className="table table-zebra">
        <thead>
          <tr>
            <th>Aluno</th>
            <th>Data</th>
            <th>Horário</th>
            <th>Tipo</th>
            <th className="text-right">Repasse</th>
            {selectionMode ? (
              <th className="text-center">
                <input
                  type="checkbox"
                  className="checkbox checkbox-sm"
                  checked={allPageSelected}
                  disabled={selectablePageIds.length === 0}
                  onChange={() => onTogglePage(selectablePageIds)}
                  aria-label="Selecionar repasses pendentes desta página"
                />
              </th>
            ) : null}
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => {
            const repasseId = atendimento.repasse.id;
            const canSelect = atendimento.repasse.status === "PENDENTE" && repasseId !== undefined;
            const selected = repasseId !== undefined && selectedIds.includes(repasseId);

            function handleRowClick() {
              if (!selectionMode) {
                onOpen(atendimento.id);
                return;
              }

              if (canSelect && repasseId !== undefined) {
                onToggle(repasseId);
              }
            }

            return (
              <tr
                key={atendimento.id}
                className={`cursor-pointer ${selected ? "bg-primary/5" : "hover:bg-base-200/70"}`}
                onClick={handleRowClick}
              >
                <td className="font-semibold">{atendimento.alunoResumo.nome ?? "Não informado"}</td>
                <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                <td>
                  {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
                </td>
                <td>
                  <AtendimentoTipoBadge tipo={atendimento.tipo} />
                </td>
                <td className="w-10">
                  <div className="flex items-center justify-between gap-3">
                    <PaymentStatusIndicator status={atendimento.repasse.status} />
                    <span>{brl.format(atendimento.repasse.valor ?? 0)}</span>
                  </div>
                </td>
                {selectionMode ? (
                  <td className="text-center">
                    <input
                      type="checkbox"
                      className="checkbox checkbox-sm"
                      checked={selected}
                      disabled={!canSelect}
                      onClick={(event) => event.stopPropagation()}
                      onChange={() => {
                        if (repasseId !== undefined) {
                          onToggle(repasseId);
                        }
                      }}
                      aria-label={`Selecionar repasse do atendimento ${atendimento.id}`}
                    />
                  </td>
                ) : null}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
