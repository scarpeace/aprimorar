"use client";

import { Trash2 } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useUserMutations } from "@/features/usuarios/hooks/use-user-mutations";
import type { UserListResponse } from "@/lib/api/generated/types/UserListResponse";

type ExcluirUsuarioButtonProps = {
  user: UserListResponse;
};

export function ExcluirUsuarioButton({ user }: Readonly<ExcluirUsuarioButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const { deleteUser } = useUserMutations();

  function confirm() {
    deleteUser.mutate(
      { userId: user.id },
      {
        onSuccess: () => setIsOpen(false),
      },
    );
  }

  return (
    <>
      <Button
        type="button"
        size="xs"
        variant="error"
        disabled={deleteUser.isPending}
        onClick={() => setIsOpen(true)}
      >
        <Trash2 size={16} />
        Excluir
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        title="Excluir usuário"
        description={`Esta ação removerá permanentemente o acesso de ${user.email}.`}
        confirmLabel="Excluir usuário"
        pendingLabel="Excluindo..."
        variant="error"
        isPending={deleteUser.isPending}
        onConfirm={confirm}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
}
