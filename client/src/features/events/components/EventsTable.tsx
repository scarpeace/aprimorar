import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { EventResponseDTO } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import type { ReactNode } from "react";
import { useNavigate } from "react-router-dom";

type EventsTableStateProps = {
  isPending: boolean;
  error: unknown;
};

export function EventsTableState({
  isPending,
  error,
}: Readonly<EventsTableStateProps>) {

  if (isPending) return <LoadingSpinner text="Carregando Eventos..." />;

  if (error) return (
    <ErrorCard
      title="Não foi possível carregar a listagem de alunos"
      error={error}
    />
  )
}

type EventTableProps = {
  events?: EventResponseDTO[];
  children?: ReactNode;
  isPending: boolean;
  error: unknown;
};

export function EventsTable({
  error,
  events,
  children,
  isPending,
}: Readonly<EventTableProps>) {
  const navigate = useNavigate();
  return (
    <>
      <EventsTableState isPending={isPending} error={error} />
      <div>
        <table className="table table-zebra bg-base-100 overflow-x-auto w-full p-3 rounded-xl animate-[fade-up_280ms_ease-out_both]">
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
                Hora
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Valor
              </th>
              <th className="text-left font-semibold text-base-content/80">
                Pagamento
              </th>
              <th className="text-center pr-6 font-semibold text-base-content/80">
                Ações
              </th>
            </tr>
          </thead>

          <tbody className="whitespace-nowrap">
            {events?.map((event) => (
              <tr
                key={event.eventId}
                className="transition-colors hover:bg-base-200/70"
              >
                <td>{event.studentName}</td>

                <td className="text-center">{event.employeeName}</td>

                <td>{formatDateShortYear(event.startDate)}</td>
                <td>{formatTime(event.startDate)}</td>
                <td>{brl.format(event.price)}</td>
                <td>{brl.format(event.payment)}</td>

                <td className="text-center">
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
        {children}
      </div>
    </>
  );
}
