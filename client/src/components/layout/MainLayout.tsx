import { useQueryClient } from "@tanstack/react-query"
import {
  Calendar,
  LayoutDashboard,
  LogOut,
  UserCog,
  GraduationCap,
  Handshake
} from "lucide-react"
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom"

import { Button } from "@/components/ui/button"
import { useAuthSession } from "@/features/auth/hooks/useAuthSession"
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors"
import { clearProtectedQueryCache } from "@/lib/shared/queryClient"
import { toast } from "sonner"

const navigation = [
  { name: "Painel", href: "/", icon: LayoutDashboard },
  { name: "Alunos", href: "/students", icon: GraduationCap },
  { name: "Pais e Responsáveis", href: "/parents", icon: Handshake },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Atendimentos", href: "/events", icon: Calendar },
]

const dutyLabels = {
  TEACHER: "Professor(a)",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor(a)",
  SYSTEM: "Sistema",
} as const

function isNavigationActive(currentPath: string, itemHref: string) {
  return currentPath === itemHref || currentPath.startsWith(`${itemHref}/`)
}

export function MainLayout() {
  const location = useLocation()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { currentUser, logout } = useAuthSession()

  const dutyLabel = currentUser ? dutyLabels[currentUser.duty] : "Equipe interna"

  const handleLogout = async () => {
    try {
      await logout.mutateAsync()
      await clearProtectedQueryCache(queryClient)
      navigate("/login", { replace: true })
    } catch (error) {
      toast.error(getFriendlyErrorMessage(error) || "Não foi possível encerrar a sessão. Tente novamente.")
    }
  }

  return (
    <div className="app-main-layout">
      <aside className="app-main-sidebar">
        <div className="app-main-brand">
          <div>
            <h1 className="app-text text-xl font-extrabold tracking-tight">Aprimorar</h1>
            <p className="app-main-brand-caption">Gestão educacional</p>
          </div>
        </div>
        <nav className="app-main-nav">
          {navigation.map((item) => {
            const isActive = isNavigationActive(location.pathname, item.href)
            return (
              <Link
                key={item.name}
                to={item.href}
                className={`app-main-nav-link ${isActive ? "app-main-nav-link-active" : ""}`.trim()}
              >
                <item.icon className="h-5 w-5" />
                {item.name}
              </Link>
            )
          })}
        </nav>
        <div className="border-t border-base-300/70 px-4 py-4">
          <div className="mb-4 rounded-2xl border border-base-300/70 bg-base-100/70 p-4 shadow-sm">
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-base-content/50">Sessão ativa</p>
            <p className="mt-3 text-base font-semibold text-base-content">{currentUser?.displayName ?? "Usuário autenticado"}</p>
            <p className="text-sm text-base-content/70">{dutyLabel}</p>
            <p className="mt-1 text-xs text-base-content/60">{currentUser?.email ?? ""}</p>
          </div>

          <Button
            type="button"
            variant="outlineDanger"
            className="w-full justify-between"
            onClick={handleLogout}
            disabled={logout.isPending}
          >
            {logout.isPending ? "Saindo..." : "Sair"}
            <LogOut className="h-4 w-4" />
          </Button>
        </div>
      </aside>
      <main className="app-main-content">
        <div className="app-main-content-inner">
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
