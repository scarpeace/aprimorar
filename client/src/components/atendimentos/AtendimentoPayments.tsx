"use client";

import type { AtendimentoResponse } from "@/lib/api/generated/types/AtendimentoResponse";
import { AtendimentoPaymentBadge } from "@/components/atendimentos/AtendimentoPaymentBadge";
import { DetailField } from "@/components/ui/DetailField";
import { useAtendimentoMutations } from "@/hooks/use-atendimento-mutations";
import { formatDate } from "@/lib/utils/formatter";

type AtendimentoPaymentsProps = {
  atendimento: AtendimentoResponse;
};

export function AtendimentoPayments({ atendimento }: Readonly<AtendimentoPaymentsProps>) {
  const { efetivarPagamentoAluno, efetivarRepasseColaborador } = useAtendimentoMutations();
  const isPending = efetivarPagamentoAluno.isPending || efetivarRepasseColaborador.isPending;
  const studentPaid = !!atendimento.dataPagamentoAluno;
  const collaboratorPaid = !!atendimento.dataPagamentoColaborador;

  function handleStudentPaymentChange(checked: boolean) {
    if (!checked) {
      return;
    }

    efetivarPagamentoAluno.mutate({ id: atendimento.id });
  }

  function handleCollaboratorPaymentChange(checked: boolean) {
    if (!checked) {
      return;
    }

    efetivarRepasseColaborador.mutate({ id: atendimento.id });
  }

  return (
    <div className="app-shell-card p-6">
      <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
        <div className="space-y-3">
          <h2 className="text-xl font-bold text-base-content">Pagamento</h2>
          <div className="flex flex-wrap gap-2">
            <AtendimentoPaymentBadge label="Aluno" paidAt={atendimento.dataPagamentoAluno} />
            <AtendimentoPaymentBadge label="Colab." paidAt={atendimento.dataPagamentoColaborador} />
          </div>
        </div>

        <div className="flex flex-col gap-3">
          <label
            className={`label cursor-pointer justify-start gap-3 rounded-xl border px-4 py-3 ${
              studentPaid ? "border-success/40 bg-success/5" : "border-warning/40 bg-warning/5"
            }`}
          >
            <span className="label-text text-sm text-base-content/70">Pagamento do aluno</span>
            <input
              type="checkbox"
              className={`checkbox checkbox-sm ${
                studentPaid ? "checkbox-success" : "checkbox-warning"
              }`}
              checked={studentPaid}
              disabled={isPending}
              onChange={(event) => handleStudentPaymentChange(event.target.checked)}
            />
          </label>

          <label
            className={`label cursor-pointer justify-start gap-3 rounded-xl border px-4 py-3 ${
              collaboratorPaid ? "border-success/40 bg-success/5" : "border-warning/40 bg-warning/5"
            }`}
          >
            <span className="label-text text-sm text-base-content/70">Repasse do colaborador</span>
            <input
              type="checkbox"
              className={`checkbox checkbox-sm ${
                collaboratorPaid ? "checkbox-success" : "checkbox-warning"
              }`}
              checked={collaboratorPaid}
              disabled={isPending}
              onChange={(event) => handleCollaboratorPaymentChange(event.target.checked)}
            />
          </label>
        </div>
      </div>

      <div className="mt-6 grid gap-5 md:grid-cols-2">
        <DetailField label="Pagamento do aluno" value={formatDate(atendimento.dataPagamentoAluno)} />
        <DetailField label="Pagamento do colaborador" value={formatDate(atendimento.dataPagamentoColaborador)} />
        <DetailField label="ID do aluno" value={atendimento.alunoId} />
        <DetailField label="ID do colaborador" value={atendimento.colaboradorId} />
      </div>
    </div>
  );
}
