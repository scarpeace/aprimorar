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
  | "primary-outline"
  | "secondary-outline"
  | "success-outline"
  | "warning-outline"
  | "error-outline"

type ButtonSize = "md" | "sm" | "xs" | "lg"

type CommonButtonProps = {
  variant?: ButtonVariant
  size?: ButtonSize
  className?: string
}

function getVariantClass(variant: ButtonVariant) {
  switch (variant) {
    case "primary-outline": return "btn-outline btn-primary"
    case "secondary-outline": return "btn-outline btn-secondary"
    case "success-outline": return "btn-outline btn-success"
    case "warning-outline": return "btn-outline btn-warning"
    case "error-outline": return "btn-outline btn-error"
    case "danger": return "btn-error"
    default: return `btn-${variant}`
  }
}

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & CommonButtonProps

export function Button({ variant = "primary", size = "md", className = "", ...props }: ButtonProps) {
  const sizeClass = size === "md" ? "" : `btn-${size}`
  const variantClass = getVariantClass(variant)
  
  return (
    <button 
      className={`btn ${variantClass} ${sizeClass} ${className}`.trim()} 
      {...props} 
    />
  )
}

export type ButtonLinkProps = ComponentProps<typeof Link> & CommonButtonProps

export function ButtonLink({ variant = "primary", size = "md", className = "", ...props }: ButtonLinkProps) {
  const sizeClass = size === "md" ? "" : `btn-${size}`
  const variantClass = getVariantClass(variant)

  return (
    <Link 
      className={`btn ${variantClass} ${sizeClass} ${className}`.trim()} 
      {...props} 
    />
  )
}
