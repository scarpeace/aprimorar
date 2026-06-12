import { MainLayout } from "@/components/layout/MainLayout"
import { ProtectedRoute } from "@/components/auth/ProtectedRoute"
import { AdminOnly } from "@/components/auth/AdminOnly"
import { PageLoading } from "@/components/ui/page-loading"
import { Suspense, lazy } from "react"
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/ui/error-boundary"

const DashboardPage = lazy(() => import("@/features/dashboard/pages/DashboardPage").then((module) => ({ default: module.DashboardPage })))

const AlunosPage = lazy(() => import("@/features/alunos/pages/AlunosPage").then((module) => ({ default: module.AlunosPage })))
const AlunoDetailPage = lazy(() => import("@/features/alunos/pages/AlunoDetailPage").then((module) => ({ default: module.AlunoDetailPage })))

const ResponsaveisPage = lazy(() => import("@/features/responsaveis/pages/ResponsaveisPage").then((module) => ({ default: module.ResponsaveisPage })))
const ResponsavelDetailPage = lazy(() => import("@/features/responsaveis/pages/ResponsavelDetailPage").then((module) => ({ default: module.ResponsavelDetailPage })))

const ColaboradoresPage = lazy(() => import("@/features/colaboradores/pages/ColaboradoresPage").then((module) => ({ default: module.ColaboradoresPage })))
const ColaboradorDetailPage = lazy(() => import("@/features/colaboradores/pages/ColaboradorDetailPage").then((module) => ({ default: module.ColaboradorDetailPage })))

const AtendimentosPage = lazy(() => import("@/features/atendimentos/pages/AtendimentosPage").then((module) => ({ default: module.AtendimentosPage })))
const AtendimentoDetailPage = lazy(() => import("@/features/atendimentos/pages/AtendimentoDetailPage").then((module) => ({ default: module.AtendimentoDetailPage })))

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

                <Route path="/alunos" element={<AlunosPage />} />
                <Route path="/alunos/:id" element={<AlunoDetailPage />} />

                <Route path="/colaboradores" element={<ColaboradoresPage />} />
                <Route path="/colaboradores/:id" element={<ColaboradorDetailPage />} />

                <Route path="/atendimentos" element={<AtendimentosPage />} />
                <Route path="/atendimentos/:id" element={<AtendimentoDetailPage />} />

                <Route path="/responsaveis" element={<ResponsaveisPage />} />
                <Route path="/responsaveis/:id" element={<ResponsavelDetailPage />} />

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
