"use client";

import { Power, PowerOff } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useUserMutations } from "@/features/usuarios/hooks/use-user-mutations";
import type { UserListResponse } from "@/lib/api/generated/types/UserListResponse";

type UsuarioStatusButtonProps = {
  user: UserListResponse;
};

export function UsuarioStatusButton({ user }: Readonly<UsuarioStatusButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const { activateUser, deactivateUser } = useUserMutations();
  const isEnabled = user.enabled === true;
  const statusMutation = isEnabled ? deactivateUser : activateUser;

  function confirm() {
    statusMutation.mutate(
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
        variant={isEnabled ? "warning" : "success"}
        disabled={statusMutation.isPending}
        onClick={() => setIsOpen(true)}
      >
        {isEnabled ? <PowerOff size={16} /> : <Power size={16} />}
        {isEnabled ? "Desativar" : "Ativar"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        title={isEnabled ? "Desativar usuário" : "Ativar usuário"}
        description={
          isEnabled
            ? `${user.email} não poderá mais acessar o sistema.`
            : `${user.email} voltará a ter acesso ao sistema.`
        }
        confirmLabel={isEnabled ? "Desativar" : "Ativar"}
        pendingLabel={isEnabled ? "Desativando..." : "Ativando..."}
        variant={isEnabled ? "warning" : "success"}
        isPending={statusMutation.isPending}
        onConfirm={confirm}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
}
