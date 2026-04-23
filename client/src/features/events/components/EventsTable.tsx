import { Badge } from "@/components/ui/badge";
import { ButtonLink, Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { type PageDTOEventResponseDTO, type EventResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { SquareArrowOutUpRightIcon, Pencil, CheckCircle2 } from "lucide-react";
import { useEventMutations } from "../hooks/use-event-mutations";

type EventsTableProps = {
  eventsPage?: PageDTOEventResponseDTO;
  currentPage: number;
  onPageChange: (page: number) => void;
  isPending: boolean;
  error: unknown;
  onEdit?: (event: EventResponseDTO) => void;
};

export function EventsTable({
  eventsPage,
  currentPage,
  onPageChange,
  isPending,
  error,
  onEdit,
}: Readonly<EventsTableProps>) {
  const { updateEvent } = useEventMutations();


  if (isPending) {
    return <LoadingSpinner text="Carregando Eventos..." />;
  }

  if (error) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de Eventos"
        error={error}
      />
    );
  }

  return (
    <>
      <table className="table table-zebra table-auto bg-base-100 overflow-x-auto shadow-2xl animate-[fade-up_280ms_ease-out_both]">
        <thead className="bg-base-300 rounded">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Aluno
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Colaborador
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Data
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Horário
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Status
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Conteúdo
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Valor
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Pagamento
            </th>
            <th className="text-right font-semibold text-base-content/80 pr-4">
              Ações
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {eventsPage?.content?.map((event) => (
            <tr
              key={event.eventId}
              className="transition-colors hover:bg-base-200/70"
            >
              <td className="">{event.studentName}</td>
              <td>{event.employeeName}</td>
              <td className="">{formatDateShortYear(event.startDate)}</td>
              <td className=" text-center">{formatTime(event.startDate)} - {formatTime(event.endDate)}</td>
              <td>
                {event.status === "SCHEDULED" && <Badge variant="primary">Agendado</Badge>}
                {event.status === "COMPLETED" && <Badge variant="success" className="p-1 px-2">Concluído</Badge>}
                {event.status === "CANCELED" && <Badge variant="error" className="p-1 px-2">Cancelado</Badge>}
              </td>
              <td className=" text-center">
                {EventContentLabels[event.content] || event.content}
              </td>

              <td>{brl.format(event.price)}</td>
              <td>{brl.format(event.payment)}</td>

              <td className="text-right flex justify-end gap-1 pr-2">
                {event.status !== "COMPLETED" && event.status !== "CANCELED" && (
                  <Button
                    className="btn-square btn-ghost btn-xs text-success"
                    onClick={() => handleStatusChange(event, "COMPLETED")}
                    title="Concluir"
                    disabled={updateEvent.isPending}
                  >
                    <CheckCircle2 size={16} />
                  </Button>
                )}
                {event.status !== "CANCELED" && (
                  <Button
                    className="btn-square btn-ghost btn-xs text-error"
                    onClick={() => handleStatusChange(event, "CANCELED")}
                    title="Cancelar"
                    disabled={updateEvent.isPending}
                  >
                    <XCircle size={16} />
                  </Button>
                )}
                {onEdit && (
                  <Button
                    className="btn-square btn-ghost btn-xs text-info"
                    onClick={() => onEdit(event)}
                    title="Editar"
                  >
                    <Pencil size={16} />
                  </Button>
                )}
                <ButtonLink
                  className="btn-square btn-xs"
                  to={`/events/${event.eventId}`}
                  variant="primary"
                  title="Detalhes"
                >
                  <SquareArrowOutUpRightIcon className="h-4 w-4" />
                </ButtonLink>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Pagination
        paginationData={eventsPage}
        currentPage={currentPage}
        onPageChange={onPageChange}
      />
    </>
  );
}
