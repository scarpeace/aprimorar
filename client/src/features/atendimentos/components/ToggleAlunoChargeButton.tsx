import { Button } from "@/components/ui/button";
import type { ProblemResponseDTO, ToggleStudentAtendimentoChargeMutationResponse } from "@/kubb";
import type { ResponseErrorConfig } from "@kubb/plugin-client/clients/axios";
import type { UseMutationResult } from "@tanstack/react-query";
import { Check, Clock3 } from "lucide-react";

interface ToggleAlunoChargeButtonProps {
  alunoChargePaid: boolean;
  toggleStudentCharge: UseMutationResult<
    ToggleStudentAtendimentoChargeMutationResponse,
    ResponseErrorConfig<ProblemResponseDTO>,
    { id: string },
    unknown
  >;
  handleToggleIncomeStatus: () => void;
}

export function ToggleAlunoChargeButton({
  alunoChargePaid,
  toggleStudentCharge,
  handleToggleIncomeStatus,
}: ToggleAlunoChargeButtonProps) {
  return (
    <Button
      size="sm"
      variant={alunoChargePaid ? "outline" : "success"}
      onClick={handleToggleIncomeStatus}
      disabled={toggleStudentCharge.isPending}
      className="w-full sm:w-auto"
    >
      {alunoChargePaid ? (
        <Clock3 className="mr-1 h-4 w-4" />
      ) : (
        <Check className="mr-1 h-4 w-4" />
      )}
      {alunoChargePaid ? "Remover cobrança do aluno" : "Registrar cobrança do aluno"}
    </Button>
  );
}
