import type { AppointmentResponseDTO, PageDTOAppointmentResponseDTO } from "@/kubb";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorCard } from "@/components/ui/error-card";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { Pagination } from "@/components/ui/pagination";
import { useAppointmentMutations } from "@/features/appointments/hooks/use-appointment-mutations";
import { Button, ButtonLink } from "@/components/ui/button";
import {
  Calendar,
  CircleDollarSign,
  SquareArrowOutUpRight,
} from "lucide-react";
import { memo } from "react";

interface EmployeeEventsTableProps {
  appointments?: PageDTOAppointmentResponseDTO;
  currentPage: number;
  error?: unknown;
  isLoading: boolean;
  onPageChange: (page: number) => void;
}

export const EmployeeEventsTable = memo(function EmployeeEventsTable({
  appointments,
  currentPage,
  error,
  isLoading,
  onPageChange,
}: EmployeeEventsTableProps) {
  const { toggleEmployeePayment } = useAppointmentMutations();

  const handleToggleEmployeePayment = (eventId: string) => {
    toggleEmployeePayment.mutate({ id: eventId });
  };

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={error} />;
  }

  if (isLoading) {
    return <LoadingSpinner text="Carregando atendimentos..." />;
  }

  return (
    <div className="relative min-h-150">
      <div className="p-4 rounded-xl bg-base-100">
      <div className="flex gap-3 mb-6">
        <Calendar className="text-base-content/80" />
        <h3 className="text-xl font-bold text-base-content/80">
          Atendimentos vinculados ao colaborador
        </h3>
      </div>

        <table className="table table-zebra w-full table-auto bg-base-100">
          <thead className="bg-base-300 sticky top-0 z-10">
            <tr>
              <th className="font-bold text-base-content/70">Aluno</th>
              <th className="font-bold text-base-content/70">Data</th>
              <th className="text-center font-bold text-base-content/70">Horário</th>
              <th className="text-center font-bold text-base-content/70">Conteúdo</th>
              <th className="text-right font-bold text-base-content/70">Repasse</th>
              <th className="text-center font-bold text-base-content/70 pr-6">Ações</th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {appointments?.content?.map((event: AppointmentResponseDTO) => (
                <tr key={event.id} className="hover:bg-base-200/50 transition-colors group">
                  <td className="font-semibold">{event.studentName}</td>
                  <td>{formatDateShortYear(event.startDate)}</td>
                  <td className="text-center text-sm font-medium">
                    {formatTime(event.startDate)} - {formatTime(event.endDate)}
                  </td>
                  <td className="text-center">
                    <span className="badge badge-sm badge-outline font-bold uppercase text-[10px]">
                      {EventContentLabels[event.content] || event.content}
                    </span>
                  </td>

                  <td className="text-right font-mono text-sm">{brl.format(event.payment)}</td>

                  <td className="text-center">
                    <div className="flex justify-center gap-1.5 opacity-80 group-hover:opacity-100 transition-opacity">
                      <div className="tooltip" data-tip={event.employeePaymentDate != null ? "Cancelar Pagamento" : "Marcar como Pago"}>
                        <Button
                          disabled={toggleEmployeePayment.isPending}
                          className="w-9 h-9 p-0"
                          size="sm"
                          variant={event.employeePaymentDate != null ? "success" : "warning"}
                          onClick={() => handleToggleEmployeePayment(event.id)}
                        >
                          <CircleDollarSign size={18} />
                        </Button>
                      </div>
                      <div className="tooltip" data-tip="Detalhes">
                        <ButtonLink to={`/appointments/${event.id}`} size="sm" className="w-9 h-9 p-0" variant="primary">
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
        paginationData={appointments}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </div>
  );
});
