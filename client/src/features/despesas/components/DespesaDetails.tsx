import { DetailField } from "@/components/ui/DetailField";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";
import {
  categoriaDespesaLabels,
  formaPagamentoDespesaLabels,
} from "@/lib/constants/despesa-constants";
import { formatDateShortYear } from "@/lib/utils/date-utils";
import { brl, formatDate } from "@/lib/utils/formatter";

type DespesaDetailsProps = {
  despesa: DespesaResponse;
};

const tipoLabels = {
  ENTRADA: "Entrada",
  SAIDA: "Saída",
} as const;

export function DespesaDetails({ despesa }: Readonly<DespesaDetailsProps>) {
  return (
    <div className="mt-6 space-y-6">
      <div className="space-y-4">
        <h3 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados do lançamento</h3>

        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          <DetailField label="Tipo" value={despesa.tipo ? tipoLabels[despesa.tipo] : null} />
          <DetailField
            label="Categoria"
            value={despesa.categoria ? categoriaDespesaLabels[despesa.categoria] : null}
          />
          <DetailField label="Valor" value={despesa.valor == null ? null : brl.format(despesa.valor)} />
          <DetailField
            label="Vencimento"
            value={despesa.dataVencimento ? formatDateShortYear(despesa.dataVencimento) : null}
          />
          <DetailField
            label="Data do pagamento"
            value={despesa.dataPagamento ? formatDateShortYear(despesa.dataPagamento) : null}
          />
          <DetailField
            label="Forma de pagamento"
            value={despesa.formaPagamento ? formaPagamentoDespesaLabels[despesa.formaPagamento] : null}
          />
        </div>
      </div>

      <div className="space-y-4 border-t border-base-300 pt-6">
        <h3 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Informações adicionais</h3>

        <div className="grid gap-5 sm:grid-cols-2">
          <DetailField label="Descrição" value={despesa.descricao} />
          <DetailField label="Criado em" value={formatDate(despesa.createdAt)} />
          <DetailField label="Atualizado em" value={formatDate(despesa.updatedAt)} />
        </div>
      </div>
    </div>
  );
}
