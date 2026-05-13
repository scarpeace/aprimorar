import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { AppointmentResponseDTO, PageDTOAppointmentResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, CircleDollarSign, SquareArrowOutUpRight } from "lucide-react";
import { memo } from "react";
import { StudentEventMobileCard } from "./StudentEventMobileCard";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useAppointmentMutations } from "@/features/appointments/hooks/use-appointment-mutations";

interface StudentEventsTableProps {
  appointments?: PageDTOAppointmentResponseDTO;
  currentPage: number;
  error?: unknown;
  isLoading: boolean;
  isTogglingCharge: boolean;
  onPageChange: (page: number) => void;
}

export const StudentEventsTable = memo(function StudentEventsTable({
  appointments,
  currentPage,
  error,
  isLoading,
  isTogglingCharge,
  onPageChange,
}: StudentEventsTableProps) {

  const { toggleStudentCharge } = useAppointmentMutations();

  const handleToggleStudentCharge = (appointmentId: string) => {
    toggleStudentCharge.mutate({ id: appointmentId });
  };

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={error} />;
  }

  if (isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  return (
    <div className="relative min-h-75">

      {/* Desktop View (Table) */}
      <div className="p-4 rounded-xl bg-base-100">
      <div className="flex gap-3 mb-4">
        <Calendar className="text-base-content/80" />
        <h3 className="text-xl font-bold text-base-content/80">
          Atendimentos vinculados ao aluno
        </h3>
      </div>

        <table className="table table-zebra w-full table-auto bg-base-100">
          <thead className="bg-base-300 sticky top-0 z-10">
            <tr>
              <th className="font-bold text-base-content/70">Colaborador</th>
              <th className="font-bold text-base-content/70">Data</th>
              <th className="text-center font-bold text-base-content/70">Horário</th>
              <th className="text-center font-bold text-base-content/70">Conteúdo</th>
              <th className="text-right font-bold text-base-content/70">Cobrança</th>
              <th className="text-center font-bold text-base-content/70 pr-6">Ações</th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {appointments?.content.map((event: AppointmentResponseDTO) => (
                <tr key={event.id} className="hover:bg-base-200/50 transition-colors group">
                  <td className="font-semibold">{event.employeeName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>
                  <td className="text-right font-mono text-sm">
                    {brl.format(event.price)}
                  </td>
                  <td className="text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 group-hover:opacity-100 transition-opacity">
                      <div className="tooltip" data-tip={event.studentChargeDate != null ? "Cancelar Cobrança" : "Marcar como Cobrado"}>
                        <Button
                          disabled={isTogglingCharge}
                          className="w-9 h-9 p-0"
                          size="sm"
                          variant={event.studentChargeDate != null ? "success" : "warning"}
                          onClick={() => handleToggleStudentCharge(event.id)}
                        >
                          <CircleDollarSign size={18}/>
                        </Button>
                      </div>
                      <div className="tooltip" data-tip="Detalhes">
                        <ButtonLink to={`/appointments/${event.id}`} size="sm" className="w-9 h-9 p-0" variant="primary">
                          <SquareArrowOutUpRight size={18}/>
                        </ButtonLink>
                      </div>
                    </div>
                  </td>
                </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Mobile View (Cards) */}
      <div className="md:hidden flex flex-col gap-4">
        {appointments?.content?.map((event: AppointmentResponseDTO, index: number) => (
          <StudentEventMobileCard
            key={event.id}
            event={event}
            index={index}
            isPending={isTogglingCharge}
            onToggleCharge={() => handleToggleStudentCharge(event.id)}
          />
        ))}
      </div>

      <Pagination
        paginationData={appointments}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </div>
  );
});
