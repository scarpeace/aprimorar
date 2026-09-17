"use client";

import { Banknote, Ban, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/Button";
import { EditarDespesaButton } from "@/features/despesas/components/EditarDespesaButton";
import { useDespesaMutations } from "@/features/despesas/hooks/use-despesa-mutations";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";

type DespesaActionsProps = {
  despesa: DespesaResponse;
};

export function DespesaActions({ despesa }: Readonly<DespesaActionsProps>) {
  const router = useRouter();
  const { deleteDespesa, payDespesa, cancelPaymentDespesa } = useDespesaMutations();
  const isPaymentPending = payDespesa.isPending || cancelPaymentDespesa.isPending;
  const isPaid = despesa.status === "PAGA";

  function togglePayment() {
    if (!despesa.id) {
      return;
    }

    const mutation = isPaid ? cancelPaymentDespesa : payDespesa;
    mutation.mutate({ despesaId: despesa.id });
  }

  function remove() {
    if (!despesa.id || !window.confirm("Deseja mesmo excluir esta despesa?")) {
      return;
    }

    deleteDespesa.mutate(
      { despesaId: despesa.id },
      {
        onSuccess: () => router.push("/financeiro"),
      },
    );
  }

  return (
    <>
      <EditarDespesaButton despesa={despesa} />

      <Button
        type="button"
        size="sm"
        variant={isPaid ? "warning" : "success"}
        disabled={isPaymentPending}
        onClick={togglePayment}
      >
        {isPaid ? <Ban size={18} /> : <Banknote size={18} />}
        {isPaid ? "Cancelar pagamento" : "Registrar pagamento"}
      </Button>

      <Button type="button" size="sm" variant="error" disabled={deleteDespesa.isPending} onClick={remove}>
        <Trash2 size={18} />
        Excluir
      </Button>
    </>
  );
}
