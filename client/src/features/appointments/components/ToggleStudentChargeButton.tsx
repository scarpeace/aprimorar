import { Button } from "@/components/ui/button";
import type { ProblemResponseDTO, ToggleStudentAtendimentoChargeMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Check, Clock3 } from "lucide-react";

interface ToggleStudentChargeButtonProps {
  studentChargePaid: boolean;
  toggleStudentCharge: UseMutationResult<
    ToggleStudentAtendimentoChargeMutationResponse,
    ResponseErrorConfig<ProblemResponseDTO>,
    { id: string },
    unknown
  >;
  handleToggleIncomeStatus: () => void;
}

export function ToggleStudentChargeButton({
  studentChargePaid,
  toggleStudentCharge,
  handleToggleIncomeStatus,
}: ToggleStudentChargeButtonProps) {
  return (
    <Button
      size="sm"
      variant={studentChargePaid ? "outline" : "success"}
      onClick={handleToggleIncomeStatus}
      disabled={toggleStudentCharge.isPending}
      className="w-full sm:w-auto"
    >
      {studentChargePaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {studentChargePaid ? "Remover cobrança do aluno" : "Registrar cobrança do aluno"}
    </Button>
  );
}
