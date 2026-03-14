import type { ReactNode } from "react"

type SummaryFieldProps = {
  title: string
  children: ReactNode
}

export function SummaryField({ title, children }: SummaryFieldProps) {
  return (
    <div className="rounded-lg border border-border bg-card p-3">
      <p className="text-xs font-semibold text-muted-foreground">{title}</p>
      <p className="mt-1 break-words text-sm text-foreground">{children}</p>
    </div>
  )
}
