import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { ArchiveIcon, ArchiveRestoreIcon } from "lucide-react";
import { useState } from "react";

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
  const modalVariant = isArchived ? "info" : "warning";

  function handleConfirm() {
    const action = isArchived ? unarchiveStudent : archiveStudent;
    action(
      { studentId },
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
        title={`${actionLabel} Aluno`}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o aluno?`}
        isPending={isPending}
        confirmText="Sim"
        cancelText="Cancelar"
        variant={modalVariant}
      />
    </>
  );
};
