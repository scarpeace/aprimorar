import { Button } from "@/components/ui/button";
import { Check, Clock3 } from "lucide-react";
import { useDespesaMutations } from "../hooks/use-despesa-mutations";

type ToggleDespesaPaymentButtonProps = {
  despesaId: string;
  isPaid: boolean;
  iconOnly?: boolean;
};

export function ToggleDespesaPaymentButton({
  despesaId,
  isPaid,
  iconOnly = false,
}: Readonly<ToggleDespesaPaymentButtonProps>) {
  const { toggleExpensePayment } = useDespesaMutations();
  const label = isPaid ? "Remover pagamento" : "Marcar como paga";

  return (
    <Button
      type="button"
      size="sm"
      variant={isPaid ? "outline" : "success"}
      onClick={() => toggleExpensePayment.mutate({ id: despesaId })}
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
