import {
  LayoutDashboard,
  LogOut,
  UserCog,
  GraduationCap,
  ShieldCheck,
  BellElectric
} from "lucide-react"
import { Link, Outlet, useLocation } from "react-router-dom"
import { useAuth } from "@/features/auth/lib/use-auth"

const baseNavigation = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Alunos e Responsáveis", href: "/alunos", icon: GraduationCap },
  { name: "Colaboradores", href: "/colaboradores", icon: UserCog },
  { name: "Atendimentos", href: "/Atendimento", icon: BellElectric },
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
    <div className="min-h-screen w-full flex flex-col md:flex-row overfloww-y-hidden">
      <aside className="border-b border-base-300 bg-base-100 shadow-sm backdrop-blur md:w-68 md:border-b-0 md:border-r">
        <div className="flex min-h-18 items-center border-b border-base-300 px-5">
          <div>
            <h1 className="text-xl font-extrabold tracking-tight text-base-content">Aprimorar</h1>
            <p className="mt-0.5 text-xs font-semibold uppercase tracking-wider text-base-content/60">Gestão educacional</p>
          </div>
        </div>
        <nav className="flex gap-2 overflow-x-auto p-4 md:flex-col md:overflow-x-visible">
          {navigation.map((item) => {
            const isActive = isNavigationActive(location.pathname, item.href)
            return (
              <Link
                key={item.name}
                to={item.href}
                className={`flex items-center gap-3 whitespace-nowrap rounded-lg border border-transparent px-3 py-2 text-sm font-semibold text-base-content/70 transition hover:border-base-300 hover:bg-base-200 hover:text-base-content ${isActive ? "border-primary/25 bg-base-200 text-base-content shadow-md" : ""}`.trim()}
              >
                <item.icon className="h-5 w-5" />
                {item.name}
              </Link>
            )
          })}
        </nav>

        <div className="mt-auto border-t border-base-300 p-3">
          <div className="mb-3 px-3">
            <p className="text-xs font-semibold text-base-content/60 truncate">{user?.username}</p>
            <p className="text-xs uppercase tracking-wider text-base-content/40"><strong>{user?.role}</strong></p>
          </div>
          <button
            onClick={logout}
            className="w-full rounded-lg border border-error/30 bg-error/10 p-3 text-sm font-semibold text-error transition hover:border-error/30 hover:cursor-pointer hover:bg-error/30 hover:text-error"
          >
            <span className="flex items-center gap-3">
              <LogOut className="h-5 w-5" />
              Sair
            </span>
          </button>
        </div>
      </aside>
      <main className="min-w-0 w-full min-h-screen">
        <div className="mx-auto min-w-0 w-full px-4 py-5 md:px-8 md:py-7">
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
