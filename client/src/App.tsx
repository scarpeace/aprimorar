import { MainLayout } from "@/components/layout/MainLayout"
import { PageLoading } from "@/components/ui/page-loading"
import { AuthGate } from "@/features/auth/components/AuthGate"
import { useAuthSession } from "@/features/auth/hooks/useAuthSession"
import { Suspense, lazy } from "react"
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/ui/error-boundary"

const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((module) => ({ default: module.DashboardPage })))

const StudentsPage = lazy(() => import("@/features/students/pages/StudentsPage").then((module) => ({ default: module.StudentsPage })))
const StudentCreatePage = lazy(() => import("@/features/students/pages/StudentCreatePage").then((module) => ({ default: module.StudentCreatePage })))
const StudentEditPage = lazy(() => import("@/features/students/pages/StudentEditPage").then((module) => ({ default: module.StudentEditPage })))
const StudentDetailsPage = lazy(() => import("@/features/students/pages/StudentDetailsPage").then((module) => ({ default: module.StudentDetailsPage })))

const ParentsPage = lazy(() => import("@/features/parents/pages/ParentsPage").then((module) => ({ default: module.ParentsPage })))
const ParentCreatePage = lazy(() => import("@/features/parents/pages/ParentCreatePage").then((module) => ({ default: module.ParentCreatePage })))
const ParentDetailPage = lazy(() => import("@/features/parents/pages/ParentDetailPage").then((module) => ({ default: module.ParentDetailPage })))
const ParentEditPage = lazy(() => import("@/features/parents/pages/ParentEditPage").then((module) => ({ default: module.ParentEditPage })))

const EmployeesPage = lazy(() => import("@/features/employees/pages/EmployeesPage").then((module) => ({ default: module.EmployeesPage })))
const EmployeeDetailPage = lazy(() => import("@/features/employees/pages/EmployeeDetailPage").then((module) => ({ default: module.EmployeeDetailPage })))
const EmployeeCreatePage = lazy(() => import("@/features/employees/pages/EmployeeCreatePage").then((module) => ({ default: module.EmployeeCreatePage })))
const EmployeeEditPage = lazy(() => import("@/features/employees/pages/EmployeeEditPage").then((module) => ({ default: module.EmployeeEditPage })))

const EventsPage = lazy(() => import("@/features/events/pages/EventsPage").then((module) => ({ default: module.EventsPage })))
const EventDetailPage = lazy(() => import("@/features/events/pages/EventDetailPage").then((module) => ({ default: module.EventDetailPage })))
const EventCreatePage = lazy(() => import("@/features/events/pages/EventCreatePage").then((module) => ({ default: module.EventCreatePage })))
const EventEditPage = lazy(() => import("@/features/events/pages/EventEditPage").then((module) => ({ default: module.EventEditPage })))

const GeneralExpensesPage = lazy(() => import("@/features/finance/pages/GeneralExpensesPage").then((module) => ({ default: module.GeneralExpensesPage })))
const FinanceDashboardPage = lazy(() => import("@/features/finance/pages/FinanceDashboardPage").then((module) => ({ default: module.FinanceDashboardPage })))
const EventSettlementPage = lazy(() => import("@/features/finance/pages/EventSettlementPage").then((module) => ({ default: module.EventSettlementPage })))

const LoginPage = lazy(() => import("@/features/auth/pages/LoginPage").then((module) => ({ default: module.LoginPage })))

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
              <Route path="/students/new" element={<StudentCreatePage />} />
              <Route path="/students/edit/:id" element={<StudentEditPage />} />
              <Route path="/students/:id" element={<StudentDetailsPage />} />

              <Route path="/employees" element={<EmployeesPage />} />
              <Route path="/employees/:id" element={<EmployeeDetailPage />} />

              <Route path="/events" element={<EventsPage />} />
              <Route path="/events/new" element={<EventCreatePage />} />
              <Route path="/events/:id" element={<EventDetailPage />} />
              <Route path="/events/edit/:id" element={<EventEditPage />} />

              <Route path="/parents" element={<ParentsPage />} />
              <Route path="/parents/new" element={<ParentCreatePage/>} />
              <Route path="/parents/:id" element={<ParentDetailPage />} />
              <Route path="/parents/edit/:id" element={<ParentEditPage />} />

              <Route path="/finance" element={<FinanceDashboardPage />} />
              <Route path="/finance/expenses" element={<GeneralExpensesPage />} />
              <Route path="/finance/settlement" element={<EventSettlementPage />} />
            </Route>

            <Route path="*" element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />} />
          </Routes>
        </Suspense>
      </ErrorBoundary>
    </BrowserRouter>
  )
}

export default App
