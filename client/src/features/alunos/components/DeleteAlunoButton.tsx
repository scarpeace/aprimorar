import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useAlunoMutations } from "../hooks/use-aluno-mutations";
import { ConfirmationModal } from "@/components/ui/confirmation-modal";

type DeleteAlunoButtonProps = {
  studentId: string;
  className?: string;
};

export const DeleteAlunoButton = ({ studentId, className }: DeleteAlunoButtonProps) => {
  const [isOpen, setIsOpen] = useState(false);

  const { deleteStudent } = useAlunoMutations();

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!deleteStudent.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteStudent.mutate(
      { studentId },
      {
        onSuccess: () => {
          setIsOpen(false);
        }
      },
    );
  };

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={deleteStudent.isPending}
        variant="danger"
        className={className}
      >
        <Trash2 className="h-4 w-4" />
        {deleteStudent.isPending ? "Excluindo..." : "Excluir"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Atenção: EXCLUSÃO DEFINITIVA"
        description="Leia os termos de exclusão antes de confirmar."
        variant="danger"
        isPending={deleteStudent.isPending}
        disableCloseOnPending
        confirmText="Excluir Aluno"
      ><div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
        Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
        <strong>
          todos os seus atendimentoos e atendimentos serão transferidos
          automaticamente para um perfil de "Aluno Removido"
        </strong>{" "}
        para manter a consistência financeira e o histórico.
      </div>
      </ConfirmationModal>
     </>
  );
};
