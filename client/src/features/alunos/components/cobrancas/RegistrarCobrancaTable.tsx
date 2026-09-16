import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import type { AtendimentoIndividualResponse } from "@/lib/api/generated/types/AtendimentoIndividualResponse";
import { formatDateShortYear } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

type RegistrarCobrancaTableProps = {
  atendimentos: AtendimentoIndividualResponse[];
  selectedIds: number[];
  onToggle: (cobrancaId: number) => void;
};

export function RegistrarCobrancaTable({
  atendimentos,
  selectedIds,
  onToggle,
}: Readonly<RegistrarCobrancaTableProps>) {
  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra table-sm">
        <thead>
          <tr>
            <th>Colaborador</th>
            <th>Data</th>
            <th>Tipo</th>
            <th className="text-right">Valor</th>
            <th className="text-center">Selecionar</th>
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => {
            const cobrancaId = atendimento.cobranca.id;

            return (
              <tr key={atendimento.id}>
                <td className="font-semibold">{atendimento.colaboradorResumo.nome}</td>
                <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                <td>
                  <AtendimentoTipoBadge tipo={atendimento.tipo} />
                </td>
                <td className="text-right">{brl.format(atendimento.cobranca.valor)}</td>
                <td className="text-center">
                  <input
                    type="checkbox"
                    className="checkbox checkbox-sm"
                    checked={selectedIds.includes(cobrancaId)}
                    onChange={() => onToggle(cobrancaId)}
                    aria-label={`Selecionar cobrança do atendimento ${atendimento.id}`}
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
