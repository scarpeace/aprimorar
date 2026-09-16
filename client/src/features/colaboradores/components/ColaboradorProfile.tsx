"use client";

import { ActiveStatusIndicator } from "@/components/ui/ActiveStatusIndicator";
import { BackButton } from "@/components/ui/BackButton";
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

function ColaboradorProfileSkeleton() {
  return (
    <Card>
      <LoadingSkeleton className="h-56 w-full" />
    </Card>
  );
}

export function ColaboradorProfile({ colaboradorId }: Readonly<ColaboradorProfileProps>) {
  const colaborador = useFindColaboradorById(colaboradorId);

  if (colaborador.isLoading) {
    return <ColaboradorProfileSkeleton />;
  }

  if (colaborador.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o colaborador"
        description="A consulta do detalhe falhou para o identificador informado."
        error={colaborador.error}
      />
    );
  }

  if (!colaborador.data) {
    return <ErrorCard title="Colaborador não encontrado" description="A API respondeu sem conteúdo para este cadastro." />;
  }

  const { data } = colaborador;
  const active = data.active !== false;

  return (
    <Card>
      <CardHeader>
        <div className="flex flex-wrap items-center gap-3">
          <ActiveStatusIndicator active={active} />
          <CardTitle>{data.nome}</CardTitle>
          <span className="text-sm font-semibold text-base-content/65">{data.funcao}</span>
        </div>

        <CardActions>
          <EditarColaboradorButton colaborador={data} />
          <ColaboradorStatusButton colaboradorId={data.id} active={active} />
          <BackButton href="/colaboradores" />
        </CardActions>
      </CardHeader>

      <ColaboradorDetails colaborador={data} />
    </Card>
  );
}
