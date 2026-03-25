import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import {
  useArchiveParentMutation,
  useUnarchiveParentMutation,
} from "../query/parentMutations";

type ArchiveParentButtonProps = {
  parentId: string;
  isArchived: boolean;
};

export const ArchiveParentButton = ({
  parentId,
  isArchived,
}: ArchiveParentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const { mutate: unarchiveParent, isPending: isUnarchivingParent } =
    useUnarchiveParentMutation();
  const { mutate: archiveParent, isPending: isArchivingParent } =
    useArchiveParentMutation();

  const handleArchive = () => {
    archiveParent({ parentId });
  };
  const handleUnarchive = () => {
    unarchiveParent({ parentId });
  };

  if (isUnarchivingParent || isArchivingParent) {
    return (
      <Button type="button" disabled variant="outline" className="sm:mr-auto">
        <Loader2Icon className="h-4 w-4 animate-spin" />
        Processando...
      </Button>
    );
  }

  if (showConfirm) {
    return (
      <InlineConfirmAlert
        variant={isArchived ? "info" : "warning"}
        message={`Deseja mesmo ${isArchived ? "Desarquivar" : "Arquivar"} o aluno?`}
        confirmText={`Sim, ${isArchived ? "Desarquivar" : "Arquivar"} o aluno`}
        cancelText="Cancelar"
        onConfirm={isArchived ? handleUnarchive : handleArchive}
        onCancel={() => setShowConfirm(false)}
        className="sm:mr-auto"
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
        className="sm:mr-auto"
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
      className="sm:mr-auto"
    >
      <ArchiveIcon className="h-4 w-4" />
      Arquivar
    </Button>
  );
};
