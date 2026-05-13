import { Button, ButtonLink } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { AppointmentResponseDTO, PageDTOAppointmentResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, SquareArrowOutUpRight } from "lucide-react";
import { memo } from "react";
import { StudentAppointmentMobileCard } from "./StudentAppointmentMobileCard";
import { useAppointmentMutations } from "@/features/appointments/hooks/use-appointment-mutations";

interface StudentEventsTableProps {
  appointments?: PageDTOAppointmentResponseDTO;
  currentPage: number;
  error?: unknown;
  isLoading: boolean;
  onPageChange: (page: number) => void;
}

export const StudentEventsTable = memo(function StudentEventsTable({
  appointments,
  error,
  isLoading,
  currentPage,
  onPageChange,
}: StudentEventsTableProps) {
  const { toggleStudentCharge } = useAppointmentMutations();
  const events = appointments?.content ?? [];
  const totalEvents = appointments?.totalElements ?? events.length;

  const handleToggleStudentCharge = (appointmentId: string) => {
    toggleStudentCharge.mutate({ id: appointmentId });
  };

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={error} />;
  }

  if (isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  if (events.length === 0) {
    return (
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_280ms_ease-out_both]">
        <div className="mb-4 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex items-start gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-primary/10 text-primary">
              <Calendar className="h-5 w-5" />
            </div>
            <div>
              <h3 className="text-xl font-bold text-base-content">Atendimentos vinculados ao aluno</h3>
              <p className="text-sm text-base-content/60">
                Historico de aulas, horarios e cobrancas relacionadas ao aluno.
              </p>
            </div>
          </div>
        </div>

        <EmptyCard
          title="Nenhum atendimento encontrado"
          description="Os eventos vinculados ao aluno aparecerao aqui assim que houver agendamentos cadastrados."
        />
      </section>
    );
  }

  return (
    <section className="relative rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_280ms_ease-out_both]">
      <div className="mb-5 flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
        <div className="flex items-start gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-primary/10 text-primary shadow-sm">
            <Calendar className="h-5 w-5" />
          </div>
          <div>
            <div className="flex flex-wrap items-center gap-2">
              <h3 className="text-xl font-bold text-base-content">Atendimentos vinculados ao aluno</h3>
              <span className="badge badge-outline badge-primary badge-sm font-semibold">
                {totalEvents} {totalEvents === 1 ? "evento" : "eventos"}
              </span>
            </div>
            <p className="mt-1 text-sm text-base-content/60">
              Acompanhe horarios, conteudos aplicados e o status das cobrancas registradas para este aluno.
            </p>
          </div>
        </div>
      </div>

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
              {events.map((event: AppointmentResponseDTO) => (
                <tr key={event.id} className="group transition-colors hover:bg-base-200/50">
                  <td>
                    <div className="font-semibold text-base-content">{event.employeeName}</div>
                  </td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${event.studentChargeDate ? "bg-success" : "bg-warning"}`}
                        title={event.studentChargeDate ? "Cobrado" : "Pendente"}
                      />
                      <span>{brl.format(event.price)}</span>
                    </div>
                  </td>
                  <td className="relative z-20 text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 transition-opacity group-hover:opacity-100">
                      <div
                        className="tooltip tooltip-left z-30 before:z-30 after:z-30"
                        data-tip={event.studentChargeDate != null ? "Cancelar Cobrança" : "Marcar como Cobrado"}
                      >
                        <Button
                          disabled={toggleStudentCharge.isPending}
                          className="h-9 w-9 p-0"
                          size="sm"
                          variant={event.studentChargeDate != null ? "success" : "warning"}
                          onClick={() => handleToggleStudentCharge(event.id)}
                        >
                          <CircleDollarSign size={18} />
                        </Button>
                      </div>
                      <div className="tooltip tooltip-left z-30 before:z-30 after:z-30" data-tip="Detalhes">
                        <ButtonLink
                          to={`/appointments/${event.id}`}
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
        {events.map((event: AppointmentResponseDTO, index: number) => (
          <StudentAppointmentMobileCard
            key={event.id}
            event={event}
            index={index}
            isPending={toggleStudentCharge.isPending}
            onToggleCharge={() => handleToggleStudentCharge(event.id)}
          />
        ))}
      </div>

      <Pagination
        paginationData={appointments}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </section>
  );
});
