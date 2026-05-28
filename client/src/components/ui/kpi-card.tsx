import type { LucideIcon } from "lucide-react"
import type { ReactNode } from "react"

interface KpiCardProps {
  label: string
  value: number | ReactNode
  className?: string
  Icon: LucideIcon
}

export function KpiCard({ label, value, className, Icon }: KpiCardProps) {
  return (
    <div className={`card border border-base-300 rounded-xl bg-base-100 shadow-sm transition-all hover:shadow-md ${className ?? ""}`}>
      <div className="flex items-center p-3 gap-3">
        <div className="p-3 rounded-xl bg-primary/10">
          <Icon size={24} />
        </div>
        <div className="flex flex-col">
          <h2 className="text-xs sm:text-sm font-semibold uppercase opacity-60">{label}</h2>
          <span className="text-xl sm:text-3xl font-bold">{value}</span>
        </div>
      </div>
    </div>
  )
}
