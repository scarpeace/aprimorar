import type { ReactNode } from "react"

interface KpiCardProps {
  label: string
  value: ReactNode
  className?: string
}

export function KpiCard({ label, value, className }: KpiCardProps) {
  return (
    <div className={`card border border-base-300 bg-base-100 shadow-sm ${className ?? ""}`}>
      <div className="card-body gap-2">
        <h2 className="app-kpi-label">{label}</h2>
        <div className="app-kpi-value">{value}</div>
      </div>
    </div>
  )
}
