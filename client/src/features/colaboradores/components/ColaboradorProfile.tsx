"use client";

import { ActiveStatusIndicator } from "@/components/ui/ActiveStatusIndicator";

import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";

import { ColaboradorDetails } from "@/features/colaboradores/components/ColaboradorDetails";
import { ColaboradorStatusButton } from "@/features/colaboradores/components/ColaboradorStatusButton";
import { EditarColaboradorButton } from "@/features/colaboradores/components/EditarColaboradorButton";
import { useFindColaboradorById } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";

type ColaboradorProfileProps = {
  colaboradorId: string;
};

export function ColaboradorProfile({ colaboradorId }: Readonly<ColaboradorProfileProps>) {
  const colaborador = useFindColaboradorById(colaboradorId);

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Cadastro</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Consulte e gerencie os dados cadastrais deste colaborador.</p>
        </div>
      </CardHeader>

      {colaborador.isLoading ? (
        <LoadingSkeleton className="h-56 w-full" />
      ) : colaborador.error ? (
        <ErrorCard
          title="Não foi possível carregar o colaborador"
          description="A consulta do detalhe falhou para o identificador informado."
          error={colaborador.error}
        />
      ) : colaborador.data ? (
        <>
          <div className="flex flex-col gap-4 mt-3 md:flex-row md:items-start md:justify-between">
            <div className="flex flex-wrap items-center justify-center gap-3">
              <ActiveStatusIndicator active={colaborador.data.active !== false} />
              <h2 className="text-xl font-bold uppercase text-base-content">{colaborador.data.nome}</h2>
              <span className="text-sm font-semibold text-base-content/65">{colaborador.data.funcao}</span>
            </div>

            <CardActions>
              <EditarColaboradorButton colaborador={colaborador.data} />
              <ColaboradorStatusButton colaboradorId={colaborador.data.id} active={colaborador.data.active !== false} />
            </CardActions>
          </div>

          <ColaboradorDetails colaborador={colaborador.data} />
        </>
      ) : (
        <ErrorCard title="Colaborador não encontrado" description="A API respondeu sem conteúdo para este cadastro." />
      )}
    </Card>
  );
}
