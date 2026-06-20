import { ButtonLink } from "@/components/button.tsx";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/error-card.tsx";
import { LoadingSpinner } from "@/components/loading-spinner.tsx";
import { Pagination } from "@/components/pagination.tsx";
import { useGetColaboradoresList, type AtendimentoResponse, type PagedModelAtendimentoResponse } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/utils/formatter.ts";
import { Calendar, SquareArrowOutUpRight } from "lucide-react";
import { memo } from "react";
import { getParticipantName } from "@/features/atendimentos/lib/atendimento-participant-labels";
import { AlunoAtendimentoMobileCard } from "./AlunoAtendimentoMobileCard.tsx";

type AlunoEventsTableProps = {
  atendimentos?: PagedModelAtendimentoResponse;
  currentPage: number;
  error?: unknown;
  isLoading: boolean;
  onPageChange: (page: number) => void;
};

const tipoLabels: Record<AtendimentoResponse["tipo"], string> = {
  AULA: "Aula",
  MENTORIA: "Mentoria",
  TERAPIA: "Terapia",
  ORIENTACAO_VOCACIONAL: "Orientação Vocacional",
  ENEM: "Enem",
  PAS: "PAS",
  OUTRO: "Outro",
};

export const AlunoEventsTable = memo(function AlunoEventsTable({
  atendimentos,
  error,
  isLoading,
  currentPage,
  onPageChange,
}: Readonly<AlunoEventsTableProps>) {
  const colaboradoresQuery = useGetColaboradoresList();
  const events = atendimentos?.content ?? [];
  const totalEvents = atendimentos?.page?.totalElements ?? events.length;

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de atendimentos" error={error} />;
  }

  if (isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  const header = (
    <div className="mb-5 flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
      <div className="flex items-start gap-3">
        <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-primary/10 text-primary shadow-sm">
          <Calendar className="h-5 w-5" />
        </div>
        <div>
          <div className="flex flex-wrap items-center gap-2">
            <h3 className="text-xl font-bold text-base-content">
              Atendimentos vinculados ao aluno
            </h3>
            {events.length > 0 && (
              <span className="badge badge-outline badge-primary badge-sm font-semibold">
                {totalEvents} {totalEvents === 1 ? "atendimento" : "atendimentos"}
              </span>
            )}
          </div>
          <p className="mt-1 text-sm text-base-content/60">
            Acompanhe horários, tipos de atendimento e valores registrados para este aluno.
          </p>
        </div>
      </div>
    </div>
  );

  if (events.length === 0) {
    return (
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_280ms_ease-out_both]">
        {header}

        <EmptyCard
          title="Nenhum atendimento encontrado"
          description="Os atendimentos vinculados ao aluno aparecerão aqui assim que houver agendamentos cadastrados."
        />
      </section>
    );
  }

  return (
    <section className="relative rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_280ms_ease-out_both]">
      {header}

      <div className="hidden md:block">
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra w-full table-auto bg-base-100">
            <thead className="sticky top-0 z-10 bg-base-200/90 backdrop-blur">
              <tr>
                <th className="font-bold text-base-content/70">Colaborador</th>
                <th className="font-bold text-base-content/70">Data</th>
                <th className="text-center font-bold text-base-content/70">Horário</th>
                <th className="text-center font-bold text-base-content/70">Tipo</th>
                <th className="text-right font-bold text-base-content/70">Valor</th>
                <th className="text-center font-bold text-base-content/70 pr-6">Ações</th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {events.map((atendimento: AtendimentoResponse) => (
                  <tr key={atendimento.id} className="group transition-colors hover:bg-base-200/50">
                    <td>
                      <div className="font-semibold text-base-content">{getParticipantName(atendimento.colaboradorId, colaboradoresQuery.data)}</div>
                    </td>
                    <td>{formatDateShortYear(atendimento.inicio)}</td>
                    <td className="text-center text-sm font-medium">
                      {formatTime(atendimento.inicio)} - {formatTime(atendimento.fim)}
                    </td>
                    <td className="text-center">
                      <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                        {tipoLabels[atendimento.tipo] ?? atendimento.tipo}
                      </span>
                    </td>
                    <td className="text-right">
                      <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                        <span>{brl.format(atendimento.valor)}</span>
                      </div>
                    </td>
                    <td className="relative z-20 text-center">
                      <div className="flex justify-center gap-1.5 opacity-80 transition-opacity group-hover:opacity-100">
                        <div className="tooltip tooltip-left z-30 before:z-30 after:z-30" data-tip="Detalhes">
                          <ButtonLink
                            to={`/atendimentos/${atendimento.id}`}
                            size="sm"
                            className="h-9 w-9 p-0"
                            variant="primary"
                          >
                            <SquareArrowOutUpRight size={18} />
                          </ButtonLink>
                        </div>
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
          <AlunoAtendimentoMobileCard
            key={atendimento.id}
            atendimento={atendimento}
            colaboradorNome={getParticipantName(atendimento.colaboradorId, colaboradoresQuery.data)}
            index={index}
          />
        ))}
      </div>

      <Pagination
        size={atendimentos?.page?.size ?? 0}
        totalElements={atendimentos?.page?.totalElements ?? 0}
        totalPages={atendimentos?.page?.totalPages ?? 0}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </section>
  );
});
