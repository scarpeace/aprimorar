import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import { useArchiveStudent, useGetStudentById, useUnarchiveStudent } from "@/kubb";

//TODO falta adicionar o comportamento de Alert para desarquivar também
export const ArchiveStudentButton = ({
  studentId,
}: {
  studentId: string;
}) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const { data: student } = useGetStudentById(studentId);
  const { mutate: unarchiveStudent, isPending: isUnarchiving } =
    useUnarchiveStudent();
  const { mutate: archiveStudent, isPending: isArchiving } =
    useArchiveStudent();

  const isLoading = isArchiving || isUnarchiving;

  const handleArchive = () => {
    archiveStudent({ studentId }, {
      onSuccess: () => setShowConfirm(false),
    });
  };

  const handleUnarchive = () => {
    unarchiveStudent({ studentId });
  };
  console.log(student?.archivedAt)

  if (isLoading && !showConfirm) {
    return (
      <Button type="button" disabled variant="outline" className="sm:mr-auto">
        <Loader2Icon className="h-4 w-4 animate-spin" />
        Processando...
      </Button>
    );
  }

  if (student?.archivedAt !== null) {
    return (
      <Button
        type="button"
        onClick={handleUnarchive}
        variant="outline"
        className="sm:mr-auto"
      >
        <ArchiveRestoreIcon className="h-4 w-4" />
        Desarquivar
      </Button>
    );
  }

  if (showConfirm) {
    return (
      <InlineConfirmAlert
        variant="warning"
        message="Deseja realmente arquivar este aluno?"
        confirmText="Sim, arquivar"
        cancelText="Cancelar"
        onConfirm={handleArchive}
        onCancel={() => setShowConfirm(false)}
        className="sm:mr-auto"
        isLoading={isArchiving}
      />
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
