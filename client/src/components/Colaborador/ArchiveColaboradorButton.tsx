import { Button } from "@/components/Ui/Button.tsx";
import { ConfirmModal } from "@/components/Ui/confirm-modal.tsx";
import { ArchiveIcon, ArchiveRestoreIcon } from "lucide-react";
import { useState } from "react";
import { useColaboradorMutations } from "../../services/use-colaborador-mutations.ts";

type ArchiveColaboradorButtonProps = {
  colaboradorId: string;
  isArchived: boolean | null;
};

export const ArchiveColaboradorButton = ({
  colaboradorId,
  isArchived,
}: ArchiveColaboradorButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const { archiveColaborador, unarchiveColaborador } = useColaboradorMutations();

  const archived = !!isArchived;
  const isPending = archiveColaborador.isPending || unarchiveColaborador.isPending;
  const actionLabel = archived ? "Desarquivar" : "Arquivar";
  const Icon = archived ? ArchiveRestoreIcon : ArchiveIcon;
  const variant = archived ? "outline" : "warning";
  const modalVariant = archived ? "info" : "warning";

  function handleConfirm() {
    const action = archived ? unarchiveColaborador : archiveColaborador;
    action.mutate(
      { colaboradorId },
      {
        onSettled: () => setShowConfirm(false),
      }
    );
  }

  return (
    <>
      <Button
        type="button"
        onClick={() => setShowConfirm(true)}
        variant={variant}
        disabled={isPending}
      >
        <Icon className="h-4 w-4" />
        {isPending ? "Processando..." : actionLabel}
      </Button>

      <ConfirmModal
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={handleConfirm}
        title={`${actionLabel} Colaborador`}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o colaborador?`}
        isPending={isPending}
        confirmText="Sim"
        cancelText="Cancelar"
        variant={modalVariant}
      />
    </>
  );
};
