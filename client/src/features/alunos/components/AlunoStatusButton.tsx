"use client";

import { Power, PowerOff } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useAlunoMutations } from "@/features/alunos/hooks/use-aluno-mutations";

type AlunoStatusButtonProps = {
  alunoId: string;
  active: boolean;
};

export function AlunoStatusButton({ alunoId, active }: Readonly<AlunoStatusButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const { activateAluno, deactivateAluno } = useAlunoMutations();
  const statusMutation = active ? deactivateAluno : activateAluno;

  function confirm() {
    statusMutation.mutate(
      { alunoId },
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
        title={active ? "Desativar aluno" : "Ativar aluno"}
        description={
          active
            ? "O aluno não poderá receber novos atendimentos enquanto estiver inativo."
            : "O aluno voltará a ficar disponível para novos atendimentos."
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
