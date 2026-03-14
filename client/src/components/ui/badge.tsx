import * as React from "react"

import { cn } from "@/lib/shared/utils"

type BadgeVariant = "default" | "secondary" | "success" | "warn" | "danger" | "outline"

export interface BadgeProps extends React.HTMLAttributes<HTMLSpanElement> {
  variant?: BadgeVariant
}

const Badge = React.forwardRef<HTMLSpanElement, BadgeProps>(
  ({ className, variant = "default", ...props }, ref) => {
    const variantClasses = {
      default: "bg-primary text-primary-foreground",
      secondary: "bg-secondary text-secondary-foreground",
      success: "bg-success text-success-foreground",
      warn: "bg-warn text-warn-foreground",
      danger: "bg-danger text-danger-foreground",
      outline: "border border-input text-foreground",
    }

    return (
      <span
        ref={ref}
        className={cn(
          "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold transition-colors",
          variantClasses[variant],
          className
        )}
        {...props}
      />
    )
  }
)

Badge.displayName = "Badge"

export { Badge }
