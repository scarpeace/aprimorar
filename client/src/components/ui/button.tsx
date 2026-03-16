import type { ButtonHTMLAttributes, ComponentProps } from "react"
import { Link } from "react-router-dom"

type ButtonVariant =
  | "primary"
  | "secondary"
  | "success"
  | "warning"
  | "error"
  | "danger"
  | "outline"
  | "ghost"
  | "outlinePrimary"
  | "outlineSecondary"
  | "outlineSuccess"
  | "outlineWarning"
  | "outlineError"
  | "outlineDanger"

type ButtonSize = "md" | "sm"

type CommonButtonProps = {
  variant?: ButtonVariant
  size?: ButtonSize
  className?: string
}

const VARIANT_CLASSES: Record<ButtonVariant, string> = {
  primary: "btn-primary",
  secondary: "btn-secondary",
  success: "btn-success",
  warning: "btn-warning",
  error: "btn-error",
  danger: "btn-error",
  outline: "btn-outline",
  ghost: "btn-ghost",
  outlinePrimary: "btn-outline btn-primary",
  outlineSecondary: "btn-outline btn-secondary",
  outlineSuccess: "btn-outline btn-success",
  outlineWarning: "btn-outline btn-warning",
  outlineError: "btn-outline btn-error",
  outlineDanger: "btn-outline btn-error",
}

const SIZE_CLASSES: Record<ButtonSize, string> = {
  md: "",
  sm: "btn-sm",
}

function buildButtonClassName({ variant = "primary", size = "md", className }: CommonButtonProps) {
  return ["btn", VARIANT_CLASSES[variant], SIZE_CLASSES[size], className].filter(Boolean).join(" ")
}

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & CommonButtonProps

export function Button({ variant = "primary", size = "md", className, ...props }: ButtonProps) {
  return <button className={buildButtonClassName({ variant, size, className })} {...props} />
}

export type ButtonLinkProps = ComponentProps<typeof Link> & CommonButtonProps

export function ButtonLink({ variant = "primary", size = "md", className, ...props }: ButtonLinkProps) {
  return <Link className={buildButtonClassName({ variant, size, className })} {...props} />
}
