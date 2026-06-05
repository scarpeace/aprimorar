import type { ReactNode } from "react"
import { createPortal } from "react-dom"

interface ModalProps {
  isOpen: boolean
  onClose: () => void
  title: string
  description?: string
  children: ReactNode
  size?: "sm" | "md" | "lg" | "xl"
  closeOnBackdrop?: boolean
  closeOnEscape?: boolean
}

const sizeClasses = {
  sm: "max-w-md",
  md: "max-w-2xl",
  lg: "max-w-4xl",
  xl: "max-w-6xl",
} as const

export function Modal({
  isOpen,
  onClose,
  title,
  description,
  children,
  size = "md",
  closeOnBackdrop = true,
  closeOnEscape = true,
}: ModalProps) {
  if (!isOpen || typeof document === "undefined") return null

  const handleBackdropClick = () => {
    if (closeOnBackdrop) onClose()
  }

  const modalContent = (
    <div className="modal modal-open" role="presentation">
      <div className={`modal-box border border-base-300 bg-base-100 shadow-2xl ${sizeClasses[size]}`}>
        <h3 className="mb-1 text-lg font-bold">{title}</h3>
        {description ? <p className="mb-4 text-sm text-base-content/60">{description}</p> : null}
        {children}
      </div>
      <button
        type="button"
        className="modal-backdrop"
        aria-label="Fechar modal"
        onClick={handleBackdropClick}
        onKeyDown={(event) => {
          if (closeOnEscape && event.key === "Escape") onClose()
        }}
      />
    </div>
  )

  return createPortal(modalContent, document.body)
}
