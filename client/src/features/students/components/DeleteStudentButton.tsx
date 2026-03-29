import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { useEventsByStudent } from "@/features/events/query/eventQueries";
import { useDeleteStudentMutation } from "../hooks/use-student-mutation";
import { useNavigate } from "react-router-dom";

export const DeleteStudentButton = ({ studentId }: { studentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const { mutate: deleteStudent, isPending: isDeleting } =
    useDeleteStudentMutation();
  const { data: eventsData, isLoading: isEventsLoading } =
    useEventsByStudent(studentId);

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteStudent(
      { studentId },
      {
        onSettled: () => {
          setIsOpen(false);
          navigate("/students");
        },
      },
    );
  };

  const eventsCount = eventsData?.page?.totalElements ?? 0;

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={isDeleting}
        variant="danger"
        className="sm:mr-auto"
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
