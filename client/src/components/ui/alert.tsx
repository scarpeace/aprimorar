import { getFriendlyErrorMessage } from "@/lib/shared/api-errors"
import { CircleX } from "lucide-react"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  className?: string
  error: unknown
}

export function Alert({ variant = "info", className = "", error }: Readonly<AlertProps>) {
  const friendlyError = getFriendlyErrorMessage(error)
  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      <CircleX className="w-4 h-4 mr-2" />
      <span>{friendlyError ?? "Algo inesperado aconteceu, por favor tente novamente."}</span>
    </div>
  )
}
