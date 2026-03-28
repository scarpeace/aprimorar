import type { ProblemDetailResponseDTO } from "@/kubb"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  className?: string
  error?: ProblemDetailResponseDTO
}

export function Alert({ variant = "info", className = "", error }: Readonly<AlertProps>) {
  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      {error?.status}
      { error?.title}
      <span>{error?.detail}</span>
    </div>
  )
}
