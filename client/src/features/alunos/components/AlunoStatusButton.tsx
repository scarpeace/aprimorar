"use client";

import { Power, PowerOff } from "lucide-react";
import { Button } from "@/components/ui/Button";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { useAlunoMutations } from "@/features/alunos/hooks/use-aluno-mutations";

type AlunoStatusButtonProps = {
  alunoId: string;
  active: boolean;
};

export function AlunoStatusButton({ alunoId, active }: Readonly<AlunoStatusButtonProps>) {
  const { activateAluno, deactivateAluno } = useAlunoMutations();
  const statusMutation = active ? deactivateAluno : activateAluno;

  function handleStatusToggle() {
    const actionLabel = active ? "desativar" : "ativar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este aluno?`)) {
      return;
    }

    statusMutation.mutate({ alunoId });
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
