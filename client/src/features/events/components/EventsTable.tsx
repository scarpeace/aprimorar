import { Link } from "react-router-dom"
import type { EventResponse } from "@/lib/schemas/event"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"

export type EventsTableVariant = "studentPage" | "eventsPage" | "employeePage"

type EventsTableProps = {
  events: EventResponse[]
  variant?: EventsTableVariant
}

export function EventsTable({ events, variant = "eventsPage" }: EventsTableProps) {
  const showPrice = variant === "eventsPage" || variant === "studentPage"
  const showPayment = variant === "employeePage"

  return (
    <div className="overflow-x-auto">
      <table className="table table-zebra w-full">
        <thead className="bg-base-200/90">
          <tr>
            <th className="app-th">Aluno</th>
            <th className="app-th">Colaborador</th>
            <th className="app-th">Data</th>
            <th className="app-th">Horario</th>
            <th className="app-th">Conteudo</th>
            {showPrice ? <th className="app-th">Valor</th> : null}
            {showPayment ? <th className="app-th">Pagamento</th> : null}
            <th className="app-th">Acoes</th>
          </tr>
        </thead>
        <tbody>
          {events.map((event) => (
            <tr className="transition-colors hover:bg-base-200/70" key={event.id}>
              <td>{event.studentName}</td>
              <td>{event.employeeName}</td>
              <td>{formatDateShortYear(event.startDate)}</td>
              <td>{formatTime(event.startDate)} as {formatTime(event.endDate)}</td>
              <td>{eventContentLabels[event.content]}</td>
              {showPrice ? <td>{brl.format(event.price)}</td> : null}
              {showPayment ? <td>{brl.format(event.payment)}</td> : null}
              <td>
                <Link className="btn btn-ghost btn-sm" to={`/events/${event.id}`}>
                  Detalhes
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
