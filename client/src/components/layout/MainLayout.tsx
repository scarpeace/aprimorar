import { Link, useLocation, Outlet } from "react-router-dom"
import {
  LayoutDashboard,
  UserCog,
  Calendar,
  GraduationCap,
} from "lucide-react"
import { cn } from "@/lib/utils"
import styles from "@/components/layout/MainLayout.module.css"

const navigation = [
  { name: "Painel", href: "/", icon: LayoutDashboard },
  { name: "Alunos", href: "/students", icon: GraduationCap },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Eventos", href: "/events", icon: Calendar },
]


export function MainLayout() {
  const location = useLocation()

  return (
    <div className={styles.layout}>
      <aside className={styles.sidebar}>
        <div className={styles.brand}>
          <h1 className="text-xl font-bold text-gray-900">Aprimorar</h1>
        </div>
        <nav className={styles.nav}>
          {navigation.map((item) => {
            const isActive = item.href === "/"
              ? location.pathname === item.href
              : location.pathname.startsWith(item.href)
            return (
              <Link
                key={item.name}
                to={item.href}
                className={cn(
                  styles.navLink,
                  isActive ? styles.navLinkActive : undefined
                )}
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
