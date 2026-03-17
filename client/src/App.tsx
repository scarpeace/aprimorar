import { Suspense, lazy } from "react"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"
import { PageLoading } from "@/components/ui/page-loading"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/ui/error-boundary"

const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((module) => ({ default: module.DashboardPage })))
const StudentsPage = lazy(() => import("@/features/students/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentDetailPage = lazy(() => import("@/features/students/StudentDetailPage").then((module) => ({ default: module.StudentDetailPage })))
const StudentCreatePage = lazy(() => import("@/features/students/StudentCreatePage").then((module) => ({ default: module.StudentCreatePage })))
const StudentEditPage = lazy(() => import("@/features/students/StudentEditPage").then((module) => ({ default: module.StudentEditPage })))
const EmployeesPage = lazy(() => import("@/features/employees/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))
const EmployeeCreatePage = lazy(() => import("@/features/employees/EmployeeCreatePage").then((module) => ({ default: module.EmployeeCreatePage })))
const EmployeeEditPage = lazy(() => import("@/features/employees/EmployeeEditPage").then((module) => ({ default: module.EmployeeEditPage })))
const EventsPage = lazy(() => import("@/features/events/EventsPage").then((module) => ({ default: module.EventsPage })))
const EventDetailPage = lazy(() => import("@/features/events/EventDetailPage").then((module) => ({ default: module.EventDetailPage })))
const EventCreatePage = lazy(() => import("@/features/events/EventCreatePage").then((module) => ({ default: module.EventCreatePage })))
const EventEditPage = lazy(() => import("@/features/events/EventEditPage").then((module) => ({ default: module.EventEditPage })))
const ParentsPage = lazy(() => import("@/features/parents/ParentsPage").then((module) => ({ default: module.ParentsPage })))
const ParentDetailPage = lazy(() => import("@/features/parents/ParentDetailPage").then((module) => ({ default: module.ParentDetailPage })))
const ParentCreatePage = lazy(() => import("@/features/parents/ParentCreatePage").then((module) => ({ default: module.ParentCreatePage })))
const ParentEditPage = lazy(() => import("@/features/parents/ParentEditPage").then((module) => ({ default: module.ParentEditPage })))

function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" richColors />
      <ErrorBoundary>
        <Suspense fallback={<PageLoading message="Carregando tela..." />}>
          <Routes>
            <Route element={<MainLayout />}>
              <Route path="/" element={<DashboardPage />} />

              <Route path="/students" element={<StudentsPage />} />
              <Route path="/students/new" element={<StudentCreatePage />} />
              <Route path="/students/edit/:id" element={<StudentEditPage />} />
              <Route path="/students/:id" element={<StudentDetailPage />} />

              <Route path="/employees" element={<EmployeesPage />} />
              <Route path="/employees/new" element={<EmployeeCreatePage />} />
              <Route path="/employees/edit/:id" element={<EmployeeEditPage />} />
              <Route path="/employees/:id" element={<EmployeeDetailPage />} />

              <Route path="/events" element={<EventsPage />} />
              <Route path="/events/new" element={<EventCreatePage />} />
              <Route path="/events/edit/:id" element={<EventEditPage />} />
              <Route path="/events/:id" element={<EventDetailPage />} />

              <Route path="/parents" element={<ParentsPage />} />
              <Route path="/parents/new" element={<ParentCreatePage />} />
              <Route path="/parents/:id" element={<ParentDetailPage />} />
              <Route path="/parents/edit/:id" element={<ParentEditPage />} />
            </Route>
          </Routes>
        </Suspense>
      </ErrorBoundary>
    </BrowserRouter>
  )
}

export default App
