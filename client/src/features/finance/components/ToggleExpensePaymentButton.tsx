import { Button } from "@/components/ui/button";
import type { ToggleExpensePaymentMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Check, Clock3 } from "lucide-react";

type ToggleExpensePaymentButtonProps = {
  isPaid: boolean;
  iconOnly?: boolean;
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
  iconOnly = false,
  toggleExpensePayment,
  onTogglePayment,
}: Readonly<ToggleExpensePaymentButtonProps>) {
  const label = isPaid ? "Remover pagamento" : "Marcar como paga";

  return (
    <Button
      type="button"
      size="sm"
      variant={isPaid ? "outline" : "success"}
      onClick={onTogglePayment}
      disabled={toggleExpensePayment.isPending}
      title={label}
      aria-label={label}
      className={iconOnly ? "btn-square" : "w-full sm:w-auto"}
    >
      {isPaid ? (
        <Clock3 className={`${iconOnly ? "" : "mr-1"} h-4 w-4`} />
      ) : (
        <Check className={`${iconOnly ? "" : "mr-1"} h-4 w-4`} />
      )}
      {iconOnly ? null : label}
    </Button>
  );
}
