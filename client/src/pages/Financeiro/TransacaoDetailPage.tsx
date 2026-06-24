import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { TransacaoInfoSection } from "@/components/Financeiro/TransacaoInfoSection.tsx";
import { Badge } from "@/components/Ui/Badge.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingCard } from "@/components/Ui/LoadingCard.tsx";
import { useGetTransacaoById, transacaoResponseDTOTipoEnum } from "@/kubb";
import { BanknoteArrowDown } from "lucide-react";
import { useParams } from "react-router-dom";

export function TransacaoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const transacaoId = Number(id);

  const transacaoQuery = useGetTransacaoById(transacaoId);

  const isSaida = transacaoQuery.data?.tipo === transacaoResponseDTOTipoEnum.SAIDA;

  const headerProps = {
    description: isSaida
      ? "Veja as informações registradas para o pagamento."
      : "Veja as informações registradas para o recebimento.",
    title: isSaida ? "Detalhes da despesa" : "Detalhes do recebimento",
    Icon: BanknoteArrowDown,
    iconBg: "warning",
  } as const;

  if (transacaoQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes da transação"
          error={transacaoQuery.error}
        />
      </PageLayout>
    );
  }

  if (transacaoQuery.isPending || !transacaoQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados da transação" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_300ms_ease-out_both]">
        <div className="mb-4 flex flex-wrap items-center gap-2">
          <Badge variant={isSaida ? "warning" : "info"} className="px-3 py-1 font-semibold">
            {transacaoQuery.data.tipo}
          </Badge>
          <Badge variant="neutral" className="px-3 py-1 font-semibold">
            {transacaoQuery.data.status}
          </Badge>
          <Badge variant="ghost" className="px-3 py-1 font-semibold">
            {transacaoQuery.data.categoria}
          </Badge>
        </div>

        <TransacaoInfoSection transacao={transacaoQuery.data} />
      </section>
    </PageLayout>
  );
}
