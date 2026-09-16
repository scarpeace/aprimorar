import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type AlunoAtendimentosTableProps = {
  atendimentos: AtendimentoIndividualResponse[];
  onOpen: (atendimentoId: number) => void;
};

export function AlunoAtendimentosTable({ atendimentos, onOpen }: Readonly<AlunoAtendimentosTableProps>) {
  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra">
        <thead>
          <tr>
            <th>Colaborador</th>
            <th>Data</th>
            <th>Horário</th>
            <th>Tipo</th>
            <th className="text-right">Cobrança</th>
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => (
            <tr
              key={atendimento.id}
              className="cursor-pointer hover:bg-base-200/70"
              onClick={() => onOpen(atendimento.id)}
            >
              <td className="font-semibold">{atendimento.colaboradorResumo.nome ?? "Não informado"}</td>
              <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
              <td>
                {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
              </td>
              <td>
                <AtendimentoTipoBadge tipo={atendimento.tipo} />
              </td>
              <td className="w-10">
                <div className="flex items-center justify-between gap-3">
                  <PaymentStatusIndicator status={atendimento.cobranca.status} />
                  <span>{brl.format(atendimento.cobranca.valor ?? 0)}</span>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
