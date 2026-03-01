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
import type { StudentResponse } from "@/lib/schemas"
import { studentsApi, type PageResponse } from "@/services/api"

export function StudentsPage() {
  const [studentList, setStudentList] = useState<StudentResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const studentsRes = await studentsApi.list()
        const studentsPage: PageResponse<StudentResponse> = studentsRes.data
        setStudentList(studentsPage.content)
      } catch (error) {
        console.error("Failed to fetch students:", error)
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
          <h1 className="text-3xl font-bold text-gray-900">Students</h1>
          <p className="text-sm text-gray-600">Manage student records and enrollments.</p>
        </div>
        <Button type="button">Add Student</Button>
      </div>

      <div className="rounded-md border bg-white">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Activity</TableHead>
              <TableHead>School</TableHead>
              <TableHead>Active</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {studentList.map((student) => (
              <TableRow key={student.id}>
                <TableCell>{student.name}</TableCell>
                <TableCell>{student.email}</TableCell>
                <TableCell>{student.activity}</TableCell>
                <TableCell>{student.school}</TableCell>
                <TableCell>{student.active ? "Yes" : "No"}</TableCell>
                <TableCell>
                  <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/students/${student.id}`}>
                    Details
                  </Link>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {studentList.length === 0 ? (
        <EmptyState
          title="No students yet"
          description="When you add your first student, they will appear in the table above."
          actionLabel="Add Student"
        />
      ) : null}
    </div>
  )
}
