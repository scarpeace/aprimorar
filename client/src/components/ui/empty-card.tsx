import type { ReactNode } from "react"

type EmptyCardProps = {
  title: string
  description: string
  action?: ReactNode
}

export function EmptyCard({ title, description, action }: EmptyCardProps) {
  return (
    <div className="bg-base-100 border-base-300 card border border-dashed shadow-sm">
      <div className="card-body gap-4">
        <h2 className="card-title">{title}</h2>
        <p className="text-base-content/70 text-sm">{description}</p>
        {action ? <div className="card-actions justify-start">{action}</div> : null}
      </div>
    </div>
  )
}
