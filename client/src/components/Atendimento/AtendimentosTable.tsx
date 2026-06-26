import {
  useGetAtendimentos,
  type AtendimentoResponse,
  type AtendimentoResponseStatusEnumKey,
  type AtendimentoResponseTipoEnumKey,
} from "@/kubb";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { useNavigate } from "react-router-dom";
import { useDebounce } from "@/hooks/useDebounce.ts";
import { Fragment, useId, useState } from "react";
import { Button } from "@/components/Ui/Button.tsx";
import { monthTabs, tipoAtendimentoLabels } from "@/utils/constants/atendimento-constants.ts";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { EmptyCard } from "../Ui/EmptyCard.tsx";
import { AtendimentoMobileCard } from "./AtendimentoMobileCard.tsx";
import { brl } from "@/utils/formatter.ts";
import { formatDateShortYear, formatTime } from "@/utils/date-utils.ts";

type AtendimentosTableProps = {
  search: string;
  filterStatus: AtendimentoResponseStatusEnumKey | "";
  filterTipo: AtendimentoResponseTipoEnumKey | "";
};

export function AtendimentosTable({ search, filterStatus, filterTipo }: AtendimentosTableProps) {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const now = new Date();
  const [selectedYear, setSelectedYear] = useState(now.getFullYear());
  const [selectedMonthIndex, setSelectedMonthIndex] = useState(now.getMonth());
  const monthTabsGroup = useId();
  const debouncedSearch = useDebounce(search, 500);
  const anoMes = `${selectedYear}-${String(selectedMonthIndex + 1).padStart(2, "0")}`;

  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    anoMes,
    sort: ["inicio,desc", "id,asc"],
    busca: debouncedSearch || undefined,
    status: filterStatus || undefined,
    tipo: filterTipo || undefined,
  });

  const events = eventsQuery.data?.content ?? [];
  const pagination = eventsQuery.data?.page;
  const hasEvents = events.length > 0;

  return (
    <section className="mb-2 overflow-hidden rounded-2xl border border-base-300 bg-base-100 shadow-sm">
      <div className="flex items-center justify-center gap-2 border-b border-base-300 px-3 py-3 sm:gap-3">
        <Button
          type="button"
          variant="ghost"
          size="sm"
          className="btn-square"
          aria-label="Ano anterior"
          onClick={() => {
            setSelectedYear((currentYear) => currentYear - 1);
            setPage(0);
          }}
        >
          <ChevronLeft className="h-4 w-4" />
        </Button>

        <span className="min-w-20 text-2xl text-center font-semibold text-base-content">
          {selectedYear}
        </span>

        <Button
          type="button"
          variant="ghost"
          size="sm"
          className="btn-square"
          aria-label="Próximo ano"
          onClick={() => {
            setSelectedYear((currentYear) => currentYear + 1);
            setPage(0);
          }}
        >
          <ChevronRight className="h-4 w-4" />
        </Button>
      </div>

      <div className="overflow-x-auto">
        <div className="tabs tabs-lift pt-2 justify-between">
          {monthTabs.map((month, monthIndex) => {
            const isActive = monthIndex === selectedMonthIndex;

            return (
              <Fragment key={month}>
                <input
                  type="radio"
                  name={monthTabsGroup}
                  className="tab whitespace-nowrap"
                  aria-label={month}
                  checked={isActive}
                  onChange={() => {
                    setSelectedMonthIndex(monthIndex);
                    setPage(0);
                  }}
                />
                <div className="tab-content border-base-300 rounded-xl">
                  {isActive ? (
                    <>
                      {eventsQuery.isError && (
                        <ErrorCard title="Não foi possível carregar a listagem de atendimentos" error={eventsQuery.error} />
                      )}

                      {eventsQuery.isLoading && (
                        <LoadingSpinner text="Carregando atendimentos..." />
                      )}

                      {!eventsQuery.isLoading && !eventsQuery.isError && !hasEvents && (
                        <EmptyCard title="Nenhum atendimento encontrado" description="Altere os filtros ou selecione outro período." />
                      )}

                      {hasEvents && (
                        <>
                          <div className="hidden md:block">
                            <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
                              <table className="table table-zebra w-full table-auto bg-base-100">
                                <thead className="sticky z-10 bg-base-200/90">
                                  <tr>
                                    <th className="font-bold text-base-content/70">Aluno</th>
                                    <th className="font-bold text-base-content/70">Colaborador</th>
                                    <th className="font-bold text-base-content/70">Data</th>
                                    <th className="text-center font-bold text-base-content/70">Horário</th>
                                    <th className="text-center font-bold text-base-content/70">Tipo</th>
                                    <th className="text-center font-bold text-base-content/70">Status</th>
                                    <th className="text-right font-bold text-base-content/70">Valor</th>
                                    <th className="text-right font-bold text-base-content/70">Repasse</th>
                                  </tr>
                                </thead>

                                <tbody className="whitespace-nowrap">
                                  {events.map((atendimento: AtendimentoResponse) => (
                                    <tr
                                      key={atendimento.id}
                                      className="group cursor-pointer transition-colors hover:bg-base-200/50"
                                      onClick={() => navigate(`/atendimentos/${atendimento.id}`)}
                                    >
                                      <td>
                                        <div className="font-semibold text-base-content">{atendimento.nomeAluno}</div>
                                      </td>
                                      <td>{atendimento.nomeColaborador}</td>
                                      <td>{formatDateShortYear(atendimento.inicio)}</td>
                                      <td className="text-center text-sm font-medium">
                                        {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
                                      </td>
                                      <td className="text-center">
                                        <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                                          {tipoAtendimentoLabels[atendimento.tipo] ?? atendimento.tipo}
                                        </span>
                                      </td>
                                      <td className="text-center">
                                        <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                                          {atendimento.status}
                                        </span>
                                      </td>
                                      <td className="text-right">
                                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                                          <span>{brl.format(atendimento.valor)}</span>
                                        </div>
                                      </td>
                                      <td className="text-right">
                                        <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                                          <span>{brl.format(atendimento.repasse)}</span>
                                        </div>
                                      </td>
                                    </tr>
                                  ))}
                                </tbody>
                              </table>
                            </div>
                          </div>

                          <div className="flex flex-col gap-4 md:hidden">
                            {events.map((atendimento: AtendimentoResponse, index: number) => (
                              <AtendimentoMobileCard
                                key={atendimento.id}
                                atendimento={atendimento}
                                alunoNome={atendimento.nomeAluno}
                                colaboradorNome={atendimento.nomeColaborador}
                                index={index}
                              />
                            ))}
                          </div>
                        </>
                      )}

                      <Pagination
                        size={pagination?.size ?? 0}
                        totalElements={pagination?.totalElements ?? 0}
                        totalPages={pagination?.totalPages ?? 0}
                        currentPage={page}
                        onPageChange={setPage}
                      />
                    </>
                  ) : null}
                </div>
              </Fragment>
            );
          })}
        </div>
      </div>
    </section>
  );
}
