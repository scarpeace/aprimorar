import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import {
  useArchiveParentMutation,
  useUnarchiveParentMutation,
} from "../hooks/parent-mutations";

type ArchiveParentButtonProps = {
  parentId: string;
  isArchived: boolean;
};

export const ArchiveParentButton = ({
  parentId,
  isArchived,
}: ArchiveParentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const { mutate: unarchiveParent, isPending: isUnarchivingParent } = useUnarchiveParentMutation();
  const { mutate: archiveParent, isPending: isArchivingParent } = useArchiveParentMutation();

  const handleArchive = () => {
    archiveParent({ parentId });
    setShowConfirm(false);
  };
  const handleUnarchive = () => {
    unarchiveParent({ parentId });
    setShowConfirm(false);
  };

  if (isUnarchivingParent || isArchivingParent) {
    return (
      <Button type="button" disabled variant="outline">
        <Loader2Icon className="h-4 w-4 animate-spin" />
        Processando...
      </Button>
    );
  }

  if (showConfirm) {
    return (
      <InlineConfirmAlert
        variant={isArchived ? "info" : "warning"}
        message={`Deseja mesmo ${isArchived ? "Desarquivar" : "Arquivar"} o responsável?`}
        confirmText={'Sim'}
        cancelText="Cancelar"
        onConfirm={isArchived ? handleUnarchive : handleArchive}
        onCancel={() => setShowConfirm(false)}
        className="p-2"
        isLoading={isArchivingParent || isUnarchivingParent}
      />
    );
  }

  if (isArchived) {
    return (
      <Button
        type="button"
        onClick={() => setShowConfirm(true)}
        variant="outline"
      >
        <ArchiveRestoreIcon className="h-4 w-4" />
        Desarquivar
      </Button>
    );
  }

  return (
    <Button
      type="button"
      onClick={() => setShowConfirm(true)}
      variant="warning"
    >
      <ArchiveIcon className="h-4 w-4" />
      Arquivar
    </Button>
  );
};
