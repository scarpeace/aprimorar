import { ButtonLink } from "@/components/ui/button"
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
    <div className="app-table-wrap">
      <table className="table table-zebra w-full">
        <thead className="bg-base-200/90">
          <tr>
            <th className="app-th">Aluno</th>
            <th className="app-th">Colaborador</th>
            <th className="app-th">Data</th>
            <th className="app-th">Horário</th>
            <th className="app-th">Conteúdo</th>
            {showPrice ? <th className="app-th hidden lg:table-cell">Valor</th> : null}
            {showPayment ? <th className="app-th hidden lg:table-cell">Pagamento</th> : null}
            <th className="app-th">Ações</th>
          </tr>
        </thead>
        <tbody>
          {events.map((event) => (
            <tr className="transition-colors hover:bg-base-200/70" key={event.id}>
              <td>{event.studentName}</td>
              <td>{event.employeeName}</td>
              <td>{formatDateShortYear(event.startDate)}</td>
              <td>{formatTime(event.startDate)} às {formatTime(event.endDate)}</td>
              <td>{eventContentLabels[event.content]}</td>
              {showPrice ? <td className="hidden lg:table-cell">{brl.format(event.price)}</td> : null}
              {showPayment ? <td className="hidden lg:table-cell">{brl.format(event.payment)}</td> : null}
              <td>
                <ButtonLink size="sm" to={`/events/${event.id}`} variant="outline">
                  Detalhes
                </ButtonLink>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
