import { Button } from "@/components/ui/button";
import type { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { Check, Clock3 } from "lucide-react";

type ToggleColaboradorPaymentButtonProps = {
  colaboradorPaymentPaid: boolean;
  alternarPagamentoColaborador: ReturnType<typeof useAtendimentoMutations>["alternarPagamentoColaborador"];
  handleToggleEmployeePayment: () => void;
};

export function ToggleColaboradorPaymentButton({
  colaboradorPaymentPaid,
  alternarPagamentoColaborador,
  handleToggleEmployeePayment,
}: Readonly<ToggleColaboradorPaymentButtonProps>) {
  return (
    <Button
      size="sm"
      variant={colaboradorPaymentPaid ? "outline" : "success"}
      onClick={handleToggleEmployeePayment}
      disabled={alternarPagamentoColaborador.isPending}
      className="w-full sm:w-auto"
    >
      {colaboradorPaymentPaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {colaboradorPaymentPaid ? "Marcar repasse pendente" : "Marcar colaborador pago"}
    </Button>
  );
}
