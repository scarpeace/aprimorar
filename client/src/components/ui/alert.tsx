import type { ProblemDetailResponseDTO } from "@/kubb"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  message?: string
  error?: ProblemDetailResponseDTO
  className?: string
}

export function Alert({ variant = "info", message, error, className = "" }: Readonly<AlertProps>) {
  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      <span>{error?.detail}</span>
    </div>
  )
}
