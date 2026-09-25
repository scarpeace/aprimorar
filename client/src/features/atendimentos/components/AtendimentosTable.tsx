"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { DateField } from "@/components/ui/DateField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { SearchInput } from "@/components/ui/SearchInput";
import { SelectField } from "@/components/ui/SelectField";
import { AtendimentoCard } from "@/features/atendimentos/components/AtendimentoCard";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import { useGetAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos/useGetAtendimentosIndividuais";
import type { GetAtendimentosIndividuaisQueryParamsTipoEnumKey } from "@/lib/api/generated/types/GetAtendimentosIndividuais";
import { atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;


type AtendimentoTipo = GetAtendimentosIndividuaisQueryParamsTipoEnumKey | "";

export function AtendimentosTable() {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [tipo, setTipo] = useState<AtendimentoTipo>("");

  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const search = useDebounce(searchInput.trim(), 300);

  const atendimentos = useGetAtendimentosIndividuais({
    page,
    size: PAGE_SIZE,
    busca: search || undefined,
    tipo: tipo || undefined,

    inicio: dataInicio ? `${dataInicio}T00:00:00` : undefined,
    fim: dataFim ? `${dataFim}T23:59:59` : undefined,
  });

  const content = atendimentos.data?.content ?? [];
  const metadata = atendimentos.data?.page;
  const totalPages = metadata?.totalPages ?? 0;
  const totalElements = metadata?.totalElements ?? 0;
  const currentPage = metadata?.number ?? page;

  function changeFilter(updateFilter: () => void) {
    setPage(0);
    updateFilter();
  }

  function openAtendimento(atendimentoId: number) {
    router.push(`/atendimentos/${atendimentoId}`);
  }

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Atendimentos cadastrados</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os atendimentos registrados e acesse seus detalhes.
          </p>
        </div>
      </CardHeader>

      <div className="flex flex-wrap items-end gap-3">
        <SearchInput
          label="Buscar"
          value={searchInput}
          onChange={(value) => changeFilter(() => setSearchInput(value))}
          placeholder="Busque pelo atendimento"
          className="min-w-60 flex-1"
        />

        <SelectField
          label="Tipo"
          value={tipo}
          options={atendimentoTipoOptions}
          onChange={(value) => changeFilter(() => setTipo(value as AtendimentoTipo))}
          className="w-full sm:w-48"
        />


        <DateField
          label="Início"
          value={dataInicio}
          onChange={(value) => changeFilter(() => setDataInicio(value))}
          className="w-full sm:w-44"
        />

        <DateField
          label="Fim"
          value={dataFim}
          onChange={(value) => changeFilter(() => setDataFim(value))}
          className="w-full sm:w-44"
        />
      </div>

      {atendimentos.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : atendimentos.error ? (
        <ErrorCard
          title="Não foi possível carregar os atendimentos"
          description="A consulta paginada falhou. Tente novamente."
          error={atendimentos.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhum atendimento encontrado" description="Ajuste os filtros para localizar os atendimentos desejados." />
      ) : (
        <div className="space-y-4">
          <div className="hidden overflow-x-auto md:block">
            <table className="table table-zebra">
              <thead>
                <tr>
                  <th>Aluno</th>
                  <th>Colaborador</th>
                  <th>Data</th>
                  <th>Horário</th>
                  <th>Tipo</th>
                  <th className="text-right">Cobrança</th>
                  <th className="text-right">Repasse</th>
                </tr>
              </thead>

              <tbody>
                {content.map((atendimento) => (
                  <tr
                    key={atendimento.id}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => openAtendimento(atendimento.id)}
                  >
                    <td className="font-semibold">{atendimento.alunoNome}</td>
                    <td>{atendimento.colaboradorNome}</td>
                    <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                    <td>
                      {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
                    </td>
                    <td>
                      <AtendimentoTipoBadge tipo={atendimento.tipo} />
                    </td>
                    <td className="w-10">
                      <div className="flex items-center justify-between gap-3">
                        <PaymentStatusIndicator status={atendimento.cobrancaStatus} />
                        <span>{brl.format(atendimento.valorCobranca)}</span>
                      </div>
                    </td>
                    <td className="w-10">
                      <div className="flex items-center justify-between gap-3">
                        <PaymentStatusIndicator status={atendimento.repasseStatus} />
                        <span>{brl.format(atendimento.valorRepasse)}</span>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="grid gap-4 md:hidden">
            {content.map((atendimento) => (
              <AtendimentoCard
                key={atendimento.id}
                atendimento={atendimento}
                onOpen={() => openAtendimento(atendimento.id)}
              />
            ))}
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            summary={
              <>
                Mostrando {content.length} de {totalElements} atendimento(s)
              </>
            }
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </Card>
  );
}
