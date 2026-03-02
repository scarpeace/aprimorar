import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

export function StudentCreatePage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">New Student</h1>
          <p className="text-sm text-gray-600">Create a new student record.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">← Back to Students</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Coming soon</CardTitle>
          <CardDescription>This page is a placeholder for the student creation form.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button asChild type="button">
            <Link to="/students">View students</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}
