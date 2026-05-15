import { Button } from "@/components/ui/button";
import type { ToggleExpensePaymentMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Check, Clock3 } from "lucide-react";

type ToggleExpensePaymentButtonProps = {
  isPaid: boolean;
  toggleExpensePayment: UseMutationResult<
    ToggleExpensePaymentMutationResponse,
    ResponseErrorConfig<Error>,
    { id: string },
    unknown
  >;
  onTogglePayment: () => void;
};

export function ToggleExpensePaymentButton({
  isPaid,
  toggleExpensePayment,
  onTogglePayment,
}: Readonly<ToggleExpensePaymentButtonProps>) {
  return (
    <Button
      type="button"
      size="sm"
      variant={isPaid ? "outline" : "success"}
      onClick={onTogglePayment}
      disabled={toggleExpensePayment.isPending}
      className="w-full sm:w-auto"
    >
      {isPaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {isPaid ? "Remover pagamento" : "Marcar como paga"}
    </Button>
  );
}
