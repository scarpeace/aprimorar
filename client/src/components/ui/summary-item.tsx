import type { ReactNode } from "react"

type SummaryItemProps = {
  label: string
  value: ReactNode
}

export function SummaryItem({ label, value }: SummaryItemProps) {
  return (
    <div className="app-border app-surface-alt rounded-box border p-3">
      <p className="app-text-muted text-xs font-semibold">{label}</p>
      <p className="app-text mt-1 break-words text-sm">{value}</p>
    </div>
  )
}
