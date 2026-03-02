import { Link, useLocation, Outlet } from "react-router-dom"
import {
  LayoutDashboard,
  UserCog,
  Calendar,
  GraduationCap,
} from "lucide-react"
import { cn } from "@/lib/utils"

const navigation = [
  { name: "Painel", href: "/", icon: LayoutDashboard },
  { name: "Alunos", href: "/students", icon: GraduationCap },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Eventos", href: "/events", icon: Calendar },
]


export function MainLayout() {
  const location = useLocation()

  return (
    <div className="flex h-screen bg-gray-50">
      <aside className="w-64 bg-white border-r border-gray-200">
        <div className="flex h-16 items-center border-b border-gray-200 px-6">
          <h1 className="text-xl font-bold text-gray-900">Aprimorar</h1>
        </div>
        <nav className="space-y-1 px-3 py-4">
          {navigation.map((item) => {
            const isActive = location.pathname === item.href
            return (
              <Link
                key={item.name}
                to={item.href}
                className={cn(
                  "flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium transition-colors",
                  isActive
                    ? "bg-gray-100 text-gray-900"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                )}
              >
                <item.icon className="h-5 w-5" />
                {item.name}
              </Link>
            )
          })}
        </nav>
      </aside>
      <main className="flex-1 overflow-y-auto">
        <div className="p-8">
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
