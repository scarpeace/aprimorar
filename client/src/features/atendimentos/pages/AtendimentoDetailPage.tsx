import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetAtendimentoById } from "@/kubb";
import { Calendar, Edit } from "lucide-react";
import { useParams } from "react-router-dom";
import { tipoAtendimentoLabels } from "@/features/atendimentos/lib/tipo-atendimento-labels";
import { useState } from "react";
import { AtendimentoForm } from "../components/AtendimentoForm";
import { AtendimentoInfoSection } from "../components/AtendimentoInfoSection";

export function AtendimentoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const atendimentoId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const atendimentoQuery = useGetAtendimentoById(atendimentoId);

  const headerProps = {
    description: "Veja e gerencie as informações do atendimento",
    Icon: Calendar,
    title: "Detalhes do atendimento",
    iconBg: "secondary",
  } as const;

  if (atendimentoQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar atendimento"
          error={atendimentoQuery.error}
        />
      </PageLayout>
    );
  }

  if (atendimentoQuery.isPending || !atendimentoQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do atendimento" />
      </PageLayout>
    );
  }

  const contentLabel = tipoAtendimentoLabels[atendimentoQuery.data.tipo] || atendimentoQuery.data.tipo;

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_300ms_ease-out_both] lg:justify-between">

        <div className="flex justify-between items-center mb-3">
          <div className="flex flex-row gap-3">
              <Badge variant="primary" className="px-3 py-1 font-semibold">
                {contentLabel}
              </Badge>
              <Badge variant="neutral" className="px-3 py-1 font-semibold">
                {atendimentoQuery.data.status}
              </Badge>
            </div>


              <div className="flex flex-row gap-2 sm:flex-row sm:flex-wrap">
                <Button
                  onClick={() => setIsFormOpen(true)}
                  variant="primary"
                  size="sm"
                  className="w-full sm:w-auto"
                >
                  <Edit className="mr-1 h-4 w-4" /> Editar
                </Button>
              </div>
        </div>

          <AtendimentoInfoSection atendimento={atendimentoQuery.data} />
        </section>

    </PageLayout>
  );
}
