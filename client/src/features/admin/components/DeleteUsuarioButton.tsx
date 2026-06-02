import { useState } from "react";

import { Button } from "@/components/ui/button";

import { useUsuarioMutations } from "../hooks/use-usuario-mutations";
import { ConfirmationModal } from "@/components/ui/confirmation-modal";

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
