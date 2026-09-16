"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { SearchInput } from "@/components/ui/SearchInput";
import { TablePagination } from "@/components/ui/TablePagination";
import { AtendimentoPaymentBadge } from "@/features/atendimentos/components/AtendimentoPaymentBadge";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import { ColaboradorAtendimentoCard } from "@/features/colaboradores/components/ColaboradorAtendimentoCard";
import { useBuscarAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 20;

type ColaboradorAtendimentosTableProps = {
  colaboradorId: string;
};

export function ColaboradorAtendimentosTable({ colaboradorId }: Readonly<ColaboradorAtendimentosTableProps>) {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const search = useDebounce(searchInput.trim(), 300);

  const atendimentos = useBuscarAtendimentosIndividuais({
    page,
    size: PAGE_SIZE,
    colaboradorId,
    busca: search || undefined,
  });

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;
  const hasPrevious = currentPage > 0;
  const hasNext = totalPages > 0 && currentPage < totalPages - 1;

  function handleSearchChange(value: string) {
    setPage(0);
    setSearchInput(value);
  }

  function openAtendimento(atendimentoId: number) {
    router.push(`/atendimentos/${atendimentoId}`);
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Atendimentos do colaborador</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Consulte os atendimentos vinculados a este colaborador.</p>
        </div>
      </CardHeader>

      <SearchInput
        label="Buscar"
        value={searchInput}
        onChange={handleSearchChange}
        placeholder="Busque pelo atendimento"
        className="max-w-md"
      />

      {atendimentos.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : atendimentos.error ? (
        <ErrorCard
          title="Não foi possível carregar os atendimentos do colaborador"
          description="A consulta paginada falhou. Tente novamente."
          error={atendimentos.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhum atendimento encontrado" description="Não existem atendimentos para a busca informada." />
      ) : (
        <div className="space-y-4">
          <div className="hidden overflow-x-auto md:block">
            <table className="table table-zebra">
              <thead>
                <tr>
                  <th>Aluno</th>
                  <th>Data</th>
                  <th>Horário</th>
                  <th>Tipo</th>
                  <th className="text-right">Repasse</th>
                  <th>Situação</th>
                </tr>
              </thead>

              <tbody>
                {content.map((atendimento) => (
                  <tr
                    key={atendimento.id}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => openAtendimento(atendimento.id)}
                  >
                    <td className="font-semibold">{atendimento.alunoResumo.nome ?? "Não informado"}</td>
                    <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                    <td>
                      {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
                    </td>
                    <td>
                      <AtendimentoTipoBadge tipo={atendimento.tipo} />
                    </td>
                    <td className="text-right">{brl.format(atendimento.repasse.valor ?? 0)}</td>
                    <td>
                      <AtendimentoPaymentBadge label="Repasse" paidAt={atendimento.repasse.dataRepasse} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="grid gap-4 md:hidden">
            {content.map((atendimento) => (
              <ColaboradorAtendimentoCard
                key={atendimento.id}
                atendimento={atendimento}
                onClick={() => openAtendimento(atendimento.id)}
              />
            ))}
          </div>

          <TablePagination
            summary={`${totalElements} atendimento(s) encontrado(s) • página ${currentPage + 1}${totalPages > 0 ? ` de ${totalPages}` : ""}`}
            hasPrevious={hasPrevious}
            hasNext={hasNext}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </Card>
  );
}
