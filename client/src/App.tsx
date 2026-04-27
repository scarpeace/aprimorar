import { MainLayout } from "@/components/layout/MainLayout"
import { PageLoading } from "@/components/ui/page-loading"
import { AuthGate } from "@/features/auth/components/AuthGate"
import { AdminGuard } from "@/features/admin/components/AdminGuard"
import { useAuthSession } from "@/features/auth/hooks/useAuthSession"
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

const GeneralExpensesPage = lazy(() => import("@/features/finance/pages/GeneralExpensesPage").then((module) => ({ default: module.GeneralExpensesPage })))
const FinanceDashboardPage = lazy(() => import("@/features/finance/pages/FinanceDashboardPage").then((module) => ({ default: module.FinanceDashboardPage })))
const EventSettlementPage = lazy(() => import("@/features/finance/pages/EventSettlementPage").then((module) => ({ default: module.EventSettlementPage })))

const LoginPage = lazy(() => import("@/features/auth/pages/LoginPage").then((module) => ({ default: module.LoginPage })))

const UsersPage = lazy(() => import("@/features/admin/pages/UsersPage").then((module) => ({ default: module.UsersPage })))

import { pt } from "zod/locales"
import z from "zod/v4"

function App() {
  const { isAuthenticated, isCheckingSession } = useAuthSession()

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
                <AuthGate
                  isPending={isCheckingSession}
                  isAuthenticated={isAuthenticated}
                  fallback={<Navigate to="/login" replace />}
                  loadingMessage="Verificando sua sessão..."
                >
                  <MainLayout />
                </AuthGate>
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

              <Route path="/finance" element={<FinanceDashboardPage />} />
              <Route path="/finance/expenses" element={<GeneralExpensesPage />} />
              <Route path="/finance/settlement" element={<EventSettlementPage />} />

              <Route
                path="/admin/users"
                element={
                  <AdminGuard>
                    <UsersPage />
                  </AdminGuard>
                }
              />
            </Route>

            <Route path="*" element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />} />
          </Routes>
        </Suspense>
      </ErrorBoundary>
    </BrowserRouter>
  )
}

export default App
