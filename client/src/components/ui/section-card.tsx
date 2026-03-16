import type { ReactNode } from "react"

type SectionCardProps = {
  title?: string
  description?: string
  headerAction?: ReactNode
  children: ReactNode
  className?: string
  bodyClassName?: string
}

export function SectionCard({ title, description, headerAction, children, className, bodyClassName }: SectionCardProps) {
  return (
    <section className={`card border app-border app-surface shadow-sm ${className ?? ""}`.trim()}>
      <div className={`card-body ${bodyClassName ?? ""}`.trim()}>
        {title || description ? (
          <div className="mb-2 flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
            <div>
              {title ? <h2 className="card-title">{title}</h2> : null}
              {description ? <p className="text-sm app-text-muted">{description}</p> : null}
            </div>
            {headerAction ? <div className="shrink-0">{headerAction}</div> : null}
          </div>
        ) : null}

        {children}
      </div>
    </section>
  )
}
