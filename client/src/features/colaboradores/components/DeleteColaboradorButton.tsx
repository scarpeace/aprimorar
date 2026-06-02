import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useColaboradorMutations } from "../hooks/use-colaborador-mutations";
import { ConfirmationModal } from "@/components/ui/confirmation-modal";

export const DeleteColaboradorButton = ({ colaboradorId }: { colaboradorId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const { deleteEmployee } = useColaboradorMutations();

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!deleteEmployee.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteEmployee.mutate(
      { colaboradorId: colaboradorId },
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
        disabled={deleteEmployee.isPending}
        variant="danger"
      >
        <Trash2 className="h-4 w-4" />
        {deleteEmployee.isPending ? "Excluindo..." : "Excluir"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Atenção: EXCLUSÃO DEFINITIVA"
        description="Leia os termos de exclusão antes de confirmar."
        variant="danger"
        isPending={deleteEmployee.isPending}
        disableCloseOnPending
        confirmText="Excluir Aluno"
      >
        <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
          Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
          <strong>
            todos os seus atendimentoos e atendimentos serão transferidos
            automaticamente para um perfil de "Colaborador Removido"
          </strong>{" "}
          para manter a consistência financeira e o histórico.
        </div>
      </ConfirmationModal>
    </>
  );
};
