import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { Pagination } from "@/components/ui/pagination";
import type { PageDTOEventResponseDTO } from "@/kubb";
import {
  brl,
  formatDateShortYear,
  formatTime,
} from "@/lib/utils/formatter";
import { type ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import { eventContentLabels } from "../hooks/eventContentLabels";

type EventsTableProps = {
  eventsPage?: PageDTOEventResponseDTO;
  children?: ReactNode;
  currentPage: number;
  onPageChange: (page: number) => void;
  isPending: boolean;
  error: unknown;
};

export function EventsTable({
  eventsPage,
  currentPage,
  onPageChange,
  isPending,
  error,
}: Readonly<EventsTableProps>) {
  const navigate = useNavigate();
  if (isPending) {
    return <LoadingSpinner text="Carregando Eventos..." />;
  }

  if (error) {
    return <ErrorCard title="Não foi possível carregar a listagem de Eventos" error={error} />;
  }

  return (
    <>
      <div>
        <table className="table table-zebra table-auto bg-base-100 overflow-x-auto w-full p-3 rounded animate-[fade-up_280ms_ease-out_both]">
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
                Horário incío
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Horário fim
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

              {/*<th className="text-left font-semibold text-base-content/80">
                Status
              </th>*/}
              <th className="text-left font-semibold text-base-content/80">
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
                <td className="p-3">{event.studentName}</td>
                <td className="p-3">{event.employeeName}</td>
                <td className="p-3">{formatDateShortYear(event.startDate)}</td>
                <td className="p-3 text-center">{formatTime(event.startDate)}</td>
                <td className="p-3 text-center">{formatTime(event.endDate)}</td>
                <td className="p-3 text-center">{eventContentLabels[event.content] || event.content}</td>

                <td className="p-3">{brl.format(event.price)}</td>
                <td className="p-3">{brl.format(event.payment)}</td>
                {/*<td>NAO IMPLEMENTADO</td>*/}

                <td className="p-2">
                  <span
                    className="btn m-2 btn-secondary"
                    onClick={() => navigate(`/events/${event.eventId}`)}
                  >
                    Detalhes
                  </span>
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
      </div>
    </>
  );
}
