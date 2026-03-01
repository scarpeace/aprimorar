import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Employee Details</h1>
          <p className="text-sm text-gray-600">Employee ID: {id}</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">Back to Employees</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Profile</CardTitle>
          <CardDescription>This page will show full employee information once API integration is added.</CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-gray-600">Placeholder detail view for User Story 3.2 implementation.</p>
        </CardContent>
      </Card>
    </div>
  )
}
