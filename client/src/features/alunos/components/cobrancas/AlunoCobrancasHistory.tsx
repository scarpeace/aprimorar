"use client";

import { useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { useBuscarLotesDeCobranca } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarLotesDeCobranca";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

type AlunoCobrancasHistoryProps = {
  alunoId: string;
};

export function AlunoCobrancasHistory({ alunoId }: Readonly<AlunoCobrancasHistoryProps>) {
  const [page, setPage] = useState(0);
  const cobrancas = useBuscarLotesDeCobranca({
    alunoId,
    page,
    size: PAGE_SIZE,
  });

  const content = cobrancas.data?.content ?? [];
  const metadata = cobrancas.data?.page;
  const currentPage = metadata?.number ?? page;
  const totalElements = metadata?.totalElements ?? 0;
  const totalPages = metadata?.totalPages ?? 0;

  return (
    <Card className="h-full">
      <CardHeader>
        <div>
          <CardTitle>Histórico de cobranças</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os pagamentos registrados para este aluno.
          </p>
        </div>
      </CardHeader>

      {cobrancas.isLoading ? (
        <LoadingSkeleton className="h-48 w-full" />
      ) : cobrancas.error ? (
        <ErrorCard
          title="Não foi possível carregar o histórico de cobranças"
          description="A consulta dos pagamentos agrupados falhou. Tente novamente."
          error={cobrancas.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard
          title="Nenhuma cobrança paga"
          description="Os pagamentos registrados aparecerão aqui depois de recebidos."
        />
      ) : (
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="table table-zebra table-xs">
              <thead>
                <tr>
                  <th>Data</th>
                  <th>Pgto</th>
                  <th className="text-center">Cobranças</th>
                  <th className="text-right">Valor total</th>
                </tr>
              </thead>

              <tbody>
                {content.map((cobranca) => (
                  <tr key={cobranca.loteId}>
                    <td>
                      {formatDateShortYear(cobranca.dataPagamento)} às {formatTime(cobranca.dataPagamento)}
                    </td>
                    <td>{formaPagamentoLabels[cobranca.formaPagamento]}</td>
                    <td className="text-center">{cobranca.quantidadeCobrancas}</td>
                    <td className="text-right font-semibold">{brl.format(cobranca.valorTotal)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            summary={<>Mostrando {content.length} de {totalElements} pagamento(s)</>}
            onPrevious={() => setPage(currentPage - 1)}
            onNext={() => setPage(currentPage + 1)}
          />
        </div>
      )}
    </Card>
  );
}
