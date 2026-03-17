import type { ReactNode } from "react"
import { ButtonLink } from "./button"
import { type LucideIcon } from "lucide-react"

type PageHeaderProps = {
  title: string
  description?: string
  link?: string
  action?: ReactNode
  icon?: LucideIcon
  children?: ReactNode
  titleClassName?: string
}

export function PageHeader({
  title,
  description,
  link,
  action,
  icon: Icon,
  children,
  titleClassName,
}: Readonly<PageHeaderProps>) {
  return (
    <header className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between mb-8">
      <div className="flex items-center gap-4 w-full min-w-0 sm:flex-1">
        {Icon && (
          <div className="shrink-0">
            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-success/15">
              <Icon className="h-6 w-6 text-success" />
            </div>
          </div>
        )}
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
