import type { ProblemDetailResponseDTO } from "@/kubb"
import { CircleX } from "lucide-react"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  className?: string
  error?: string
}

export function Alert({ variant = "info", className = "", error }: Readonly<AlertProps>) {
  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      <CircleX className="w-4 h-4 mr-2" />
      <span>Erro: {error?? "Algo inesperado aconteceu, por favor tente novamente."}</span>
    </div>
  )
}
