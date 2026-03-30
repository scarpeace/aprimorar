import type { ReactNode } from "react"

type SectionCardProps = {
  title?: string
  description?: string
  headerAction?: ReactNode
  children: ReactNode
}

export function SectionCard({ title, description, headerAction, children }: Readonly<SectionCardProps>) {
  return (
    <section className={`card border app-border app-surface shadow-sm`}>
      <div className={`card-body`}>
        {title || description ? (
          <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
            <div>
              {title ? <h2 className="card-title">{title}</h2> : null}
              {description ? <p className="text-sm app-text-muted">{description}</p> : null}
            </div>
            {headerAction ? <div className="shrink-0 flex flex-wrap gap-3">{headerAction}</div> : null}
          </div>
        ) : null}

        {children}
      </div>
    </section>
  )
}
