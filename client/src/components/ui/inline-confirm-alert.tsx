import React, { ReactNode } from "react"
import { AlertTriangle, Info, AlertCircle, CheckCircle } from "lucide-react"

interface InlineConfirmAlertProps {
  message: ReactNode
  onConfirm: () => void
  onCancel: () => void
  confirmText?: string
  cancelText?: string
  variant?: "warning" | "error" | "info" | "success"
  className?: string
  isLoading?: boolean
}

export function InlineConfirmAlert({
  message,
  onConfirm,
  onCancel,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  variant = "warning",
  className = "",
  isLoading = false,
}: InlineConfirmAlertProps) {
  
  const variantIcons = {
    warning: <AlertTriangle className="h-6 w-6 shrink-0" />,
    error: <AlertCircle className="h-6 w-6 shrink-0" />,
    info: <Info className="h-6 w-6 shrink-0" />,
    success: <CheckCircle className="h-6 w-6 shrink-0" />,
  }

  const confirmBtnClasses = {
    warning: "btn-warning",
    error: "btn-error",
    info: "btn-info",
    success: "btn-success",
  }

  return (
    <div role="alert" className={`alert alert-${variant} alert-vertical sm:alert-horizontal shadow-sm ${className}`}>
      {variantIcons[variant]}
      <span className="text-sm font-medium">{message}</span>
      <div className="flex gap-2">
        <button 
          className="btn btn-sm btn-ghost" 
          onClick={onCancel}
          disabled={isLoading}
        >
          {cancelText}
        </button>
        <button 
          className={`btn btn-sm ${confirmBtnClasses[variant]}`} 
          onClick={onConfirm}
          disabled={isLoading}
        >
          {isLoading ? (
            <>
              <span className="loading loading-spinner loading-xs"></span>
              Aguarde...
            </>
          ) : (
             confirmText
          )}
        </button>
      </div>
    </div>
  )
}
