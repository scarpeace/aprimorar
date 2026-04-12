import { Link, useLocation, Outlet } from "react-router-dom"
import {
  LayoutDashboard,
  UserCog,
  Calendar,
  GraduationCap,
  Handshake
} from "lucide-react"

const navigation = [
  { name: "Painel", href: "/", icon: LayoutDashboard },
  { name: "Alunos", href: "/students", icon: GraduationCap },
  { name: "Pais e Responsáveis", href: "/parents", icon: Handshake },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Eventos", href: "/events", icon: Calendar },
]


export function MainLayout() {
  const location = useLocation()

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
            const isActive = location.pathname === item.href
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
      </aside>
      <main className="app-main-content">
        <div className="app-main-content-inner">
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
