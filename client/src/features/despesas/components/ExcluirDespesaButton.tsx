"use client";

import { Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useDespesaMutations } from "@/features/despesas/hooks/use-despesa-mutations";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";

type ExcluirDespesaButtonProps = {
  despesa: DespesaResponse;
};

export function ExcluirDespesaButton({ despesa }: Readonly<ExcluirDespesaButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const router = useRouter();
  const { deleteDespesa } = useDespesaMutations();

  function confirm() {
    if (!despesa.id) {
      return;
    }

    deleteDespesa.mutate(
      { despesaId: despesa.id },
      {
        onSuccess: () => {
          setIsOpen(false);
          router.push("/financeiro");
        },
      },
    );
  }

  return (
    <>
      <Button
        type="button"
        size="sm"
        variant="error"
        disabled={deleteDespesa.isPending || !despesa.id}
        onClick={() => setIsOpen(true)}
      >
        <Trash2 size={18} />
        Excluir
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        title="Excluir despesa"
        description="Esta ação removerá permanentemente o lançamento financeiro."
        confirmLabel="Excluir despesa"
        pendingLabel="Excluindo..."
        variant="error"
        isPending={deleteDespesa.isPending}
        onConfirm={confirm}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
}
