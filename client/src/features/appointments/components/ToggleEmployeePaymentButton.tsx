import { Button } from "@/components/ui/button";
import type { ToggleEmployeeAppointmentPaymentMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Clock3, Check } from "lucide-react";

interface ToggleEmployeePaymentButtonProps {
  employeePaymentPaid: boolean;
  toggleEmployeePayment: UseMutationResult<
    ToggleEmployeeAppointmentPaymentMutationResponse,
    ResponseErrorConfig<Error>,
    { id: string },
    unknown
  >;
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
