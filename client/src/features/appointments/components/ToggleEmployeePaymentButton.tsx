import { Button } from "@/components/ui/button";
import { Clock3, Check } from "lucide-react";

interface ToggleEmployeePaymentButtonProps {
  employeePaymentPaid: boolean;
  toggleEmployeePayment: any;
  handleToggleEmployeePayment: () => void;
}

export function ToggleEmployeePaymentButton({
  employeePaymentPaid,
  toggleEmployeePayment,
  handleToggleEmployeePayment,
}: ToggleEmployeePaymentButtonProps) {
  return (
    <Button
      size="sm"
      variant={employeePaymentPaid ? "outline" : "success"}
      onClick={handleToggleEmployeePayment}
      disabled={toggleEmployeePayment.isPending}
      className="w-full sm:w-auto"
    >
      {employeePaymentPaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {employeePaymentPaid ? "Funcionário pendente" : "Marcar funcionário pago"}
    </Button>
  );
}
