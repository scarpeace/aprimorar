import type { ButtonHTMLAttributes, ComponentProps } from "react"
import { Link } from "react-router-dom"

type ButtonVariant =
  | "primary"
  | "secondary"
  | "success"
  | "warning"
  | "error"
  | "outline"
  | "outlineError"
  | "ghost"

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
  outline: "btn-outline",
  outlineError: "btn-outline btn-error",
  ghost: "btn-ghost",
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
