import type { ReactNode } from "react"
import { type LucideIcon } from "lucide-react"

type PageHeaderProps = {
  title: string
  description?: string
  action?: ReactNode
  leading?: ReactNode
  Icon?: React.ElementType
  iconClassName?: string
  iconBgClassName?: string
  children?: ReactNode
  titleClassName?: string
}

export function PageHeader({
  title,
  description,
  action,
  leading,
  Icon,
  iconClassName,
  iconBgClassName,
  children,
  titleClassName,
}: Readonly<PageHeaderProps>) {
  return (
    <header className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between mb-3">
      <div className="flex items-center gap-4 w-full min-w-0 sm:flex-1">
        {Icon ? (
          <div className={`flex h-26 w-30 items-center justify-center rounded-full ${iconBgClassName || "bg-success/15"}`} >
            <Icon className={`h-16 w-16 ${iconClassName || "text-success"}`} />
          </div>
        ) : null}
        <div className="w-full min-w-0">
          <h1 className={`${titleClassName ?? "text-3xl font-bold app-text"}`.trim()}>{title}</h1>
          {description ? <p className="text-sm app-text-muted">{description}</p> : null}
          {children ? <div className="mt-3">{children}</div> : null}
        </div>
      </div>

      {action && (
        <div className="shrink-0 flex gap-3">
          {action}
        </div>
      )}
    </header>
  )
}
