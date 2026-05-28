import { Button, ButtonLink } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import type { AtendimentoResponseDTO, PageDTOAtendimentoResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/features/appointments/lib/eventContentLabels.ts";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, SquareArrowOutUpRight } from "lucide-react";
import { memo } from "react";
import { AlunoAtendimentoMobileCard } from "./AlunoAtendimentoMobileCard";
import { useAtendimentoMutations } from "@/features/appointments/hooks/use-atendimento-mutations";

interface AlunoEventsTableProps {
  atendimentos?: PageDTOAtendimentoResponseDTO;
  currentPage: number;
  error?: unknown;
  hideCharged: boolean;
  isLoading: boolean;
  onHideChargedChange: (value: boolean) => void;
  onPageChange: (page: number) => void;
}

export const AlunoEventsTable = memo(function AlunoEventsTable({
  atendimentos,
  error,
  hideCharged,
  isLoading,
  currentPage,
  onHideChargedChange,
  onPageChange,
}: AlunoEventsTableProps) {
  const { toggleStudentCharge } = useAtendimentoMutations();
  const events = atendimentos?.content ?? [];

  const handleToggleStudentCharge = (atendimentoId: string) => {
    toggleStudentCharge.mutate({ id: atendimentoId });
  };

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={error} />;
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
                {events.length} {events.length === 1 ? "atendimentoo" : "atendimentoos"}
              </span>
            )}
          </div>
          <p className="mt-1 text-sm text-base-content/60">
            Acompanhe horarios, conteudos aplicados e o status das cobrancas registradas para este aluno.
          </p>
        </div>
      </div>

      <div className="shrink-0 rounded-2xl border border-warning/20 bg-linear-to-r from-warning/8 via-base-100 to-base-100 px-3 py-2 shadow-sm transition-all duration-200 hover:border-warning/30 hover:shadow-md">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-warning/12 text-warning">
            <CircleDollarSign className="w-4" />
          </div>
          <ToggleSwitch
            toggled={hideCharged}
            setToggle={onHideChargedChange}
            label="Ocultar Pagos"
            className="border-warning/30 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
          />
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
          description="Os atendimentoos vinculados ao aluno aparecerao aqui assim que houver agendamentos cadastrados."
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
                <th className="text-center font-bold text-base-content/70">Horario</th>
                <th className="text-center font-bold text-base-content/70">Conteudo</th>
                <th className="text-right font-bold text-base-content/70">Cobranca</th>
                <th className="text-center font-bold text-base-content/70 pr-6">Acoes</th>
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {events.map((atendimento: AtendimentoResponseDTO) => (
                <tr key={atendimento.id} className="group transition-colors hover:bg-base-200/50">
                  <td>
                    <div className="font-semibold text-base-content">{atendimento.employeeName}</div>
                  </td>
                  <td>{formatDateShortYear(atendimento.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(atendimento.startDate)} - {formatTime(atendimento.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[atendimento.content] || atendimento.content}
                    </span>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${atendimento.studentChargeDate ? "bg-success" : "bg-warning"}`}
                        title={atendimento.studentChargeDate ? "Cobrado" : "Pendente"}
                      />
                      <span>{brl.format(atendimento.price)}</span>
                    </div>
                  </td>
                  <td className="relative z-20 text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 transition-opacity group-hover:opacity-100">
                      <div
                        className="tooltip tooltip-left z-30 before:z-30 after:z-30"
                        data-tip={atendimento.studentChargeDate != null ? "Cancelar Cobrança" : "Marcar como Cobrado"}
                      >
                        <Button
                          disabled={toggleStudentCharge.isPending}
                          className="h-9 w-9 p-0"
                          size="sm"
                          variant={atendimento.studentChargeDate != null ? "success" : "warning"}
                          onClick={() => handleToggleStudentCharge(atendimento.id)}
                        >
                          <CircleDollarSign size={18} />
                        </Button>
                      </div>
                      <div className="tooltip tooltip-left z-30 before:z-30 after:z-30" data-tip="Detalhes">
                        <ButtonLink
                          to={`/appointments/${atendimento.id}`}
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
        {events.map((atendimento: AtendimentoResponseDTO, index: number) => (
          <AlunoAtendimentoMobileCard
            key={atendimento.id}
            atendimento={atendimento}
            index={index}
            isPending={toggleStudentCharge.isPending}
            onToggleCharge={() => handleToggleStudentCharge(atendimento.id)}
          />
        ))}
      </div>

      <Pagination
        size={atendimentos?.size ?? 0}
        totalElements={atendimentos?.totalElements ?? 0}
        totalPages={atendimentos?.totalPages ?? 0}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </section>
  );
});
