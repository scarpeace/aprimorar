import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

export function EmployeeCreatePage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">New Employee</h1>
          <p className="text-sm text-gray-600">Create a new employee record.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">← Back to Employees</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Coming soon</CardTitle>
          <CardDescription>This page is a placeholder for the employee creation form.</CardDescription>
        </CardHeader>
        <CardContent>
          <Button asChild type="button">
            <Link to="/employees">View employees</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}
