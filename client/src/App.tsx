import { BrowserRouter, Routes, Route } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"
import { DashboardPage } from "@/features/dashboard/DashboardPage"
import { StudentsPage } from "@/features/students/StudentsPage"
import { StudentDetailPage } from "@/features/students/StudentDetailPage"
import { StudentCreatePage } from "@/features/students/StudentCreatePage"
import { EmployeesPage } from "@/features/employees/EmployeesPage"
import { EmployeeDetailPage } from "@/features/employees/EmployeeDetailPage"
import { EmployeeCreatePage } from "@/features/employees/EmployeeCreatePage"
import { EventsPage } from "@/features/events/EventsPage"
import { EventDetailPage } from "@/features/events/EventDetailPage"
import { EventCreatePage } from "@/features/events/EventCreatePage"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/students" element={<StudentsPage />} />
          <Route path="/students/new" element={<StudentCreatePage />} />
          <Route path="/students/:id" element={<StudentDetailPage />} />
          <Route path="/employees" element={<EmployeesPage />} />
          <Route path="/employees/new" element={<EmployeeCreatePage />} />
          <Route path="/employees/:id" element={<EmployeeDetailPage />} />
          <Route path="/events" element={<EventsPage />} />
          <Route path="/events/new" element={<EventCreatePage />} />
          <Route path="/events/:id" element={<EventDetailPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
