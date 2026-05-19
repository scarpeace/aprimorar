import { Button } from "@/components/ui/button";
import { Check, Clock3 } from "lucide-react";
import { useExpenseMutations } from "../hooks/useExpenseMutations";

type ToggleExpensePaymentButtonProps = {
  expenseId: string;
  isPaid: boolean;
  iconOnly?: boolean;
};

export function ToggleExpensePaymentButton({
  expenseId,
  isPaid,
  iconOnly = false,
}: Readonly<ToggleExpensePaymentButtonProps>) {
  const { toggleExpensePayment } = useExpenseMutations();
  const label = isPaid ? "Remover pagamento" : "Marcar como paga";

  return (
    <Button
      type="button"
      size="sm"
      variant={isPaid ? "outline" : "success"}
      onClick={() => toggleExpensePayment.mutate({ id: expenseId })}
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
