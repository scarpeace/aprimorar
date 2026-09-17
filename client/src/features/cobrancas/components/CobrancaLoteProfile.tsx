"use client";

import { PaymentStatusBadge } from "@/components/ui/PaymentStatusBadge";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { CobrancaLoteDetails } from "@/features/cobrancas/components/CobrancaLoteDetails";
import { useBuscarLoteDeCobrancaPorId } from "@/lib/api/generated/hooks/cobranças individuais/useBuscarLoteDeCobrancaPorId";
import { brl } from "@/lib/utils/formatter";

type CobrancaLoteProfileProps = {
  loteId: string;
};

export function CobrancaLoteProfile({ loteId }: Readonly<CobrancaLoteProfileProps>) {
  const lote = useBuscarLoteDeCobrancaPorId(loteId);

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Registro de pagamento</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte o pagamento e as cobranças incluídas neste lote.
          </p>
        </div>
      </CardHeader>

      {lote.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : lote.error ? (
        <ErrorCard
          title="Não foi possível carregar o registro de pagamento"
          description="A consulta do lote falhou para o identificador informado."
          error={lote.error}
        />
      ) : !lote.data ? (
        <EmptyCard
          title="Registro de pagamento não encontrado"
          description="A API respondeu sem conteúdo para este lote de cobrança."
        />
      ) : (
        <div className="space-y-6">
          <div className="flex flex-wrap items-center gap-3">
            <PaymentStatusBadge status="PAGO" />
            <span className="text-xl font-bold text-base-content">{brl.format(lote.data.valorTotal)}</span>
            <span className="text-sm text-base-content/60">
              {lote.data.quantidadeCobrancas} cobrança(s)
            </span>
          </div>

          <CobrancaLoteDetails lote={lote.data} />
        </div>
      )}
    </Card>
  );
}
