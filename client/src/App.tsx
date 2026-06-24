import { MainLayout } from "@/components/Layout/MainLayout"
import { ProtectedRoute } from "@/auth/components/ProtectedRoute.tsx"
import { PageLoading } from "@/components/Ui/PageLoading.tsx"
import { Suspense, lazy } from "react"
import { createBrowserRouter, Navigate, RouterProvider } from "react-router-dom"
import { Toaster } from "sonner"
import { ErrorBoundary } from "./pages/ErrorBoundary.tsx"
import { pt } from "zod/locales"
import z from "zod/v4"

const DashboardPage = lazy(() => import("@/pages/Dashboard/DashboardPage.tsx").then((module) => ({ default: module.DashboardPage })))

const AlunosPage = lazy(() => import("@/pages/Aluno/AlunosPage.tsx").then((module) => ({ default: module.AlunosPage })))
const AlunoDetailPage = lazy(() => import("@/pages/Aluno/AlunoDetailPage.tsx").then((module) => ({ default: module.AlunoDetailPage })))

const ResponsaveisPage = lazy(() => import("@/pages/Responsavel/ResponsaveisPage.tsx").then((module) => ({ default: module.ResponsaveisPage })))
const ResponsavelDetailPage = lazy(() => import("@/pages/Responsavel/ResponsavelDetailPage.tsx").then((module) => ({ default: module.ResponsavelDetailPage })))

const ColaboradoresPage = lazy(() => import("@/pages/Colaborador/ColaboradoresPage.tsx").then((module) => ({ default: module.ColaboradoresPage })))
const ColaboradorDetailPage = lazy(() => import("@/pages/Colaborador/ColaboradorDetailPage.tsx").then((module) => ({ default: module.ColaboradorDetailPage })))

const AtendimentosPage = lazy(() => import("@/pages/Atendimento/AtendimentosPage.tsx").then((module) => ({ default: module.AtendimentosPage })))
const AtendimentoDetailPage = lazy(() => import("@/pages/Atendimento/AtendimentoDetailPage.tsx").then((module) => ({ default: module.AtendimentoDetailPage })))

const FinanceiroPage = lazy(() => import("@/pages/Financeiro/FinanceiroPage.tsx").then((module) => ({ default: module.FinanceiroPage })))
const TransacaoDetailPage = lazy(() => import("@/pages/Financeiro/TransacaoDetailPage.tsx").then((module) => ({ default: module.TransacaoDetailPage })))

const LoginPage = lazy(() => import("@/auth/pages/Login.tsx").then((module) => ({ default: module.Login })))

const AdminPage = lazy(() => import("@/pages/Admin/AdminPage.tsx").then((module) => ({ default: module.AdminPage })))

const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <MainLayout />,
        children: [
          { index: true, element: <DashboardPage /> },
          { path: "alunos", element: <AlunosPage /> },
          { path: "alunos/:id", element: <AlunoDetailPage /> },
          { path: "colaboradores", element: <ColaboradoresPage /> },
          { path: "colaboradores/:id", element: <ColaboradorDetailPage /> },
          { path: "atendimentos", element: <AtendimentosPage /> },
          { path: "atendimentos/:id", element: <AtendimentoDetailPage /> },
          { path: "responsaveis", element: <ResponsaveisPage /> },
          { path: "responsaveis/:id", element: <ResponsavelDetailPage /> },
          { path: "financeiro", element: <FinanceiroPage /> },
          { path: "transacoes/:id", element: <TransacaoDetailPage /> },
        ],
      },
    ],
  },
  {
    element: <ProtectedRoute allowedRoles={["ROLE_ADMIN"]} />,
    children: [
      {
        element: <MainLayout />,
        children: [
          { path: "admin", element: <AdminPage /> },
          // { path: "admin/users", element: <AdminPage /> },
        ],
      },
    ],
  },
  { path: "*", element: <Navigate to="/" replace /> },
]);

function App() {
  z.config(pt())

  return (
    <>
      <Toaster position="top-right" richColors />
      <ErrorBoundary>
        <Suspense fallback={<PageLoading message="Carregando tela..." />}>
          <RouterProvider router={router} />
        </Suspense>
      </ErrorBoundary>
    </>
  )
}

export default App
