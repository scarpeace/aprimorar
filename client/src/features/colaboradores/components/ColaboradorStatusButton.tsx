"use client";

import { Power, PowerOff } from "lucide-react";
import { Button } from "@/components/ui/Button";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { useColaboradorMutations } from "@/features/colaboradores/hooks/use-colaborador-mutations";

type ColaboradorStatusButtonProps = {
  colaboradorId: string;
  active: boolean;
};

export function ColaboradorStatusButton({ colaboradorId, active }: Readonly<ColaboradorStatusButtonProps>) {
  const { activateColaborador, deactivateColaborador } = useColaboradorMutations();
  const statusMutation = active ? deactivateColaborador : activateColaborador;

  function handleStatusToggle() {
    const actionLabel = active ? "desativar" : "ativar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este colaborador?`)) {
      return;
    }

    statusMutation.mutate({ colaboradorId });
  }

  return (
    <Button
      type="button"
      size="sm"
      variant={active ? "warning" : "success"}
      disabled={statusMutation.isPending}
      onClick={handleStatusToggle}
    >
      {statusMutation.isPending ? (
        <LoadingSpinner />
      ) : active ? (
        <PowerOff size={16} />
      ) : (
        <Power size={16} />
      )}
      {active ? "Desativar" : "Ativar"}
    </Button>
  );
}
