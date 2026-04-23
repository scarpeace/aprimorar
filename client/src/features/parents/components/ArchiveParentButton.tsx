import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { ArchiveIcon, ArchiveRestoreIcon } from "lucide-react";
import { useState } from "react";
import { useParentMutations } from "../hooks/parent-mutations";

type ArchiveParentButtonProps = {
  parentId: string;
  isArchived: boolean;
};

export const ArchiveParentButton = ({
  parentId,
  isArchived,
}: ArchiveParentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const {
    archiveParent: {
      mutate: archiveParent,
      isPending: isArchivingParent,
    },
    unarchiveParent: {
      mutate: unarchiveParent,
      isPending: isUnarchivingParent,
    },
  } = useParentMutations();

  const isPending = isArchivingParent || isUnarchivingParent;
  const actionLabel = isArchived ? "Desarquivar" : "Arquivar";
  const Icon = isArchived ? ArchiveRestoreIcon : ArchiveIcon;
  const variant = isArchived ? "outline" : "warning";
  const modalVariant = isArchived ? "info" : "warning";

  function handleConfirm() {
    const action = isArchived ? unarchiveParent : archiveParent;
    action(
      { parentId },
      {
        onSettled: () => setShowConfirm(false),
      }
    );
  }

  const message = isArchived
    ? "Deseja mesmo desarquivar o responsável?"
    : "Deseja mesmo arquivar o responsável? A ação será bloqueada se houver alunos ativos vinculados.";

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
        title={`${actionLabel} Responsável`}
        message={message}
        isPending={isPending}
        confirmText="Sim"
        cancelText="Cancelar"
        variant={modalVariant}
      />
    </>
  );
};
