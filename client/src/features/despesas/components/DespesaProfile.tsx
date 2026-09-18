"use client";

import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { DespesaDetails } from "@/features/despesas/components/DespesaDetails";
import { DespesaPagamentoButton } from "@/features/despesas/components/DespesaPagamentoButton";
import { DespesaStatusBadge } from "@/features/despesas/components/DespesaStatusBadge";
import { EditarDespesaButton } from "@/features/despesas/components/EditarDespesaButton";
import { ExcluirDespesaButton } from "@/features/despesas/components/ExcluirDespesaButton";
import { useGetDespesaById } from "@/lib/api/generated/hooks/despesa/useGetDespesaById";
import { brl } from "@/lib/utils/formatter";

type DespesaProfileProps = {
  despesaId: string;
};

export function DespesaProfile({ despesaId }: Readonly<DespesaProfileProps>) {
  const despesa = useGetDespesaById(Number(despesaId));

  return (
    <Card className="h-full">
      <CardHeader>
        <div>
          <CardTitle>Despesa</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte e gerencie os dados deste lançamento financeiro.
          </p>
        </div>

        {despesa.data ? (
          <CardActions>
            <EditarDespesaButton despesa={despesa.data} />
            <DespesaPagamentoButton despesa={despesa.data} />
            <ExcluirDespesaButton despesa={despesa.data} />
          </CardActions>
        ) : null}
      </CardHeader>

      {despesa.isLoading ? (
        <LoadingSkeleton className="h-56 w-full" />
      ) : despesa.error ? (
        <ErrorCard
          title="Não foi possível carregar a despesa"
          description="A consulta do detalhe falhou para o identificador informado."
          error={despesa.error}
        />
      ) : despesa.data ? (
        <>
          <div className="mt-3 flex flex-wrap items-center gap-3">
            <DespesaStatusBadge status={despesa.data.status} />
            <h2 className="text-xl font-bold text-base-content">{despesa.data.titulo}</h2>
            <span className="text-sm font-semibold text-base-content/65">
              {brl.format(despesa.data.valor ?? 0)}
            </span>
          </div>

          <DespesaDetails despesa={despesa.data} />
        </>
      ) : (
        <EmptyCard title="Despesa não encontrada" description="A API respondeu sem conteúdo para este lançamento." />
      )}
    </Card>
  );
}
