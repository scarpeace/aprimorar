"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { DateField } from "@/components/ui/DateField";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { Pagination } from "@/components/ui/Pagination";
import { PaymentStatusIndicator } from "@/components/ui/PaymentStatusIndicator";
import { SearchInput } from "@/components/ui/SearchInput";
import { SelectField } from "@/components/ui/SelectField";
import { ColaboradorAtendimentoCard } from "@/features/colaboradores/components/atendimentos/ColaboradorAtendimentoCard";
import { AtendimentoTipoBadge } from "@/features/atendimentos/components/AtendimentoTipoBadge";
import { RegistrarRepasseButton } from "@/features/repasses/components/RegistrarRepasseButton";
import { useBuscarAtendimentosIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import type { BuscarAtendimentosIndividuaisQueryParamsTipoEnumKey } from "@/lib/api/generated/types/BuscarAtendimentosIndividuais";
import { atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";
import { useDebounce } from "@/lib/hooks/use-debounce";
import { formatDateShortYear, formatTime } from "@/lib/utils/date-utils";
import { brl } from "@/lib/utils/formatter";

const PAGE_SIZE = 10;

type ColaboradorAtendimentoTipo = BuscarAtendimentosIndividuaisQueryParamsTipoEnumKey | "";

type ColaboradorAtendimentosTableProps = {
  colaboradorId: string;
};

export function ColaboradorAtendimentosTable({ colaboradorId }: Readonly<ColaboradorAtendimentosTableProps>) {
  const router = useRouter();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState("");
  const [tipo, setTipo] = useState<ColaboradorAtendimentoTipo>("");

  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const search = useDebounce(searchInput.trim(), 300);

  const atendimentos = useBuscarAtendimentosIndividuais({
    page,
    size: PAGE_SIZE,
    colaboradorId,
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
          <CardTitle>Atendimentos do colaborador</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os atendimentos vinculados a este colaborador.
          </p>
        </div>

        <CardActions>
          <RegistrarRepasseButton colaboradorId={colaboradorId} />
        </CardActions>
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
          onChange={(value) => changeFilter(() => setTipo(value as ColaboradorAtendimentoTipo))}
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
          title="Não foi possível carregar os atendimentos do colaborador"
          description="A consulta paginada falhou. Tente novamente."
          error={atendimentos.error}
        />
      ) : content.length === 0 ? (
        <EmptyCard title="Nenhum atendimento encontrado" description="Não existem atendimentos para os filtros informados." />
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
                </tr>
              </thead>

              <tbody>
                {content.map((atendimento) => (
                  <tr
                    key={atendimento.id}
                    className="cursor-pointer hover:bg-base-200/70"
                    onClick={() => openAtendimento(atendimento.id)}
                  >
                    <td className="font-semibold">{atendimento.alunoResumo.nome}</td>
                    <td>{formatDateShortYear(atendimento.dataHoraInicio)}</td>
                    <td>
                      {formatTime(atendimento.dataHoraInicio)} - {formatTime(atendimento.dataHoraFim)}
                    </td>
                    <td>
                      <AtendimentoTipoBadge tipo={atendimento.tipo} />
                    </td>
                    <td className="w-10">
                      <div className="flex items-center justify-between gap-3">
                        <PaymentStatusIndicator status={atendimento.repasse.status} />
                        <span>{brl.format(atendimento.repasse.valor)}</span>
                      </div>
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
                onOpen={() => openAtendimento(atendimento.id)}
              />
            ))}
          </div>

          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            summary={<>Mostrando {content.length} de {totalElements} atendimento(s)</>}
            onPrevious={() => setPage((value) => value - 1)}
            onNext={() => setPage((value) => value + 1)}
          />
        </div>
      )}
    </Card>
  );
}
