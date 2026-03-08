import { lazy, Suspense } from "react"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"


const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((module) => ({ default: module.DashboardPage })))
const StudentsPage = lazy(() => import("@/features/students/pages/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentDetailPage = lazy(() => import("@/features/students/pages/StudentDetailPage").then((module) => ({ default: module.StudentDetailPage })))
const StudentCreatePage = lazy(() => import("@/features/students/pages/StudentCreatePage").then((module) => ({ default: module.StudentCreatePage })))
const StudentEditPage = lazy(() => import("@/features/students/pages/StudentEditPage").then((module) => ({ default: module.StudentEditPage })))
const EmployeesPage = lazy(() => import("@/features/employees/pages/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/pages/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))
const EmployeeCreatePage = lazy(() => import("@/features/employees/pages/EmployeeCreatePage").then((module) => ({ default: module.EmployeeCreatePage })))
const EventsPage = lazy(() => import("@/features/events/pages/EventsPage").then((module) => ({ default: module.EventsPage })))
const EventDetailPage = lazy(() => import("@/features/events/pages/EventDetailPage").then((module) => ({ default: module.EventDetailPage })))
const EventCreatePage = lazy(() => import("@/features/events/pages/EventCreatePage").then((module) => ({ default: module.EventCreatePage })))
const EventEditPage = lazy(() => import("@/features/events/pages/EventEditPage").then((module) => ({ default: module.EventEditPage })))

function withPageFallback(page: React.ReactNode) {
  return <Suspense fallback={<PageLoadingState label="Carregando página..." />}>{page}</Suspense>
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}> 
          <Route path="/" element={withPageFallback(<DashboardPage />)} />
          <Route path="/students" element={withPageFallback(<StudentsPage />)} />
          <Route path="/students/new" element={withPageFallback(<StudentCreatePage />)} />
          <Route path="/students/:id/edit" element={withPageFallback(<StudentEditPage />)} />
          <Route path="/students/:id" element={withPageFallback(<StudentDetailPage />)} />
          <Route path="/employees" element={withPageFallback(<EmployeesPage />)} />
          <Route path="/employees/new" element={withPageFallback(<EmployeeCreatePage />)} />
          <Route path="/employees/:id" element={withPageFallback(<EmployeeDetailPage />)} />
          <Route path="/events" element={withPageFallback(<EventsPage />)} />
          <Route path="/events/new" element={withPageFallback(<EventCreatePage />)} />
          <Route path="/events/:id/edit" element={withPageFallback(<EventEditPage />)} />
          <Route path="/events/:id" element={withPageFallback(<EventDetailPage />)} />

        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
