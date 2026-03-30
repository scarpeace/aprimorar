import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import {
  useArchiveStudent,
  useUnarchiveStudent,
} from "@/kubb";
import { useArchiveStudentMutation, useUnarchiveStudentMutation } from "../hooks/use-student-mutation";

type ArchiveStudentButtonProps = {
  studentId: string;
  isArchived: boolean;
};

export const ArchiveStudentButton = ({
  studentId,
  isArchived,
}: ArchiveStudentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const { mutate: unarchiveStudent, isPending: isUnarchivingStudent } = useUnarchiveStudentMutation();
  const { mutate: archiveStudent, isPending: isArchivingStudent } = useArchiveStudentMutation();

  const handleArchive = () => {
    archiveStudent({ studentId });
    setShowConfirm(false);
  };
  const handleUnarchive = () => {
    unarchiveStudent({ studentId });
    setShowConfirm(false);
  };

  if (isUnarchivingStudent || isArchivingStudent) {
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
        message={`Deseja mesmo ${isArchived ? "Desarquivar" : "Arquivar"} o aluno?`}
        confirmText={'Sim'}
        cancelText="Cancelar"
        onConfirm={isArchived ? handleUnarchive : handleArchive}
        onCancel={() => setShowConfirm(false)}
        className="p-2"
        isLoading={isArchivingStudent || isUnarchivingStudent}
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
