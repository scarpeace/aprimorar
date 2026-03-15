import type { ReactNode } from "react"

type PageHeaderProps = {
  title: string
  description?: string
  action?: ReactNode
  leading?: ReactNode
  children?: ReactNode
  titleClassName?: string
}

export function PageHeader({
  title,
  description,
  action,
  leading,
  children,
  titleClassName,
}: Readonly<PageHeaderProps>) {
  return (
    <header className="flex flex-col items-start gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div className={`${leading ? "flex items-center gap-4" : ""} w-full min-w-0 sm:flex-1`.trim()}>
        {leading ? <div className="shrink-0">
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-success/15">
            {leading}
          </div>
        </div> : null}
        <div className="w-full min-w-0">
          <h1 className={`${titleClassName ?? "text-3xl font-bold app-text"}`.trim()}>{title}</h1>
          {description ? <p className="text-sm app-text-muted">{description}</p> : null}
          {children ? <div className="mt-3">{children}</div> : null}
        </div>
      </div>

      {action ? <div className="shrink-0">{action}</div> : null}
    </header>
  )
}
