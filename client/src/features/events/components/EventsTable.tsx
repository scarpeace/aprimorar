import { ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { LoadingCard } from "@/components/ui/loading-card"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"
import { useEventsByEmployeeQuery, useEventsByStudentQuery, useEventsQuery } from "../hooks/use-events"
import { useState } from "react"
import { Pagination } from "@/components/ui/pagination"

type EventsTableProps = {
  variant: "eventsPage" | "embeddedEmployee" | "embeddedStudent"
  ownerId?: string
}

export function EventsTable({ variant = "eventsPage", ownerId }: Readonly<EventsTableProps>) {
  const [currentPage, setCurrentPage] = useState(0)
  const pageSize = 10

  const allResults = useEventsQuery(currentPage, pageSize, "startDate", {
    enabled: variant === "eventsPage"
  });
  const employeeResults = useEventsByEmployeeQuery(ownerId!, currentPage, pageSize, {
    enabled: variant === "embeddedEmployee" && !!ownerId
  });
  const studentResults = useEventsByStudentQuery(ownerId!, currentPage, pageSize, {
    enabled: variant === "embeddedStudent" && !!ownerId
  });

  const { data: events, isLoading, error } =
    variant === "eventsPage" ? allResults
      : variant === "embeddedEmployee" ? employeeResults
        : studentResults;

  const showPrice = variant === "eventsPage" || variant === "embeddedStudent"
  const showPayment = variant === "embeddedEmployee"

  if (isLoading) {
    return <LoadingCard description="Carregando eventos..." />
  }

  if (events?.page.totalElements === 0) {
    return <EmptyCard description="Cadastre um evento para ele aparecer por aqui." title={"Nenhum evento encontrado."} />
  }

  if (error) {
    return <ErrorCard description={error.message} title="Erro ao carregar eventos" />
  }

  return (
    <div className="app-table-wrap min-h-[500px]">
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
          {events?.content.map((event) => (
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
      <Pagination
        currentPage={currentPage}
        totalElements={events?.page.totalElements ?? 0}
        totalPages={events?.page.totalPages ?? 0}
        currentElementsCount={events?.content.length ?? 0}
        itemName="eventos"
        onPageChange={setCurrentPage}
      />
    </div>
  )
}
