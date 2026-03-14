import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import type { EventResponse } from "@/lib/schemas/event"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"

type EventsTableVariant = "studentPage" | "eventsPage" | "employeePage"

type EventsTableProps = {
  events: EventResponse[]
  variant?: EventsTableVariant
}

export function EventsTable({ events, variant = "eventsPage" }: EventsTableProps) {
  const isEventsPage = variant === "eventsPage"
  const isStudentPage = variant === "studentPage"
  const isEmployeePage = variant === "employeePage"

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Aluno</TableHead>
          <TableHead>Colaborador</TableHead>
          <TableHead>Data</TableHead>
          <TableHead>Horário</TableHead>
          <TableHead>Conteúdo</TableHead>
          
          {isEventsPage || isStudentPage? <TableHead>Valor</TableHead> : null}
          {isEmployeePage ? <TableHead>Pagamento</TableHead> : null}
          
          <TableHead>Ações</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {events.map((event) => (
          <TableRow key={event.id}>
            <TableCell>{event.studentName}</TableCell>
            <TableCell>{event.employeeName}</TableCell>
            <TableCell>{formatDateShortYear(event.startDate)}</TableCell>
            <TableCell>{formatTime(event.startDate)} às {formatTime(event.endDate)}</TableCell>
            <TableCell>{eventContentLabels[event.content]}</TableCell>
            
            {isEventsPage || isStudentPage ? 
            <TableCell>{brl.format(event.price)}</TableCell> : null}
            
            {isEmployeePage ? 
            <TableCell>{brl.format(event.payment)}</TableCell> : null}
            
            
            <TableCell>
              <Button variant="default" asChild>
                <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                  Detalhes
                </Link>
              </Button>
            </TableCell>
          
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
