import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import {
  getStudentByIdQueryKey,
  getStudentsQueryKey,
  useArchiveStudent,
  useUnarchiveStudent,
} from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";

type ArchiveStudentButtonProps = {
  studentId: string;
  isArchived: boolean;
};

//TODO falta adicionar o comportamento de Alert para desarquivar também
export const ArchiveStudentButton = ({
  studentId,
  isArchived,
}: ArchiveStudentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);
  const queryClient = useQueryClient();

  const { mutate: unarchiveStudent, isPending: isUnarchiving } =
    useUnarchiveStudent();
  const { mutate: archiveStudent, isPending: isArchiving } =
    useArchiveStudent();

  const handleArchive = () => {
    archiveStudent(
      { studentId },
      {
        onSuccess: () => {
          setShowConfirm(false)
          queryClient.invalidateQueries({
            queryKey: getStudentByIdQueryKey(studentId),
          });
          queryClient.invalidateQueries({
            queryKey: getStudentsQueryKey(),
          });
        },
      },
    );
  };

  const handleUnarchive = () => {
    unarchiveStudent(
      { studentId },
      {
        onSuccess: () => {
          setShowConfirm(false)
          queryClient.invalidateQueries({
            queryKey: getStudentByIdQueryKey(studentId),
          });
          queryClient.invalidateQueries({
            queryKey: getStudentsQueryKey(),
          });
        },
      },
    );
  };

  if (isUnarchiving || isArchiving) {
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
        message={`Deseja mesmo ${isArchived ? 'Desarquivar' : 'Arquivar'} o aluno?`}
        confirmText={`Sim, ${isArchived ? 'Desarquivar' : 'Arquivar'} o aluno`}
        cancelText="Cancelar"
        onConfirm={isArchived ? handleUnarchive : handleArchive}
        onCancel={() => setShowConfirm(false)}
        className="sm:mr-auto"
        isLoading={isArchiving}
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
