import type { CobrancaResponse } from "@/lib/api/generated/types/CobrancaResponse";
import { brl } from "@/lib/utils/formatter";

type CobrancasEmAbertoTableProps = {
  cobrancas: CobrancaResponse[];
  selectedIds: number[];
  onToggle: (cobrancaId: number) => void;
};

export function CobrancasEmAbertoTable({
  cobrancas,
  selectedIds,
  onToggle,
}: Readonly<CobrancasEmAbertoTableProps>) {
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
