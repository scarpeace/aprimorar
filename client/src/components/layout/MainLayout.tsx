import { Link, useLocation, Outlet } from "react-router-dom"
import {
  LayoutDashboard,
  UserCog,
  Calendar,
  GraduationCap,
  Handshake
} from "lucide-react"
import styles from "@/components/layout/MainLayout.module.css"

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
    <div className={styles.layout}>
      <aside className={styles.sidebar}>
        <div className={styles.brand}>
          <div>
            <h1 className="app-text text-xl font-extrabold tracking-tight">Aprimorar</h1>
            <p className={styles.brandCaption}>Gestão educacional</p>
          </div>
        </div>
        <nav className={styles.nav}>
          {navigation.map((item) => {
            const isActive = location.pathname === item.href
            return (
              <Link
                key={item.name}
                to={item.href}
                className={`${styles.navLink} ${isActive ? styles.navLinkActive : ""}`.trim()}
              >
                <item.icon className="h-5 w-5" />
                {item.name}
              </Link>
            )
          })}
        </nav>
      </aside>
      <main className={styles.main}>
        <div className={styles.mainInner}>
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
