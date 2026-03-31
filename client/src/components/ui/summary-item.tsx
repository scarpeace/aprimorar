import type { ReactNode } from "react"

type SummaryItemProps = {
  label: string
  value: ReactNode
}

export function SummaryItem({ label, value }: SummaryItemProps) {
  return (
    <div className="border-base-300 bg-base-200 rounded-box border p-3">
      <p className="text-base-content/70 text-xs font-semibold">{label}</p>
      <p className="text-base-content mt-1 break-words text-sm">{value}</p>
    </div>
  )
}
