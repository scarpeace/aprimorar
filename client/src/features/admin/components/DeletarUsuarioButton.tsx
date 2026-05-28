import { useState } from "react";

import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";

import { useUsuarioMutations } from "../hooks/use-usuario-mutations";

type DeletarUsuarioButtonProps = {
  usuarioId: string;
  usuarioEmail: string;
};

export function DeletarUsuarioButton({ usuarioId, usuarioEmail }: DeletarUsuarioButtonProps) {
  const [isOpen, setIsOpen] = useState(false);
  const { deleteUsuario } = useUsuarioMutations();

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

      <DeleteConfirmationModal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        onConfirm={handleConfirm}
        title="Excluir usuario"
        itemName={`usuario ${usuarioEmail}`}
        isItemPending={deleteUsuario.isPending}
        confirmText="Sim, excluir"
      />
    </>
  );
}
