"use client";

import { ActiveStatusIndicator } from "@/components/ui/ActiveStatusIndicator";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { AlunoDetails } from "@/features/alunos/components/AlunoDetails";
import { AlunoStatusButton } from "@/features/alunos/components/AlunoStatusButton";
import { EditarAlunoButton } from "@/features/alunos/components/EditarAlunoButton";
import { useGetAlunoById } from "@/lib/api/generated/hooks/aluno/useGetAlunoById";

type AlunoProfileProps = {
  alunoId: string;
};

export function AlunoProfile({ alunoId }: Readonly<AlunoProfileProps>) {
  const aluno = useGetAlunoById(alunoId);

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Cadastro</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Consulte e gerencie os dados cadastrais deste aluno.</p>
        </div>

        {aluno.data ? (
          <CardActions>
            <EditarAlunoButton aluno={aluno.data} />
            <AlunoStatusButton alunoId={aluno.data.id} active={aluno.data.active !== false} />
          </CardActions>
        ) : null}
      </CardHeader>

      {aluno.isLoading ? (
        <LoadingSkeleton className="h-56 w-full" />
      ) : aluno.error ? (
        <ErrorCard
          title="Não foi possível carregar o aluno"
          description="A consulta do detalhe falhou para o identificador informado."
          error={aluno.error}
        />
      ) : aluno.data ? (
        <>
          <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
            <div className="flex flex-wrap items-center justify-center gap-3 mt-3">
              <ActiveStatusIndicator active={aluno.data.active !== false} />
              <h2 className="text-xl font-bold uppercase text-base-content">{aluno.data.nome}</h2>
              <span className="text-sm font-semibold text-base-content/65">{aluno.data.escola}</span>
            </div>
          </div>

          <AlunoDetails aluno={aluno.data} />
        </>
      ) : (
        <ErrorCard title="Aluno não encontrado" description="A API respondeu sem conteúdo para este cadastro." />
      )}
    </Card>
  );
}
