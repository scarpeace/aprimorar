"use client";

import { Banknote, Ban } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { ConfirmationModal } from "@/components/ui/ConfirmationModal";
import { useDespesaMutations } from "@/features/despesas/hooks/use-despesa-mutations";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";

type DespesaPagamentoButtonProps = {
  despesa: DespesaResponse;
};

export function DespesaPagamentoButton({ despesa }: Readonly<DespesaPagamentoButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);
  const { payDespesa, cancelPaymentDespesa } = useDespesaMutations();
  const isPaid = despesa.status === "PAGA";
  const paymentMutation = isPaid ? cancelPaymentDespesa : payDespesa;

  function confirm() {
    if (!despesa.id) {
      return;
    }

    paymentMutation.mutate(
      { despesaId: despesa.id },
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
        variant={isPaid ? "warning" : "success"}
        disabled={paymentMutation.isPending || !despesa.id}
        onClick={() => setIsOpen(true)}
      >
        {isPaid ? <Ban size={18} /> : <Banknote size={18} />}
        {isPaid ? "Cancelar pagamento" : "Registrar pagamento"}
      </Button>

      <ConfirmationModal
        isOpen={isOpen}
        title={isPaid ? "Cancelar pagamento" : "Registrar pagamento"}
        description={
          isPaid
            ? "A despesa voltará ao estado pendente."
            : "A despesa será marcada como paga na data atual."
        }
        confirmLabel={isPaid ? "Cancelar pagamento" : "Registrar pagamento"}
        pendingLabel={isPaid ? "Cancelando..." : "Registrando..."}
        variant={isPaid ? "warning" : "success"}
        isPending={paymentMutation.isPending}
        onConfirm={confirm}
        onClose={() => setIsOpen(false)}
      />
    </>
  );
}
