import { Button } from "@/components/ui/button";
import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
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

  function handleConfirm() {
    const action = isArchived ? unarchiveParent : archiveParent;
    action({ parentId });
    setShowConfirm(false);
  }

  if (isPending) {
    return (
      <Button type="button" disabled variant="outline">
        <Loader2Icon className="h-4 w-4 animate-spin" />
        Processando...
      </Button>
    );
  }

  if (showConfirm) {
    const message = isArchived
      ? "Deseja mesmo desarquivar o responsável?"
      : "Deseja mesmo arquivar o responsável? A ação será bloqueada se houver alunos ativos vinculados.";

    return (
      <InlineConfirmAlert
        variant={isArchived ? "info" : "warning"}
        message={message}
        confirmText="Sim"
        cancelText="Cancelar"
        onConfirm={handleConfirm}
        onCancel={() => setShowConfirm(false)}
        className="p-2"
      />
    );
  }

  return (
    <Button type="button" onClick={() => setShowConfirm(true)} variant={variant}>
      <Icon className="h-4 w-4" />
      {actionLabel}
    </Button>
  );
};
