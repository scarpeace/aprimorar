"use client";

import { Power, PowerOff } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useColaboradorMutations } from "@/features/colaboradores/hooks/use-colaborador-mutations";

type ColaboradorStatusButtonProps = {
  colaboradorId: string;
  active: boolean;
};

export function ColaboradorStatusButton({ colaboradorId, active }: Readonly<ColaboradorStatusButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const { activateColaborador, deactivateColaborador } = useColaboradorMutations();
  const statusMutation = active ? deactivateColaborador : activateColaborador;

  function confirm() {
    statusMutation.mutate(
      { colaboradorId },
      {
        onSuccess: () => setIsOpen(false),
      },
    );
  }

  return (
    <>
      <Button
        type="button"
        size="sm"
        variant={active ? "warning" : "success"}
        disabled={statusMutation.isPending}
        onClick={() => setIsOpen(true)}
      >
        {active ? <PowerOff size={16} /> : <Power size={16} />}
        {active ? "Desativar" : "Ativar"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        title={active ? "Desativar colaborador" : "Ativar colaborador"}
        description={
          active
            ? "O colaborador não poderá receber novos atendimentos enquanto estiver inativo."
            : "O colaborador voltará a ficar disponível para novos atendimentos."
        }
        confirmLabel={active ? "Desativar" : "Ativar"}
        pendingLabel={active ? "Desativando..." : "Ativando..."}
        variant={active ? "warning" : "success"}
        isPending={statusMutation.isPending}
        onConfirm={confirm}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
}
