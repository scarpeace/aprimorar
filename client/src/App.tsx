import { MainLayout } from "@/components/layout/MainLayout"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"
import { AdminOnly } from "@/components/auth/AdminOnly"
import { PageLoading } from "@/components/ui/page-loading"
import { Suspense, lazy } from "react"
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/ui/error-boundary"

const DashboardPage = lazy(() => import("@/features/dashboard/pages/DashboardPage").then((module) => ({ default: module.DashboardPage })))

const StudentsPage = lazy(() => import("@/features/students/pages/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentDetailsPage = lazy(() => import("@/features/students/pages/StudentDetailsPage").then((module) => ({ default: module.StudentDetailsPage })))

const ParentsPage = lazy(() => import("@/features/parents/pages/ParentsPage").then((module) => ({ default: module.ParentsPage })))
const ParentDetailPage = lazy(() => import("@/features/parents/pages/ParentDetailPage").then((module) => ({ default: module.ParentDetailPage })))

const EmployeesPage = lazy(() => import("@/features/employees/pages/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/pages/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))

const AppointmentsPage = lazy(() => import("@/features/appointments/pages/AppointmentsPage").then((module) => ({ default: module.AppointmentsPage })))
const AppointmentDetailPage = lazy(() => import("@/features/appointments/pages/AppointmentDetailPage").then((module) => ({ default: module.AppointmentDetailPage })))

const FinancesPage = lazy(() => import("@/features/finance/pages/FinancesPage").then((module) => ({ default: module.FinancesPage })))
const ExpenseDetailPage = lazy(() => import("@/features/finance/pages/ExpenseDetailPage").then((module) => ({ default: module.ExpenseDetailPage })))

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

            <Route element={<ProtectedRoute />}>
              <Route element={(<MainLayout />)}>
                <Route path="/" element={<DashboardPage />} />

                <Route path="/students" element={<StudentsPage />} />
                <Route path="/students/:id" element={<StudentDetailsPage />} />

                <Route path="/employees" element={<EmployeesPage />} />
                <Route path="/employees/:id" element={<EmployeeDetailPage />} />

                <Route path="/appointments" element={<AppointmentsPage />} />
                <Route path="/appointments/:id" element={<AppointmentDetailPage />} />

                <Route path="/parents" element={<ParentsPage />} />
                <Route path="/parents/:id" element={<ParentDetailPage />} />

                <Route path="/finance" element={<FinancesPage />} />
                <Route path="/finance/expenses" element={<FinancesPage />} />
                <Route path="/finance/expenses/:id" element={<ExpenseDetailPage />} />
                <Route path="/finance/settlement" element={<FinancesPage />} />

                <Route path="/admin" element={<AdminOnly><AdminPage /></AdminOnly>} />
                <Route path="/admin/users" element={<AdminOnly><AdminPage /></AdminOnly>} />
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Suspense>
      </ErrorBoundary>
    </BrowserRouter>
  )
}

export default App
