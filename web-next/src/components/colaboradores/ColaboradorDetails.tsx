"use client";

import Link from "next/link";
import { useState } from "react";
import { useFindColaboradorById } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";
import { ColaboradorForm } from "@/components/colaboradores/ColaboradorForm";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { formatCpf, formatPhone, formatZip } from "@/lib/utils/formatter";

function formatDate(value?: string | null) {
  if (!value) {
    return "—";
  }

  return new Intl.DateTimeFormat("pt-BR").format(new Date(value));
}

function Field({ label, value }: Readonly<{ label: string; value?: string | number | null }>) {
  return (
    <div className="space-y-1">
      <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">{label}</p>
      <p className="text-sm text-base-content">{value || "—"}</p>
    </div>
  );
}

export function ColaboradorDetails({ colaboradorId }: Readonly<{ colaboradorId: string }>) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const colaborador = useFindColaboradorById(colaboradorId);

  if (colaborador.isLoading) {
    return <PageLoading message="Carregando colaborador..." />;
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
    return (
      <ErrorCard title="Colaborador não encontrado" description="A API respondeu sem conteúdo para este cadastro." />
    );
  }

  const { data } = colaborador;
  const endereco = data.endereco;
  const active = data.active !== false;

  return (
    <div className="space-y-6">
      <section className="app-shell-card p-6 md:p-8">
        <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Colaborador</p>
            <h1 className="mt-2 text-3xl font-bold text-base-content">{data.nome}</h1>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Dados cadastrais e endereço do colaborador selecionado.
            </p>
          </div>

          <div className="flex flex-col items-start gap-3">
            <Badge variant={active ? "success" : "ghost"}>{active ? "Ativo" : "Arquivado"}</Badge>

            <div className="flex flex-wrap gap-2">
              <Button type="button" size="sm" variant="primary" onClick={() => setIsEditOpen(true)}>
                Editar
              </Button>

              <Link className="btn btn-outline btn-sm" href="/colaboradores">
                Voltar para colaboradores
              </Link>
            </div>
          </div>
        </div>
      </section>

      <Modal
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
        title="Editar colaborador"
        description="Atualize os dados do colaborador sem sair da tela de detalhe."
        size="lg"
      >
        <ColaboradorForm initialData={data} onSuccess={() => setIsEditOpen(false)} onCancel={() => setIsEditOpen(false)} />
      </Modal>

      <section className="grid gap-6 xl:grid-cols-2">
        <div className="app-shell-card p-6">
          <h2 className="text-xl font-bold text-base-content">Dados cadastrais</h2>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <Field label="Nome" value={data.nome} />
            <Field label="CPF" value={formatCpf(data.cpf)} />
            <Field label="Telefone" value={formatPhone(data.telefone)} />
            <Field label="E-mail" value={data.email} />
            <Field label="Data de nascimento" value={formatDate(data.dataNascimento)} />
            <Field label="Função" value={data.funcao} />
            <Field label="PIX" value={data.pix} />
            <Field label="Criado em" value={formatDate(data.createdAt)} />
          </div>
        </div>

        <div className="app-shell-card p-6">
          <h2 className="text-xl font-bold text-base-content">Endereço</h2>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <Field label="Rua" value={endereco.rua} />
            <Field label="Número" value={endereco.numero} />
            <Field label="Complemento" value={endereco.complemento} />
            <Field label="Bairro" value={endereco.bairro} />
            <Field label="Cidade" value={endereco.cidade} />
            <Field label="UF" value={endereco.estado} />
            <Field label="CEP" value={formatZip(endereco.cep)} />
          </div>
        </div>
      </section>
    </div>
  );
}
