import { Button } from "@/components/ui/button";
import type { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { Check, Clock3 } from "lucide-react";

type ToggleAlunoChargeButtonProps = {
  alunoChargePaid: boolean;
  alternarCobrancaAluno: ReturnType<typeof useAtendimentoMutations>["alternarCobrancaAluno"];
  handleToggleIncomeStatus: () => void;
};

export function ToggleAlunoChargeButton({
  alunoChargePaid,
  alternarCobrancaAluno,
  handleToggleIncomeStatus,
}: Readonly<ToggleAlunoChargeButtonProps>) {
  return (
    <Button
      size="sm"
      variant={alunoChargePaid ? "outline" : "success"}
      onClick={handleToggleIncomeStatus}
      disabled={alternarCobrancaAluno.isPending}
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
