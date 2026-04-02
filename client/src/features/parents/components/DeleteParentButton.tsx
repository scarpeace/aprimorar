import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useDeleteParentMutation } from "../hooks/use-parent-mutations";

export const DeleteParentButton = ({ parentId }: { parentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const { mutate: deleteParent, isPending: isDeleting } =
    useDeleteParentMutation();
  const { data: parentStudents, isLoading: isParentStudentsLoading } =
    useStudentsByParent(parentId);

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteParent(
      { parentId },
      {
        onSettled: () => {
          setIsOpen(false);
        },
      },
    );
  };

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
        isItemLoading={isParentStudentsLoading}
        itemDeleteCount={parentStudents?.length}
        itemName="aluno"
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            O seu responsável ainda tem estudantes vinculados, ao excluí-lo, seu
            histórico pessoal será apagado, mas{" "}
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
