"use client";

import Link from "next/link";
import { useState } from "react";
import { ResponsavelAlunos } from "@/components/responsaveis/ResponsavelAlunos";
import { useGetResponsavelById } from "@/lib/api/generated/hooks/responsavel/useGetResponsavelById";
import { ResponsavelForm } from "@/components/responsaveis/ResponsavelForm";
import { Button } from "@/components/ui/Button";
import { DetailField } from "@/components/ui/DetailField";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { formatCpf, formatDate, formatPhone } from "@/lib/utils/formatter";

export function ResponsavelDetails({ responsavelId }: Readonly<{ responsavelId: string }>) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const responsavel = useGetResponsavelById(responsavelId);

  if (responsavel.isLoading) {
    return <PageLoading message="Carregando responsável..." />;
  }

  if (responsavel.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o responsável"
        description="A consulta do detalhe falhou para o identificador informado."
        error={responsavel.error}
      />
    );
  }

  if (!responsavel.data) {
    return (
      <ErrorCard title="Responsável não encontrado" description="A API respondeu sem conteúdo para este cadastro." />
    );
  }

  const { data } = responsavel;

  return (
    <div className="space-y-6">
      <Modal
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
        title="Editar responsável"
        description="Atualize os dados do responsável sem sair da tela de detalhe."
        size="md"
      >
        <ResponsavelForm initialData={data} onSuccess={() => setIsEditOpen(false)} onCancel={() => setIsEditOpen(false)} />
      </Modal>

      <section className="app-shell-card p-6">
        <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div className="space-y-3">
            <h2 className="text-xl font-bold text-base-content">Dados cadastrais</h2>
            <div className="space-y-1">
              <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Responsável</p>
              <p className="text-2xl font-bold text-base-content">{data.nome}</p>
            </div>
          </div>

          <div className="flex flex-wrap gap-2">
            <Button type="button" size="sm" variant="primary" onClick={() => setIsEditOpen(true)}>
              Editar
            </Button>

            <Link className="btn btn-outline btn-sm" href="/responsaveis">
              Voltar
            </Link>
          </div>
        </div>

        <div className="mt-6 grid gap-5 md:grid-cols-2">
          <DetailField label="CPF" value={formatCpf(data.cpf)} />
          <DetailField label="Telefone" value={formatPhone(data.telefone)} />
          <DetailField label="E-mail" value={data.email} />
          <DetailField label="Data de nascimento" value={formatDate(data.dataNascimento)} />
          <DetailField label="Criado em" value={formatDate(data.createdAt)} />
        </div>
      </section>

      <ResponsavelAlunos responsavelId={responsavelId} />
    </div>
  );
}
