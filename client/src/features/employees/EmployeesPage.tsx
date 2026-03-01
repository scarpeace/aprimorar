import { useEffect, useState } from "react"
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
import type { EmployeeResponse } from "@/lib/schemas"
import { employeesApi, type PageResponse } from "@/services/api"

export function EmployeesPage() {
  const [employeeList, setEmployeeList] = useState<EmployeeResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const employeesRes = await employeesApi.list()
        const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data
        setEmployeeList(employeesPage.content)
      } catch (error) {
        console.error("Failed to fetch employees:", error)
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
          <h1 className="text-3xl font-bold text-gray-900">Employees</h1>
          <p className="text-sm text-gray-600">Manage teachers and staff accounts.</p>
        </div>
        <Button type="button">Add Employee</Button>
      </div>

      <div className="rounded-md border bg-white">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Role</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>PIX</TableHead>
              <TableHead>Active</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {employeeList.map((employee) => (
              <TableRow key={employee.id}>
                <TableCell>{employee.name}</TableCell>
                <TableCell>{employee.role}</TableCell>
                <TableCell>{employee.email}</TableCell>
                <TableCell>{employee.pix}</TableCell>
                <TableCell>{employee.active ? "Yes" : "No"}</TableCell>
                <TableCell>Details</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {employeeList.length === 0 ? (
        <EmptyState
          title="No employees yet"
          description="When you add your first employee, they will appear in the table above."
          actionLabel="Add Employee"
        />
      ) : null}
    </div>
  )
}
