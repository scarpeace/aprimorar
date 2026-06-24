import { MiniCard } from "@/components/Ui/MiniCard.tsx";
import { SummaryItem } from "@/components/Ui/SummaryItem.tsx";
import type { TransacaoResponseDTO } from "@/kubb";
import { formatDateShortYear, formatTime } from "@/utils/date-utils";
import { brl } from "@/utils/formatter";
import { CreditCard, Receipt, Tag, UserRound } from "lucide-react";

type TransacaoInfoSectionProps = {
  transacao: TransacaoResponseDTO;
};

function formatEnumLabel(value?: string) {
  if (!value) {
    return "-";
  }

  return value
    .toLowerCase()
    .split("_")
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join(" ");
}

function formatDateTime(value?: string) {
  if (!value) {
    return "-";
  }

  return `${formatDateShortYear(value)} às ${formatTime(value)}`;
}

export function TransacaoInfoSection({ transacao }: Readonly<TransacaoInfoSectionProps>) {
  return (
    <div className="grid gap-4">
      <div className="grid gap-4 lg:grid-cols-3">
        <MiniCard label="Pagador" icon={UserRound}>
          <div className="text-xl font-semibold text-base-content">
            {transacao.nomePagador || "-"}
          </div>
        </MiniCard>

        <MiniCard label="Recebedor" icon={UserRound}>
          <div className="text-xl font-semibold text-base-content">
            {transacao.nomeRecebedor || "-"}
          </div>
        </MiniCard>

        <MiniCard label="Valor" icon={Receipt}>
          <div className="text-xl font-bold text-base-content">
            {brl.format(transacao.valor ?? 0)}
          </div>
        </MiniCard>
      </div>

      <div className="grid gap-4 border border-base-300 bg-base-200/30 p-4 rounded-xl md:grid-cols-2 xl:grid-cols-3">
        <SummaryItem label="Tipo" value={formatEnumLabel(transacao.tipo)} />
        <SummaryItem label="Categoria" value={formatEnumLabel(transacao.categoria)} />
        <SummaryItem label="Status" value={formatEnumLabel(transacao.status)} />
        <SummaryItem label="Forma de pagamento" value={formatEnumLabel(transacao.formaPagamento)} />
        <SummaryItem label="Criada em" value={formatDateTime(transacao.createdAt)} className="xl:col-span-1" />
        <SummaryItem label="Efetivada em" value={formatDateTime(transacao.dataEfetivada)} />
      </div>

      <div className="grid gap-4 lg:grid-cols-3">
        <MiniCard label="Referência" icon={Tag}>
          <div className="text-sm text-base-content/80 break-all">
            Transação #{transacao.id ?? "-"}
          </div>
        </MiniCard>

        <MiniCard label="Identificador do pagador" icon={UserRound}>
          <div className="text-sm text-base-content/80 break-all">
            {transacao.pagadorId || "-"}
          </div>
        </MiniCard>

        <MiniCard label="Identificador do recebedor" icon={CreditCard}>
          <div className="text-sm text-base-content/80 break-all">
            {transacao.recebedorId || "-"}
          </div>
        </MiniCard>
      </div>
    </div>
  );
}
