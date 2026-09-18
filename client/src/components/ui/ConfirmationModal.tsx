"use client";

import { AlertTriangle } from "lucide-react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";

type ConfirmationVariant = "primary" | "success" | "warning" | "error";

type ConfirmationModalProps = {
  isOpen: boolean;
  title: string;
  description: string;
  confirmLabel: string;
  pendingLabel?: string;
  variant?: ConfirmationVariant;
  isPending?: boolean;
  onConfirm: () => void;
  onClose: () => void;
};

const iconColorClasses: Record<ConfirmationVariant, string> = {
  primary: "text-primary",
  success: "text-success",
  warning: "text-warning",
  error: "text-error",
};

export function ConfirmationModal({
  isOpen,
  title,
  description,
  confirmLabel,
  pendingLabel = "Processando...",
  variant = "primary",
  isPending = false,
  onConfirm,
  onClose,
}: Readonly<ConfirmationModalProps>) {
  function close() {
    if (!isPending) {
      onClose();
    }
  }

  return (
    <Modal isOpen={isOpen} onClose={close} title={title} description={description} size="sm">
      <div className="space-y-6">
        <div className="flex items-start gap-3 rounded-2xl bg-base-200/60 p-4">
          <AlertTriangle className={iconColorClasses[variant]} size={20} aria-hidden="true" />
          <p className="text-sm text-base-content/70">Confirme se deseja continuar com esta operação.</p>
        </div>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={isPending} onClick={close}>
            Cancelar
          </Button>
          <Button type="button" variant={variant} disabled={isPending} onClick={onConfirm}>
            {isPending ? pendingLabel : confirmLabel}
          </Button>
        </div>
      </div>
    </Modal>
  );
}
