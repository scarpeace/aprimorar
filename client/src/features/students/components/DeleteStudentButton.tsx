import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useGetAtendimentosByStudentId } from "@/kubb";
import { useStudentMutations } from "../hooks/student-mutations";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

type DeleteStudentButtonProps = {
  studentId: string;
  className?: string;
};

export const DeleteStudentButton = ({ studentId, className }: DeleteStudentButtonProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const {
    deleteStudent: { mutate: deleteStudent, isPending: isDeleting },
  } = useStudentMutations();
  const { data: eventsData, isLoading: isEventsLoading } =
    useGetAtendimentosByStudentId(studentId);

  const handleOpenClick = () => {
    setErrorMessage(null);
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setErrorMessage(null);
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    setErrorMessage(null);
    deleteStudent(
      { studentId },
      {
        onSuccess: () => {
          setIsOpen(false);
        },
        onError: (error) => {
          setErrorMessage(getFriendlyErrorMessage(error));
        },
      },
    );
  };

  const eventsCount = eventsData?.appointments?.totalElements ?? 0;

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={isDeleting}
        variant="danger"
        className={className}
      >
        <Trash2 className="h-4 w-4" />
        {isDeleting ? "Excluindo..." : "Excluir"}
      </Button>

      <DeleteConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Excluir Aluno"
        isItemPending={isDeleting}
        isItemLoading={isEventsLoading}
        itemDeleteCount={eventsCount}
        itemName="aluno"
        errorMessage={errorMessage}
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
            <strong>
              todos os seus eventos e atendimentos serão transferidos
              automaticamente para um perfil de "Aluno Removido"
            </strong>{" "}
            para manter a consistência financeira e o histórico da escola.
          </div>
        }
      />
    </>
  );
};
