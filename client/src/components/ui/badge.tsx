import type { HTMLAttributes } from "react"

type BadgeVariant = "primary" | "warning" | "success" | "error" | "neutral"

type BadgeProps = HTMLAttributes<HTMLDivElement> & {
  variant?: BadgeVariant
}

const VARIANT_CLASSES: Record<BadgeVariant, string> = {
  primary: "badge-primary",
  warning: "badge-warning",
  success: "badge-success",
  error: "badge-error",
  neutral: "badge-neutral",
}

function buildBadgeClassName(variant: BadgeVariant, className?: string) {
  return [
    "badge",
    "badge-soft",
    VARIANT_CLASSES[variant],
    "h-auto",
    "whitespace-normal",
    "text-center",
    "text-xs",
    className,
  ]
    .filter(Boolean)
    .join(" ")
}

export function Badge({ variant = "primary", className, ...props }: BadgeProps) {
  return <div className={buildBadgeClassName(variant, className)} {...props} />
}
