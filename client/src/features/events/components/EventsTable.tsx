import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import { type EventResponseDTO, type PageDTOEventResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { useNavigate } from "react-router-dom";
import { EventStatusBadge } from "./EventStatusBadge";

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
  const navigate = useNavigate();
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
              Conteúdo
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Valor
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Pagamento
            </th>
            <th className="text-center font-semibold text-base-content/80 pr-4">
              Status
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {eventsPage?.content?.length === 0 ? (
            <tr>
              <td colSpan={8} className="text-center py-8 text-base-content/50">
                Nenhum atendimento encontrado.
              </td>
            </tr>
          ) : (
            eventsPage?.content?.map((event) => (
              <tr
                onClick={() => navigate(`/events/${event.eventId}`)}
                key={event.eventId}
                className="transition-colors hover:bg-base-300/70 hover:cursor-pointer"
              >
                <td className="">{event.studentName}</td>
                <td>{event.employeeName}</td>
                <td className="">{formatDateShortYear(event.startDate)}</td>
                <td className=" text-center">
                  {formatTime(event.startDate)} - {formatTime(event.endDate)}
                </td>
                <td className=" text-center">
                  {EventContentLabels[event.content] || event.content}
                </td>

                <td>{brl.format(event.price)}</td>
                <td>{brl.format(event.payment)}</td>

                <td className="text-center flex justify-center gap-1">
                  <EventStatusBadge event={event} />
                </td>
              </tr>
            ))
          )}
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
