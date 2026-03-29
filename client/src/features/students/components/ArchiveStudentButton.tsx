import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";

import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import {
  useArchiveStudentMutation,
  useUnarchiveStudentMutation,
} from "../hooks/use-student-mutation";
import {
  archiveStudent,
  archiveStudentMutationKey,
  getStudentByIdQueryKey,
  getStudentsQueryKey,
  getStudentSummaryQueryKey,
  useArchiveStudent,
  useUnarchiveStudent,
} from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

type ArchiveStudentButtonProps = {
  studentId: string;
  isArchived: boolean;
};

export const ArchiveStudentButton = ({
  studentId,
  isArchived,
}: ArchiveStudentButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);
  const queryClient = useQueryClient();

  const { mutate: unarchiveStudent, isPending: isUnarchivingStudent } =
    useUnarchiveStudent({
      mutation: {
        onSuccess: () => {
          toast.success("Aluno desarquivado com sucesso!");
          setShowConfirm(false);
          queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
          queryClient.invalidateQueries({queryKey: getStudentByIdQueryKey(studentId)});
          queryClient.invalidateQueries({queryKey: getStudentSummaryQueryKey()});
        },
      },
    });
  const { mutate: archiveStudent, isPending: isArchivingStudent } =
    useArchiveStudent({
      mutation: {
        onSuccess: () => {
          toast.success("Aluno arquivado com sucesso!");
          setShowConfirm(false);
          queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
          queryClient.invalidateQueries({
            queryKey: getStudentByIdQueryKey(studentId),
          });
          queryClient.invalidateQueries({
            queryKey: getStudentSummaryQueryKey(),
          });
        },
      },
    });

  const handleArchive = () => {
    archiveStudent({ studentId });
  };
  const handleUnarchive = () => {
    unarchiveStudent({ studentId });
  };

  if (isUnarchivingStudent || isArchivingStudent) {
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
