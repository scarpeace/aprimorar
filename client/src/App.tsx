import { MainLayout } from "@/components/Layout/MainLayout"
import { ProtectedRoute } from "@/auth/components/ProtectedRoute.tsx"
import { AdminOnly } from "@/auth/components/AdminOnly.tsx"
import { PageLoading } from "@/components/page-loading.tsx"
import { Suspense, lazy } from "react"
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./components/error-boundary.tsx"

const DashboardPage = lazy(() => import("@/pages/Dashboard/DashboardPage.tsx").then((module) => ({ default: module.DashboardPage })))

const AlunosPage = lazy(() => import("@/pages/Aluno/AlunosPage.tsx").then((module) => ({ default: module.AlunosPage })))
const AlunoDetailPage = lazy(() => import("@/pages/Aluno/AlunoDetailPage.tsx").then((module) => ({ default: module.AlunoDetailPage })))

const ResponsaveisPage = lazy(() => import("@/pages/Responsavel/ResponsaveisPage.tsx").then((module) => ({ default: module.ResponsaveisPage })))
const ResponsavelDetailPage = lazy(() => import("@/pages/Responsavel/ResponsavelDetailPage.tsx").then((module) => ({ default: module.ResponsavelDetailPage })))

const ColaboradoresPage = lazy(() => import("@/pages/Colaborador/ColaboradoresPage.tsx").then((module) => ({ default: module.ColaboradoresPage })))
const ColaboradorDetailPage = lazy(() => import("@/pages/Colaborador/ColaboradorDetailPage.tsx").then((module) => ({ default: module.ColaboradorDetailPage })))

const AtendimentosPage = lazy(() => import("@/pages/Atendimento/AtendimentosPage.tsx").then((module) => ({ default: module.AtendimentosPage })))
const AtendimentoDetailPage = lazy(() => import("@/pages/Atendimento/AtendimentoDetailPage.tsx").then((module) => ({ default: module.AtendimentoDetailPage })))

const LoginPage = lazy(() => import("@/auth/pages/Login.tsx").then((module) => ({ default: module.Login })))

const AdminPage = lazy(() => import("@/pages/Admin/AdminPage.tsx").then((module) => ({ default: module.AdminPage })))

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
