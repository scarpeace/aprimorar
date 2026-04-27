import { CircleX, Info, AlertTriangle, CheckCircle2 } from "lucide-react"

type AlertVariant = "info" | "success" | "warning" | "error"

type AlertProps = {
  variant?: AlertVariant
  className?: string
  error?: string
  title?: string
}

const ICONS = {
  info: Info,
  success: CheckCircle2,
  warning: AlertTriangle,
  error: CircleX,
}

export function Alert({ variant = "info", className = "", error, title }: Readonly<AlertProps>) {
  const Icon = ICONS[variant]

  return (
    <div role="alert" className={`alert alert-${variant} ${className}`}>
      <Icon className="w-5 h-5" />
      <div className="flex flex-col">
        {title && <h3 className="font-bold">{title}</h3>}
        <span>{error || "Algo inesperado aconteceu, por favor tente novamente."}</span>
      </div>
    </div>
  )
}
