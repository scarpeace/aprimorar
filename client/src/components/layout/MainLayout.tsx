import {
  Calendar,
  LayoutDashboard,
  LogOut,
  UserCog,
  GraduationCap,
  Banknote,
  ShieldCheck,
  BellElectric
} from "lucide-react"
import { Link, Outlet, useLocation } from "react-router-dom"
import { useAuth } from "@/features/auth/lib/use-auth"

const baseNavigation = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Alunos e Responsáveis", href: "/students", icon: GraduationCap },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Atendimentos", href: "/appointments", icon: BellElectric },
  { name: "Financeiro", href: "/finance", icon: Banknote },
] as const

function isNavigationActive(currentPath: string, itemHref: string) {
  return currentPath === itemHref || currentPath.startsWith(`${itemHref}/`)
}

export function MainLayout() {
  const location = useLocation()
  const { user, logout } = useAuth()

  const navigation = user?.role === "ADMIN"
    ? [...baseNavigation, { name: "Admin", href: "/admin", icon: ShieldCheck }]
    : baseNavigation

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

        <div className="mt-auto border-t border-base-300 pt-4">
          <div className="mb-3 px-3">
            <p className="text-xs font-semibold text-base-content/60 truncate">{user?.username}</p>
            <p className="text-[10px] uppercase tracking-wider text-base-content/40">{user?.role}</p>
          </div>
          <button
            onClick={logout}
            className="app-main-nav-link w-full flex items-center gap-3 px-3 py-2 text-sm text-base-content/60 hover:text-error hover:bg-error/10 rounded-lg transition-colors"
          >
            <LogOut className="h-5 w-5" />
            Sair
          </button>
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
