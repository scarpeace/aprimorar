"use client";

import { Power, PowerOff, Trash2 } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { useUserMutations } from "@/features/usuarios/hooks/use-user-mutations";
import type { UserListResponse } from "@/lib/api/generated/types/UserListResponse";

type UsuarioActionsProps = {
  user: UserListResponse;
};

type Confirmation = "status" | "delete" | null;

export function UsuarioActions({ user }: Readonly<UsuarioActionsProps>) {
  const [confirmation, setConfirmation] = useState<Confirmation>(null);
  const { activateUser, deactivateUser, deleteUser } = useUserMutations();
  const isEnabled = user.enabled === true;
  const statusMutation = isEnabled ? deactivateUser : activateUser;
  const isPending = statusMutation.isPending || deleteUser.isPending;

  if (user.role === "ADMIN") {
    return <span className="text-sm text-base-content/45">Protegido</span>;
  }

  function changeStatus() {
    statusMutation.mutate(
      { userId: user.id },
      {
        onSuccess: () => setConfirmation(null),
      },
    );
  }

  function remove() {
    deleteUser.mutate(
      { userId: user.id },
      {
        onSuccess: () => setConfirmation(null),
      },
    );
  }

  return (
    <>
      <div className="flex justify-end gap-2">
        <Button
          type="button"
          size="xs"
          variant={isEnabled ? "warning" : "success"}
          disabled={isPending}
          onClick={() => setConfirmation("status")}
        >
          {isEnabled ? <PowerOff size={16} /> : <Power size={16} />}
          {isEnabled ? "Desativar" : "Ativar"}
        </Button>

        <Button
          type="button"
          size="xs"
          variant="error"
          disabled={isPending}
          onClick={() => setConfirmation("delete")}
        >
          <Trash2 size={16} />
          Excluir
        </Button>
      </div>

      <Modal
        isOpen={confirmation === "status"}
        onClose={() => setConfirmation(null)}
        title={`${isEnabled ? "Desativar" : "Ativar"} usuário`}
        description={`Confirme a alteração de acesso para ${user.email}.`}
        size="sm"
      >
        <div className="flex justify-end gap-3">
          <Button type="button" variant="outline" disabled={isPending} onClick={() => setConfirmation(null)}>
            Cancelar
          </Button>
          <Button
            type="button"
            variant={isEnabled ? "warning" : "success"}
            disabled={isPending}
            onClick={changeStatus}
          >
            {statusMutation.isPending ? "Processando..." : isEnabled ? "Desativar" : "Ativar"}
          </Button>
        </div>
      </Modal>

      <Modal
        isOpen={confirmation === "delete"}
        onClose={() => setConfirmation(null)}
        title="Excluir usuário"
        description={`Esta ação removerá permanentemente o acesso de ${user.email}.`}
        size="sm"
      >
        <div className="flex justify-end gap-3">
          <Button type="button" variant="outline" disabled={isPending} onClick={() => setConfirmation(null)}>
            Cancelar
          </Button>
          <Button type="button" variant="error" disabled={isPending} onClick={remove}>
            {deleteUser.isPending ? "Excluindo..." : "Excluir usuário"}
          </Button>
        </div>
      </Modal>
    </>
  );
}
