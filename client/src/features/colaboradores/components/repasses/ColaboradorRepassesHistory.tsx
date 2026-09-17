"use client";

import { useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { useBuscarLotesDeRepasse } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarLotesDeRepasse";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";

const PAGE_SIZE = 10;

type ColaboradorRepassesHistoryProps = {
  colaboradorId: string;
};

export function ColaboradorRepassesHistory({ colaboradorId }: Readonly<ColaboradorRepassesHistoryProps>) {
  const [page, setPage] = useState(0);
  const repasses = useBuscarLotesDeRepasse({
    colaboradorId,
    page,
    size: PAGE_SIZE,
  });

  const content = repasses.data?.content ?? [];
  const metadata = repasses.data?.page;
  const currentPage = metadata?.number ?? page;
  const totalElements = metadata?.totalElements ?? 0;
  const totalPages = metadata?.totalPages ?? 0;

  return (
    <Card className="h-full">
      <CardHeader>
        <div>
          <CardTitle>Histórico de repasses</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os pagamentos de repasses registrados para este colaborador.
          </p>
        </div>
      </CardHeader>

      {repasses.isLoading ? (
        <LoadingSkeleton className="h-48 w-full" />
      ) : repasses.error ? (
        <ErrorCard
          title="Não foi possível carregar o histórico de repasses"
          description="A consulta dos pagamentos agrupados falhou. Tente novamente."
          error={repasses.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard
          title="Nenhum repasse registrado"
          description="Os pagamentos de repasses aparecerão aqui depois de registrados."
        />
      ) : (
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="table table-zebra table-xs">
              <thead>
                <tr>
                  <th>Data</th>
                  {/*<th>Pgto</th>*/}
                  <th className="text-center">Repasses</th>
                  <th className="text-right">Valor total</th>
                </tr>
              </thead>

              <tbody>
                {content.map((repasse) => (
                  <tr key={repasse.loteId}>
                    <td>
                      {formatDateShortYear(repasse.dataRepasse)} às {formatTime(repasse.dataRepasse)}
                    </td>
                    {/*<td>{formaPagamentoLabels[repasse.formaPagamento]}</td>*/}
                    <td className="text-center">{repasse.quantidadeRepasses}</td>
                    <td className="text-right font-semibold">{brl.format(repasse.valorTotal)}</td>
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
