import { Suspense, lazy } from "react"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"
import { LoadingState } from "@/components/ui/loading-state"

const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((module) => ({ default: module.DashboardPage })))
const StudentsPage = lazy(() => import("@/features/students/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentDetailPage = lazy(() => import("@/features/students/StudentDetailPage").then((module) => ({ default: module.StudentDetailPage })))
const StudentCreatePage = lazy(() => import("@/features/students/StudentCreatePage").then((module) => ({ default: module.StudentCreatePage })))
const EmployeesPage = lazy(() => import("@/features/employees/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))
const EmployeeCreatePage = lazy(() => import("@/features/employees/EmployeeCreatePage").then((module) => ({ default: module.EmployeeCreatePage })))
const EventsPage = lazy(() => import("@/features/events/EventsPage").then((module) => ({ default: module.EventsPage })))
const EventDetailPage = lazy(() => import("@/features/events/EventDetailPage").then((module) => ({ default: module.EventDetailPage })))
const EventCreatePage = lazy(() => import("@/features/events/EventCreatePage").then((module) => ({ default: module.EventCreatePage })))

function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<LoadingState message="Carregando tela..." />}>
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
      </Suspense>
    </BrowserRouter>
  )
}

export default App
