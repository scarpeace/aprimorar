import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>()

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Event Details</h1>
          <p className="text-sm text-gray-600">Event ID: {id}</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">Back to Events</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Session</CardTitle>
          <CardDescription>This page will show full event information once API integration is added.</CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-gray-600">Placeholder detail view for User Story 3.3 implementation.</p>
        </CardContent>
      </Card>
    </div>
  )
}
