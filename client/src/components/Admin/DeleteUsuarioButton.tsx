import { useState } from "react";

import { Button } from "@/components/Ui/Button.tsx";

import { useUsuarioMutations } from "../../services/use-usuario-mutations.ts";
import { ConfirmationModal } from "@/components/Ui/confirmation-modal.tsx";

type DeleteUsuarioButtonProps = {
  usuarioId: string;
  usuarioEmail: string;
};

export function DeletarUsuarioButton({ usuarioId, usuarioEmail }: DeleteUsuarioButtonProps) {
  const [isOpen, setIsOpen] = useState(false);
  const { deleteUsuario } = useUsuarioMutations();


  const handleClose = () => {
    if (!deleteUsuario.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirm = () => {
    deleteUsuario.mutate(
      { id: usuarioId },
      {
        onSuccess: () => {
          setIsOpen(false);
        },
      },
    );
  };

  return (
    <>
      <Button variant="danger" size="sm" onClick={() => setIsOpen(true)}>
        Excluir
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirm}
        title="Atenção: EXCLUSÃO DEFINITIVA"
        description="Leia os termos de exclusão antes de confirmar."
        variant="danger"
        isPending={deleteUsuario.isPending}
        disableCloseOnPending
        confirmText="Excluir Usuário"
      >
        <span><strong>ATENÇÃO</strong>, tem certeza que deseja excluir o usuário <strong>{usuarioEmail}</strong> do sistema?</span>
      </ConfirmationModal>
    </>
  );
}
