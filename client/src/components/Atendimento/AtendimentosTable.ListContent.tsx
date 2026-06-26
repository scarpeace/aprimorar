import { AtendimentoMobileCard } from "@/components/Atendimento/AtendimentoMobileCard.tsx";
import { ErrorCard } from "@/components/Ui/ErrorCard.tsx";
import { LoadingSpinner } from "@/components/Ui/LoadingSpinner.tsx";
import { Pagination } from "@/components/Ui/Pagination.tsx";
import { type AtendimentoResponse } from "@/kubb";
import { formatDateShortYear, formatTime } from "@/utils/date-utils.ts";
import { brl } from "@/utils/formatter.ts";
import { tipoAtendimentoLabels } from "@/utils/constants/atendimento-constants.ts";
import { EmptyCard } from "../Ui/EmptyCard.tsx";

type EventsQuery = ReturnType<typeof import("@/kubb").useGetAtendimentos>;

type PaginationData = {
  size?: number;
  totalElements?: number;
  totalPages?: number;
};

type AtendimentosListContentProps = {
  eventsQuery: EventsQuery;
  events: AtendimentoResponse[];
  hasEvents: boolean;
  page: number;
  pagination?: PaginationData;
  onPageChange: (page: number) => void;
  onRowClick: (id: number) => void;
};

export function AtendimentosTableListContent({
  eventsQuery,
  events,
  hasEvents,
  page,
  pagination,
  onPageChange,
  onRowClick,
}: AtendimentosListContentProps) {
  return (
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
                  {events.map((atendimento) => (
                    <tr
                      key={atendimento.id}
                      className="group cursor-pointer transition-colors hover:bg-base-200/50"
                      onClick={() => onRowClick(atendimento.id)}
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
            {events.map((atendimento, index) => (
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
        onPageChange={onPageChange}
      />
    </>
  );
}
