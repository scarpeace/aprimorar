"use client";

import { useState } from "react";
import { AtendimentoData } from "@/components/atendimentos/AtendimentoData";
import { AtendimentoForm } from "@/components/atendimentos/AtendimentoForm";
import { AtendimentoPayments } from "@/components/atendimentos/AtendimentoPayments";
import { useGetAtendimentoById } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentoById";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";

export function AtendimentoDetails({ atendimentoId }: Readonly<{ atendimentoId: string }>) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const atendimento = useGetAtendimentoById(Number(atendimentoId));

  if (atendimento.isLoading) {
    return <PageLoading message="Carregando atendimento..." />;
  }

  if (atendimento.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o atendimento"
        description="A consulta do detalhe falhou para o identificador informado."
        error={atendimento.error}
      />
    );
  }

  if (!atendimento.data) {
    return <ErrorCard title="Atendimento não encontrado" description="A API respondeu sem conteúdo para este atendimento." />;
  }

  const { data } = atendimento;

  return (
    <div className="space-y-6">
      <Modal
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
        title="Editar atendimento"
        description="Atualize horário, participantes e valores sem sair da tela de detalhe."
        size="lg"
      >
        <AtendimentoForm initialData={data} onSuccess={() => setIsEditOpen(false)} onCancel={() => setIsEditOpen(false)} />
      </Modal>

      <section className="grid gap-6 xl:grid-cols-2">
        <AtendimentoData atendimento={data} onEdit={() => setIsEditOpen(true)} />

        <AtendimentoPayments atendimento={data} />
      </section>
    </div>
  );
}
