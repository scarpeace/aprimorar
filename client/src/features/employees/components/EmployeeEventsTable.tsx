import type { AtendimentoResponseDTO, PageDTOAtendimentoResponseDTO } from "@/kubb";
import { EmptyCard } from "@/components/ui/empty-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorCard } from "@/components/ui/error-card";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { EventContentLabels } from "@/features/appointments/lib/eventContentLables.ts";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useAppointmentMutations } from "@/features/appointments/hooks/use-appointment-mutations";
import { Button, ButtonLink } from "@/components/ui/button";
import {
  Calendar,
  CircleDollarSign,
  SquareArrowOutUpRight,
} from "lucide-react";
import { memo } from "react";

interface EmployeeEventsTableProps {
  appointments?: PageDTOAtendimentoResponseDTO;
  currentPage: number;
  error?: unknown;
  hidePaid: boolean;
  isLoading: boolean;
  onHidePaidChange: (value: boolean) => void;
  onPageChange: (page: number) => void;
}

export const EmployeeEventsTable = memo(function EmployeeEventsTable({
  appointments,
  currentPage,
  error,
  hidePaid,
  isLoading,
  onHidePaidChange,
  onPageChange,
}: EmployeeEventsTableProps) {
  const { toggleEmployeePayment } = useAppointmentMutations();
  const events = appointments?.content ?? [];
  const totalEvents = appointments?.totalElements ?? events.length;

  const handleToggleEmployeePayment = (eventId: string) => {
    toggleEmployeePayment.mutate({ id: eventId });
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
              Atendimentos vinculados ao colaborador
            </h3>
            {events.length > 0 && (
              <span className="badge badge-outline badge-primary badge-sm font-semibold">
                {totalEvents} {totalEvents === 1 ? "evento" : "eventos"}
              </span>
            )}
          </div>
          <p className="mt-1 text-sm text-base-content/60">
            Acompanhe horarios, conteudos aplicados e o status dos repasses deste colaborador.
          </p>
        </div>
      </div>

      <div className="shrink-0 rounded-2xl border border-info/20 bg-linear-to-r from-info/8 via-base-100 to-base-100 px-3 py-2 shadow-sm transition-all duration-200 hover:border-info/30 hover:shadow-md">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-primary/10 text-primary">
            <CircleDollarSign className="w-4" />
          </div>
          <ToggleSwitch
            toggled={hidePaid}
            setToggle={onHidePaidChange}
            label="Ocultar Pagos"
            className="border-info/30 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
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
          description="Os eventos vinculados ao colaborador aparecerao aqui assim que houver agendamentos registrados."
        />
      </section>
    );
  }

  return (
    <section className="relative rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_280ms_ease-out_both]">
      {header}

      <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
        <table className="table table-zebra w-full table-auto bg-base-100">
          <thead className="sticky top-0 z-10 bg-base-200/90 backdrop-blur">
            <tr>
              <th className="font-bold text-base-content/70">Aluno</th>
              <th className="font-bold text-base-content/70">Data</th>
              <th className="text-center font-bold text-base-content/70">Horario</th>
              <th className="text-center font-bold text-base-content/70">Conteudo</th>
              <th className="text-right font-bold text-base-content/70">Repasse</th>
              <th className="text-center font-bold text-base-content/70 pr-6">Acoes</th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {events.map((event: AtendimentoResponseDTO) => (
              <tr key={event.id} className="group transition-colors hover:bg-base-200/50">
                <td>
                  <div className="font-semibold text-base-content">{event.studentName}</div>
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
                      className={`inline-block h-2.5 w-2.5 rounded-full ${event.employeePaymentDate ? "bg-success" : "bg-warning"}`}
                      title={event.employeePaymentDate ? "Pago" : "Pendente"}
                    />
                    <span>{brl.format(event.payment)}</span>
                  </div>
                </td>

                <td className="relative z-20 text-center">
                  <div className="flex justify-center gap-1.5 opacity-80 transition-opacity group-hover:opacity-100">
                    <div
                      className="tooltip tooltip-left z-30 before:z-30 after:z-30"
                      data-tip={event.employeePaymentDate != null ? "Cancelar Pagamento" : "Marcar como Pago"}
                    >
                      <Button
                        disabled={toggleEmployeePayment.isPending}
                        className="h-9 w-9 p-0"
                        size="sm"
                        variant={event.employeePaymentDate != null ? "success" : "warning"}
                        onClick={() => handleToggleEmployeePayment(event.id)}
                      >
                        <CircleDollarSign size={18} />
                      </Button>
                    </div>
                    <div className="tooltip tooltip-left z-30 before:z-30 after:z-30" data-tip="Detalhes">
                      <ButtonLink to={`/appointments/${event.id}`} size="sm" className="h-9 w-9 p-0" variant="primary">
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

      <Pagination
        size={appointments?.size ?? 0}
        totalElements={appointments?.totalElements ?? 0}
        totalPages={appointments?.totalPages ?? 0}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </section>
  );
});
