import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
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
import type { EventResponse } from "@/lib/schemas"
import { eventsApi, type PageResponse } from "@/services/api"

export function EventsPage() {
  const [eventList, setEventList] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const eventsRes = await eventsApi.list()
        const eventsPage: PageResponse<EventResponse> = eventsRes.data
        setEventList(eventsPage.content)
      } catch (error) {
        console.error("Failed to fetch events:", error)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [])

  if (loading) {
    return <div>Loading...</div>
  }

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
            {eventList.map((event) => (
              <TableRow key={event.id}>
                <TableCell>{event.title}</TableCell>
                <TableCell>{event.studentName}</TableCell>
                <TableCell>{event.employeeName}</TableCell>
                <TableCell>{event.startDateTime}</TableCell>
                <TableCell>{event.price}</TableCell>
                <TableCell>
                  <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                    Details
                  </Link>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {eventList.length === 0 ? (
        <EmptyState
          title="No events yet"
          description="When you add your first event, it will appear in the table above."
          actionLabel="Add Event"
        />
      ) : null}
    </div>
  )
}
