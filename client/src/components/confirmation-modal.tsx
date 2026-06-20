import { AlertTriangle, Info } from "lucide-react"
import { createPortal } from "react-dom"
import type { ReactNode } from "react"
import { Button } from "./button.tsx"

export type ConfirmationVariant = "danger" | "warning" | "info" | "success" | "neutral"

interface ConfirmationModalProps {
  isOpen: boolean
  title: string
  description?: string
  children?: ReactNode
  confirmText?: string
  cancelText?: string
  variant?: ConfirmationVariant
  isPending?: boolean
  disableCloseOnPending?: boolean
  onConfirm: () => void
  onClose: () => void
}

function getVariantStyle(variant: ConfirmationVariant) {
  switch (variant) {
    case "danger":
      return { titleColor: "text-error", buttonVariant: "danger" as const, icon: AlertTriangle }
    case "warning":
      return { titleColor: "text-warning", buttonVariant: "warning" as const, icon: AlertTriangle }
    case "info":
      return { titleColor: "text-info", buttonVariant: "primary" as const, icon: Info }
    case "success":
      return { titleColor: "text-success", buttonVariant: "success" as const, icon: Info }
    default:
      return { titleColor: "text-base-content", buttonVariant: "primary" as const, icon: Info }
  }
}

export const ConfirmationModal = ({
  isOpen,
  title,
  description,
  children,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  variant = "neutral",
  isPending = false,
  disableCloseOnPending = true,
  onConfirm,
  onClose,
}: ConfirmationModalProps) => {
  if (!isOpen || typeof document === "undefined") return null

  const canClose = !(disableCloseOnPending && isPending)
  const { titleColor, buttonVariant, icon: Icon } = getVariantStyle(variant)

  const handleClose = () => {
    if (canClose) {
      onClose()
    }
  }

  const modalContent = (
    <dialog className="modal modal-bottom sm:modal-middle modal-open">
      <div className="modal-box">
        <h3 className={`font-bold text-lg flex items-center gap-2 ${titleColor}`}>
          <Icon className="h-5 w-5" />
          {title}
        </h3>

        {description ? (
          <p className="py-4 text-sm text-base-content/80">{description}</p>
        ) : null}

        {children ? <div className="space-y-3">{children}</div> : null}

        <div className="modal-action">
          <form method="dialog">
            <Button type="button" variant="outline" onClick={handleClose} disabled={!canClose}>
              {cancelText}
            </Button>
            <Button
              type="button"
              variant={buttonVariant}
              onClick={onConfirm}
              disabled={isPending}
              className="ml-2"
            >
              {isPending ? "Processando..." : confirmText}
            </Button>
          </form>
        </div>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button type="button" onClick={handleClose}>close</button>
      </form>
    </dialog>
  )

  return createPortal(modalContent, document.body)
}
