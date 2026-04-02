import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useDeleteParentMutation } from "../hooks/use-parent-mutations";

import { useNavigate } from "react-router-dom";
import { useStudentsByParent } from "@/features/students/hooks/use-students-query";

//TODO: O comportamento de exclusão tá meio esquisito, ou coloca o toast ou coloca o alert? alert é só nos formulários?
export const DeleteParentButton = ({ parentId }: { parentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const { mutate: deleteParent, isPending: isDeleting } =
    useDeleteParentMutation();
  const { data: parentStudents, isLoading: isParentStudentsLoading } =
    useStudentsByParent({parentId});

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteParent({ parentId },
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
        title="Excluir Aluno"
        isItemPending={isDeleting}
        isItemLoading={isParentStudentsLoading}
        itemDeleteCount={parentStudents?.length}
        itemName="responsável"
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Se o seu responsável tiver alunos vinculados não será possível excluí-lo,
            <strong>
              {" "} primeiro exclua os alunos vinculados, em seguida volte e exclua o responsável
            </strong>{" "}
            para manter a consistência do banco de dados e histórico do sistema.
          </div>
        }
      />
    </>
  );
};
