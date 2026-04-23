import { AlertTriangle, Info } from "lucide-react"
import { createPortal } from "react-dom"
import { Button } from "./button"
import type { ReactNode } from "react"

interface ConfirmModalProps {
  isOpen: boolean
  onClose: () => void
  onConfirm: () => void
  title: string
  message: ReactNode
  isPending?: boolean
  confirmText?: string
  cancelText?: string
  variant?: "danger" | "warning" | "info" | "primary" | "success"
}

export const ConfirmModal = ({
  isOpen,
  onClose,
  onConfirm,
  title,
  message,
  isPending = false,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  variant = "warning",
}: ConfirmModalProps) => {
  if (!isOpen) return null

  if (typeof document === "undefined") return null

  const Icon = variant === "info" || variant === "primary" || variant === "success" ? Info : AlertTriangle;
  const titleColor = variant === "danger" ? "text-error" : variant === "warning" ? "text-warning" : variant === "info" ? "text-info" : "text-base-content";

  const modalContent = (
    <dialog className="modal modal-bottom sm:modal-middle modal-open">
      <div className="modal-box">
        <h3 className={`font-bold text-lg flex items-center gap-2 ${titleColor}`}>
          <Icon className="h-5 w-5" />
          {title}
        </h3>

        <div className="py-4">
          <p>{message}</p>
        </div>

        <div className="modal-action">
          <form method="dialog">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              disabled={isPending}
            >
              {cancelText}
            </Button>
            <Button
              type="button"
              variant={variant}
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
        <button type="button" onClick={onClose}>
          close
        </button>
      </form>
    </dialog>
  )

  return createPortal(modalContent, document.body)
}
