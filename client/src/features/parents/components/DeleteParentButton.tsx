import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { useGetStudentsByParent } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useParentMutations } from "../hooks/parent-mutations";

export const DeleteParentButton = ({ parentId }: { parentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const {
    deleteParent: {
      mutate: deleteParent,
      isPending: isDeleting,
    },
  } = useParentMutations();

  const {
    data: parentStudents,
    isLoading: isParentStudentsLoading,
  } = useGetStudentsByParent(parentId);
  const linkedStudentsCount = parentStudents?.totalElements ?? 0;
  const hasLinkedStudents = linkedStudentsCount > 0;

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
        onSuccess: () => {
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

      {/*TODO: esse modal tem que ser mais genérico*/}
      <DeleteConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Excluir responsável"
        isItemPending={isDeleting}
        isItemLoading={isParentStudentsLoading}
        itemDeleteCount={linkedStudentsCount}
        itemName="responsável"
        isBlocker={hasLinkedStudents}
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Não é possível excluir este responsável enquanto houver alunos ativos
            vinculados. Para manter a integridade do cadastro, atualize ou
            arquive os alunos relacionados antes de tentar novamente.
          </div>
        }
      />
    </>
  );
};
