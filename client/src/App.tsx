import { MainLayout } from "@/components/layout/MainLayout"
import { PageLoading } from "@/components/ui/page-loading"
import { Suspense, lazy } from "react"
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/ui/error-boundary"

const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((module) => ({ default: module.DashboardPage })))

const StudentsPage = lazy(() => import("@/features/students/pages/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentDetailsPage = lazy(() => import("@/features/students/pages/StudentDetailsPage").then((module) => ({ default: module.StudentDetailsPage })))

const ParentsPage = lazy(() => import("@/features/parents/pages/ParentsPage").then((module) => ({ default: module.ParentsPage })))
const ParentDetailPage = lazy(() => import("@/features/parents/pages/ParentDetailPage").then((module) => ({ default: module.ParentDetailPage })))

const EmployeesPage = lazy(() => import("@/features/employees/pages/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/pages/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))

const EventsPage = lazy(() => import("@/features/events/pages/EventsPage").then((module) => ({ default: module.EventsPage })))
const EventDetailPage = lazy(() => import("@/features/events/pages/EventDetailPage").then((module) => ({ default: module.EventDetailPage })))

const FinancesPage = lazy(() => import("@/features/finance/pages/FinancesPage").then((module) => ({ default: module.FinancesPage })))

const LoginPage = lazy(() => import("@/features/auth/pages/LoginPage").then((module) => ({ default: module.LoginPage })))

const AdminPage = lazy(() => import("@/features/admin/pages/AdminPage").then((module) => ({ default: module.AdminPage })))

import { pt } from "zod/locales"
import z from "zod/v4"

function App() {
  z.config(pt())

  return (
    <BrowserRouter>
      <Toaster position="top-right" richColors />
      <ErrorBoundary>
        <Suspense fallback={<PageLoading message="Carregando tela..." />}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />

            <Route
              element={(
                <MainLayout />
              )}
            >
              <Route path="/" element={<DashboardPage />} />

              <Route path="/students" element={<StudentsPage />} />
              <Route path="/students/:id" element={<StudentDetailsPage />} />

              <Route path="/employees" element={<EmployeesPage />} />
              <Route path="/employees/:id" element={<EmployeeDetailPage />} />

              <Route path="/events" element={<EventsPage />} />
              <Route path="/events/:id" element={<EventDetailPage />} />

              <Route path="/parents" element={<ParentsPage />} />
              <Route path="/parents/:id" element={<ParentDetailPage />} />

              <Route path="/finance" element={<FinancesPage />} />
              <Route path="/finance/expenses" element={<FinancesPage />} />
              <Route path="/finance/settlement" element={<FinancesPage />} />

              <Route path="/admin" element={<AdminPage />} />
              <Route
                path="/admin/users"
                element={<AdminPage />}
              />
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Suspense>
      </ErrorBoundary>
    </BrowserRouter>
  )
}

export default App
