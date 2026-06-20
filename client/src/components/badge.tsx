import type { HTMLAttributes } from "react"

type BadgeVariant = "primary" | "warning" | "success" | "error" | "neutral" | "ghost" | "info"

type BadgeProps = HTMLAttributes<HTMLDivElement> & {
  variant?: BadgeVariant
}

export function Badge({ variant = "primary", className = "", ...props }: BadgeProps) {
  return (
    <div
      className={`badge badge-${variant} h-auto whitespace-normal text-center text-xs ${className}`}
      {...props}
    />
  )
}
