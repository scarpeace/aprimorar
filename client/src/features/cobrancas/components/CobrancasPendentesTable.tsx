import type { CobrancaAlunoResponse } from "@/lib/api/generated/types/CobrancaAlunoResponse";
import { brl } from "@/lib/utils/formatter";

type CobrancasPendentesTableProps = {
  cobrancas: CobrancaAlunoResponse[];
  selectedIds: number[];
  onToggle: (cobrancaId: number) => void;
};

export function CobrancasPendentesTable({
  cobrancas,
  selectedIds,
  onToggle,
}: Readonly<CobrancasPendentesTableProps>) {
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
          {cobrancas.map((cobranca) => (
            <tr key={cobranca.id}>
              <td className="font-semibold">#{cobranca.atendimentoId}</td>
              <td className="text-right">{brl.format(cobranca.valor)}</td>
              <td className="text-center">
                <input
                  type="checkbox"
                  className="checkbox checkbox-sm"
                  checked={selectedIds.includes(cobranca.id)}
                  onChange={() => onToggle(cobranca.id)}
                  aria-label={`Selecionar cobrança do atendimento ${cobranca.atendimentoId}`}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
