import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
import { useState } from "react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import { useStudentMutations } from "../hooks/student-mutations";

type ArchiveStudentButtonProps = {
  studentId: string;
  isArchived: boolean;
};

export const ArchiveStudentButton = ({
  studentId,
  isArchived,
}: ArchiveStudentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const {
    archiveStudent: { mutate: archiveStudent, isPending: isArchivingStudent },
    unarchiveStudent: {
      mutate: unarchiveStudent,
      isPending: isUnarchivingStudent,
    },
  } = useStudentMutations();

  const isPending = isArchivingStudent || isUnarchivingStudent;
  const actionLabel = isArchived ? "Desarquivar" : "Arquivar";
  const Icon = isArchived ? ArchiveRestoreIcon : ArchiveIcon;
  const variant = isArchived ? "outline" : "warning";

  function handleConfirm() {
    const action = isArchived ? unarchiveStudent : archiveStudent;
    action({ studentId });
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
    return (
      <InlineConfirmAlert
        variant={isArchived ? "info" : "warning"}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o aluno?`}
        confirmText="Sim"
        cancelText="Cancelar"
        onConfirm={handleConfirm}
        onCancel={() => setShowConfirm(false)}
        className="p-2"
      />
    );
  }

  return (
    <Button
      type="button"
      onClick={() => setShowConfirm(true)}
      variant={variant}
    >
      <Icon className="h-4 w-4" />
      {actionLabel}
    </Button>
  );
};
