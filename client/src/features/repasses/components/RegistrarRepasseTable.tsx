import type { RepasseIndividualResponse } from "@/lib/api/generated/types/RepasseIndividualResponse";
import { brl } from "@/lib/utils/formatter";

type RegistrarRepasseTableProps = {
  repasses: RepasseIndividualResponse[];
  selectedIds: number[];
  onToggle: (repasseId: number) => void;
};

export function RegistrarRepasseTable({
  repasses,
  selectedIds,
  onToggle,
}: Readonly<RegistrarRepasseTableProps>) {
  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra table-sm">
        <thead>
          <tr>
            <th>Atendimento</th>
            <th className="text-right">Valor</th>
            <th className="text-center">Selecionar</th>
          </tr>
        </thead>

        <tbody>
          {repasses.map((repasse) => (
            <tr key={repasse.id}>
              <td className="font-semibold">#{repasse.atendimentoId}</td>
              <td className="text-right">{brl.format(repasse.valor)}</td>
              <td className="text-center">
                <input
                  type="checkbox"
                  className="checkbox checkbox-sm"
                  checked={selectedIds.includes(repasse.id)}
                  onChange={() => onToggle(repasse.id)}
                  aria-label={`Selecionar repasse do atendimento ${repasse.atendimentoId}`}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
