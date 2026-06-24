import { Button } from "@/components/Ui/Button.tsx";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useColaboradorMutations } from "../../services/use-colaborador-mutations.ts";
import { ConfirmationModal } from "@/components/Ui/confirmation-modal.tsx";

export const DeleteColaboradorButton = ({ colaboradorId }: { colaboradorId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const { deleteColaborador } = useColaboradorMutations();

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!deleteColaborador.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteColaborador.mutate(
      { colaboradorId },
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
        disabled={deleteColaborador.isPending}
        variant="danger"
      >
        <Trash2 className="h-4 w-4" />
        {deleteColaborador.isPending ? "Excluindo..." : "Excluir"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Atenção: EXCLUSÃO DEFINITIVA"
        description="Leia os termos de exclusão antes de confirmar."
        variant="danger"
        isPending={deleteColaborador.isPending}
        disableCloseOnPending
        confirmText="Excluir colaborador"
      >
        <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
          Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
          <strong>
            todos os seus atendimentos serão transferidos
            automaticamente para um perfil de "Colaborador Removido"
          </strong>{" "}
          para manter a consistência financeira e o histórico.
        </div>
      </ConfirmationModal>
    </>
  );
};
