import type { ReactNode } from "react"

type EmptyCardProps = {
  title: string
  description: string
  action?: ReactNode
}

export function EmptyCard({ title, description, action }: EmptyCardProps) {
  return (
    <div className="app-surface app-border card border border-dashed shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title">{title}</h2>
        <p className="app-text-muted text-sm">{description}</p>
        {action ? <div className="card-actions justify-start">{action}</div> : null}
      </div>
    </div>
  )
}
