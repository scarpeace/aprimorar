import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type RegistrarRepasseTableProps = {
  atendimentos: AtendimentoIndividualResponse[];
  selectedIds: number[];
  onToggle: (repasseId: number) => void;
};

export function RegistrarRepasseTable({
  atendimentos,
  selectedIds,
  onToggle,
}: Readonly<RegistrarRepasseTableProps>) {
  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra table-sm">
        <thead>
          <tr>
            <th>Aluno</th>
            <th>Data</th>
            <th>Tipo</th>
            <th className="text-right">Valor</th>
            <th className="text-center">Selecionar</th>
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => {
            const repasseId = atendimento.repasse.id;

            return (
              <tr key={atendimento.id}>
                <td className="font-semibold">{atendimento.alunoResumo.nome}</td>
                <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                <td>
                  <AtendimentoTipoBadge tipo={atendimento.tipo} />
                </td>
                <td className="text-right">{brl.format(atendimento.repasse.valor)}</td>
                <td className="text-center">
                  <input
                    type="checkbox"
                    className="checkbox checkbox-sm"
                    checked={selectedIds.includes(repasseId)}
                    onChange={() => onToggle(repasseId)}
                    aria-label={`Selecionar repasse do atendimento ${atendimento.id}`}
                  />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
