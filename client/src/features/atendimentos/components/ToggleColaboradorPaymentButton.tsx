import { Button } from "@/components/ui/button";
import type { ProblemResponseDTO, ToggleEmployeeAtendimentoPaymentMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Clock3, Check } from "lucide-react";

interface ToggleColaboradorPaymentButtonProps {
  colaboradorPaymentPaid: boolean;
  toggleEmployeePayment: UseMutationResult<
    ToggleEmployeeAtendimentoPaymentMutationResponse,
    ResponseErrorConfig<ProblemResponseDTO>,
    { id: string },
    unknown
  >;
  handleToggleEmployeePayment: () => void;
}

export function ToggleColaboradorPaymentButton({
  colaboradorPaymentPaid,
  toggleEmployeePayment,
  handleToggleEmployeePayment,
}: ToggleColaboradorPaymentButtonProps) {
  return (
    <Button
      size="sm"
      variant={colaboradorPaymentPaid ? "outline" : "success"}
      onClick={handleToggleEmployeePayment}
      disabled={toggleEmployeePayment.isPending}
      className="w-full sm:w-auto"
    >
      {colaboradorPaymentPaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {colaboradorPaymentPaid ? "Funcionário pendente" : "Marcar funcionário pago"}
    </Button>
  );
}
