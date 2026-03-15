import type { ReactNode } from "react"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  children: ReactNode
  className?: string
}

export function Alert({ variant = "info", children, className = "" }: Readonly<AlertProps>) {
  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      <span>{children}</span>
    </div>
  )
}
