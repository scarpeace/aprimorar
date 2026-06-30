import type { ButtonHTMLAttributes } from "react";

type ButtonVariant = "primary" | "secondary" | "success" | "warning" | "error" | "ghost" | "outline";
type ButtonSize = "sm" | "md" | "lg";

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant;
  size?: ButtonSize;
};

export function Button({ variant = "primary", size = "md", className = "", ...props }: ButtonProps) {
  const sizeClass = size === "md" ? "" : `btn-${size}`;
  const variantClass = variant === "outline" ? "btn-outline" : `btn-${variant}`;

  return <button className={`btn ${variantClass} ${sizeClass} ${className}`.trim()} {...props} />;
}
