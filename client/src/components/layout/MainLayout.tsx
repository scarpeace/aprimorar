import {
  Calendar,
  LayoutDashboard,
  UserCog,
  GraduationCap,
  Handshake,
  Banknote,
  ShieldCheck
} from "lucide-react"
import { Link, Outlet, useLocation } from "react-router-dom"
import { DateRangeInput } from "../ui/date-range-input"
import { Button } from "../ui/button"
import { useDateFilter } from "@/hooks/use-date-filter"

const navigation = [
  { name: "Painel", href: "/", icon: LayoutDashboard },
  { name: "Alunos", href: "/students", icon: GraduationCap },
  { name: "Pais e Responsáveis", href: "/parents", icon: Handshake },
  { name: "Colaboradores", href: "/employees", icon: UserCog },
  { name: "Atendimentos", href: "/appointments", icon: Calendar },
  { name: "Financeiro", href: "/finance", icon: Banknote },
  { name: "Admin", href: "/admin", icon: ShieldCheck },
]

function isNavigationActive(currentPath: string, itemHref: string) {
  return currentPath === itemHref || currentPath.startsWith(`${itemHref}/`)
}

export function MainLayout() {
  const location = useLocation()
  const {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    clearDateFilters,
    hasFilters,
  } = useDateFilter()

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
      </aside>
      <main className="app-main-content">
        <div className="app-main-content-inner">
          <div className="mb-5 flex flex-col gap-3 rounded-2xl border border-base-300 bg-base-100 p-3 shadow-sm sm:flex-row sm:items-center sm:justify-between">
            <div>
              <p className="text-[11px] font-bold uppercase tracking-[0.2em] text-base-content/45">
                Periodo global
              </p>
              <p className="text-sm text-base-content/60">
                As paginas financeiras usam este intervalo de datas.
              </p>
            </div>
            <div className="flex w-full flex-col gap-2 sm:w-auto sm:flex-row sm:items-center">
              <DateRangeInput
                startDate={startDate ?? null}
                endDate={endDate ?? null}
                onStartDateChange={handleStartDateChange}
                onEndDateChange={handleEndDateChange}
              />
              {hasFilters ? (
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={clearDateFilters}
                >
                  Limpar
                </Button>
              ) : null}
            </div>
          </div>
          <Outlet/>
        </div>
      </main>
    </div>
  )
}
