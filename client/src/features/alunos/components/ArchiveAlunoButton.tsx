import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { ArchiveIcon, ArchiveRestoreIcon } from "lucide-react";
import { useState } from "react";

import { useAlunoMutations } from "../hooks/use-aluno-mutations";

type ArchiveAlunoButtonProps = {
  alunoId: string;
  active: boolean;
  className?: string;
};

export const ArchiveAlunoButton = ({
  alunoId,
  active,
  className,
}: ArchiveAlunoButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const {
    archiveAluno: { mutate: archiveAluno, isPending: isArchivingAluno },
    unarchiveAluno: {
      mutate: unarchiveAluno,
      isPending: isUnarchivingAluno,
    },
  } = useAlunoMutations();

  const isPending = isArchivingAluno || isUnarchivingAluno;
  const actionLabel = active ? "Arquivar" : "Desarquivar";
  const Icon = active ? ArchiveRestoreIcon : ArchiveIcon;
  const variant = active ? "warning" : "outline";
  const modalVariant = active ? "warning" : "info";

  function handleConfirm() {
    const action = active ? archiveAluno : unarchiveAluno;
    action(
      { alunoId },
      {
        onSettled: () => setShowConfirm(false),
      }
    );
  }

  return (
    <>
      <Button
        type="button"
        onClick={() => setShowConfirm(true)}
        variant={variant}
        disabled={isPending}
        className={className}
      >
        <Icon className="h-4 w-4" />
        {isPending ? "Processando..." : actionLabel}
      </Button>

      <ConfirmModal
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={handleConfirm}
        title={`${actionLabel} Aluno`}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o aluno?`}
        isPending={isPending}
        confirmText="Sim"
        cancelText="Cancelar"
        variant={modalVariant}
      />
    </>
  );
};
