import { BrowserRouter, Routes, Route } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"
import { DashboardPage } from "@/features/dashboard/DashboardPage"
import { StudentsPage } from "@/features/students/StudentsPage"
import { EmployeesPage } from "@/features/employees/EmployeesPage"
import { EventsPage } from "@/features/events/EventsPage"
import { ParentsPage } from "@/features/parents/ParentsPage"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/students" element={<StudentsPage />} />
          <Route path="/employees" element={<EmployeesPage />} />
          <Route path="/events" element={<EventsPage />} />
          <Route path="/parents" element={<ParentsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
