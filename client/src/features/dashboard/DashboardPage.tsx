import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import type { StudentResponse, EmployeeResponse, EventResponse } from "@/lib/schemas"
import { studentsApi, employeesApi, eventsApi, type PageResponse } from "@/services/api"
import { useState, useEffect } from "react"

export function DashboardPage() {

  const [studentsCount, setStudentsCount] = useState(0)
  const [employeesCount, setEmployeesCount] = useState(0)
  const [eventsCount, setEventsCount] = useState(0)
  const [revenue, setRevenue] = useState(0)
  const [loading, setLoading] = useState(true)
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [studentsRes, employeesRes, eventsRes] = await Promise.all([
          studentsApi.listActive(),
          employeesApi.listActive(),
          eventsApi.list(),
        ])

        const studentsPage: PageResponse<StudentResponse> = studentsRes.data
        const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data
        const eventsPage: PageResponse<EventResponse> = eventsRes.data
        console.log(studentsPage);

        setStudentsCount(studentsPage.totalElements)
        setEmployeesCount(employeesPage.totalElements)
        setEventsCount(eventsPage.totalElements)
        // Calculate revenue from events
        const total = eventsPage.content.reduce(
          (sum, event) => sum + Number(event.payment),
          0
        )
        setRevenue(total)
      } catch (error) {
        console.error("Failed to fetch dashboard data:", error)
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
      <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Students</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{studentsCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Employees</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{employeesCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Upcoming Events</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{eventsCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Revenue</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">R$ {revenue}</div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
