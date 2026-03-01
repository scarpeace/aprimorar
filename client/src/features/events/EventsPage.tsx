import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"

export function EventsPage() {
  const events: Array<{
    id: string
    title: string
    student: string
    employee: string
    dateTime: string
    price: string
  }> = []

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Events</h1>
          <p className="text-sm text-gray-600">Manage schedules, prices, and assignments.</p>
        </div>
        <Button type="button">Add Event</Button>
      </div>

      <div className="rounded-md border bg-white">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Title</TableHead>
              <TableHead>Student</TableHead>
              <TableHead>Employee</TableHead>
              <TableHead>Date/Time</TableHead>
              <TableHead>Price</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {events.map((event) => (
              <TableRow key={event.id}>
                <TableCell>{event.title}</TableCell>
                <TableCell>{event.student}</TableCell>
                <TableCell>{event.employee}</TableCell>
                <TableCell>{event.dateTime}</TableCell>
                <TableCell>{event.price}</TableCell>
                <TableCell>Details</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {events.length === 0 ? (
        <EmptyState
          title="No events yet"
          description="When you add your first event, it will appear in the table above."
          actionLabel="Add Event"
        />
      ) : null}
    </div>
  )
}
