import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { type AppointmentResponseDTO, type PageDTOAppointmentResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { CalendarCheck2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { AppointmentMobileCard } from "./AppointmentMobileCard";

type AppointmentsTableProps = {
  appointments?: PageDTOAppointmentResponseDTO;
  currentPage: number;
  onPageChange: (page: number) => void;
  isLoading: boolean;
  error: unknown;
};

export function AppointmentsTable({
  appointments,
  currentPage,
  onPageChange,
  isLoading,
  error,
}: Readonly<AppointmentsTableProps>) {
  const navigate = useNavigate();
  const events = appointments?.content ?? [];
  const totalEvents = appointments?.totalElements ?? events.length;

  if (isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de atendimentos"
        error={error}
      />
    );
  }

  if (events.length === 0) {
    return (
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <div className="mb-4 flex items-start gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-primary/10 text-primary">
            <CalendarCheck2 className="h-5 w-5" />
          </div>
          <div>
            <h3 className="text-xl font-bold text-base-content">Agenda de atendimentos</h3>
            <p className="text-sm text-base-content/60">
              Consulte os agendamentos registrados e acompanhe os status de cobrança e repasse.
            </p>
          </div>
        </div>

        <EmptyCard
          title="Nenhum atendimento encontrado"
          description="A lista sera exibida aqui assim que houver agendamentos cadastrados ou quando os filtros retornarem resultados."
        />
      </section>
    );
  }

  return (
    <section className="relative rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
      <div className="mb-5 flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
        <div className="flex items-start gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-primary/10 text-primary shadow-sm">
            <CalendarCheck2 className="h-5 w-5" />
          </div>
          <div>
            <div className="flex flex-wrap items-center gap-2">
              <h3 className="text-xl font-bold text-base-content">Agenda de atendimentos</h3>
              <span className="badge badge-outline badge-primary badge-sm font-semibold">
                {totalEvents} {totalEvents === 1 ? "evento" : "eventos"}
              </span>
            </div>
            <p className="mt-1 text-sm text-base-content/60">
              Visualize aluno, colaborador, horario e pendencias financeiras em uma unica listagem.
            </p>
          </div>
        </div>
      </div>

      <div className="hidden md:block">
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra w-full table-auto bg-base-100">
            <thead className="sticky top-0 z-10 bg-base-200/90 backdrop-blur">
              <tr>
                <th className="font-bold text-base-content/70">Aluno</th>
                <th className="font-bold text-base-content/70">Colaborador</th>
                <th className="font-bold text-base-content/70">Data</th>
                <th className="text-center font-bold text-base-content/70">Horario</th>
                <th className="text-center font-bold text-base-content/70">Conteudo</th>
                <th className="text-right font-bold text-base-content/70">Cobranca</th>
                <th className="text-right font-bold text-base-content/70">Repasse</th>
                {/* <th className="pr-6 text-center font-bold text-base-content/70">Acoes</th> */}
              </tr>
            </thead>

            <tbody className="whitespace-nowrap">
              {events.map((event: AppointmentResponseDTO) => (
                <tr
                  key={event.id}
                  className="group cursor-pointer transition-colors hover:bg-base-200/50"
                  onClick={() => navigate(`/appointments/${event.id}`)}
                >
                  <td>
                    <div className="font-semibold text-base-content">{event.studentName}</div>
                  </td>
                  <td>{event.employeeName}</td>
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
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2 font-mono text-sm font-semibold text-base-content">
                      <span
                        className={`inline-block h-2.5 w-2.5 rounded-full ${event.employeePaymentDate ? "bg-success" : "bg-warning"}`}
                        title={event.employeePaymentDate ? "Pago" : "Pendente"}
                      />
                      <span>{brl.format(event.payment)}</span>
                    </div>
                  </td>
                  {/* <td className="relative z-20 text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 transition-opacity group-hover:opacity-100">
                      <div
                        className="tooltip tooltip-left z-30 before:z-30 after:z-30"
                        data-tip={event.studentChargeDate ? "Cancelar Cobrança" : "Marcar como Cobrado"}
                      >
                        <Button
                          disabled={toggleStudentCharge.isPending || toggleEmployeePayment.isPending}
                          className="h-9 w-9 p-0"
                          size="sm"
                          variant={event.studentChargeDate ? "success" : "warning"}
                          onClick={() => handleToggleStudentCharge(event.id)}
                        >
                          <CircleDollarSign className="h-4.5 w-4.5" />
                        </Button>
                      </div>
                      <div
                        className="tooltip tooltip-left z-30 before:z-30 after:z-30"
                        data-tip={event.employeePaymentDate ? "Cancelar Pagamento" : "Marcar como Pago"}
                      >
                        <Button
                          disabled={toggleStudentCharge.isPending || toggleEmployeePayment.isPending}
                          className="h-9 w-9 p-0"
                          size="sm"
                          variant={event.employeePaymentDate ? "success" : "warning"}
                          onClick={() => handleToggleEmployeePayment(event.id)}
                        >
                          <CircleDollarSign className="h-4.5 w-4.5" />
                        </Button>
                      </div>
                      <div className="tooltip tooltip-left z-30 before:z-30 after:z-30" data-tip="Detalhes">
                        <ButtonLink to={`/appointments/${event.id}`} size="sm" className="h-9 w-9 p-0" variant="primary">
                          <SquareArrowOutUpRight className="h-4.5 w-4.5" />
                        </ButtonLink>
                      </div>
                    </div>
                  </td> */}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="flex flex-col gap-4 md:hidden">
        {events.map((event: AppointmentResponseDTO, index: number) => (
          <AppointmentMobileCard
            key={event.id}
            event={event}
            index={index}
            isPending={false}
            onToggleCharge={() => navigate(`/appointments/${event.id}`)}
            onTogglePayment={() => navigate(`/appointments/${event.id}`)}
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
}
