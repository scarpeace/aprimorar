import type { ReactNode } from "react"

type SectionCardProps = {
  title?: string
  description?: string
  children: ReactNode
  className?: string
  bodyClassName?: string
}

export function SectionCard({ title, description, children, className, bodyClassName }: SectionCardProps) {
  return (
    <section className={`card border app-border app-surface shadow-sm ${className ?? ""}`.trim()}>
      <div className={`card-body ${bodyClassName ?? ""}`.trim()}>
        {title || description ? (
          <div className="mb-2">
            {title ? <h2 className="card-title">{title}</h2> : null}
            {description ? <p className="text-sm app-text-muted">{description}</p> : null}
          </div>
        ) : null}

        {children}
      </div>
    </section>
  )
}
