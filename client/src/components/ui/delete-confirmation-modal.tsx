import { AlertTriangle } from "lucide-react"
import { Button } from "./button"
import type { ReactNode } from "react"

interface DeleteConfirmationModalProps {
  isOpen: boolean
  onClose: () => void
  onConfirm: () => void
  title: string
  isItemPending: boolean
  isItemLoading?: boolean
  itemDeleteCount?: number
  itemName?: string
  isBlocker?: boolean
  phantomWarning?: ReactNode
  confirmText?: string
  cancelText?: string
}

export const DeleteConfirmationModal = ({
  isOpen,
  onClose,
  onConfirm,
  title,
  isItemPending,
  isItemLoading = false,
  itemDeleteCount = 0,
  itemName = "item",
  isBlocker = false,
  phantomWarning,
  confirmText = "Sim, Excluir",
  cancelText = "Cancelar",
}: DeleteConfirmationModalProps) => {
  if (!isOpen) return null

  const hasEvents = itemDeleteCount > 0

  return (
    <dialog className="modal modal-bottom sm:modal-middle modal-open">
      <div className="modal-box">
        <h3 className="font-bold text-lg flex items-center gap-2 text-error">
          <AlertTriangle className="h-5 w-5" />
          {title}
        </h3>

        {isItemLoading ? (
          <p className="py-4 text-muted-foreground">Verificando dados vinculados...</p>
        ) : hasEvents ? (
          <div className="py-4 space-y-4">
            {isBlocker ? (
              <>
                {phantomWarning}
              </>
            ) : (
              <>
                <p>
                  <strong>Atenção:</strong> Este {itemName} possui <span className="text-secondary font-semibold">{itemDeleteCount}</span> evento(s) vinculado(s).
                </p>
                {phantomWarning}
                <p className="font-semibold mt-2">Deseja prosseguir com a exclusão?</p>
              </>
            )}
          </div>
        ) : (
          <p className="py-4">
            Tem certeza que deseja excluir este {itemName}? <strong>Esta ação não pode ser desfeita.</strong>
          </p>
        )}

        <div className="modal-action">
          <form method="dialog">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              disabled={isItemPending}
            >
              {isBlocker ? "Entendi" : cancelText}
            </Button>
            {!isBlocker && (
              <Button
                type="button"
                variant="danger"
                onClick={onConfirm}
                disabled={isItemPending || isItemLoading}
                className="ml-2"
              >
                {isItemPending ? "Excluindo..." : confirmText}
              </Button>
            )}
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
}
