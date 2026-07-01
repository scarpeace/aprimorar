"use client";

import Link from "next/link";
import { useState } from "react";
import { useFindColaboradorById } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";
import { ColaboradorForm } from "@/components/colaboradores/ColaboradorForm";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { DetailField } from "@/components/ui/DetailField";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { useColaboradorMutations } from "@/hooks/use-colaborador-mutations";
import { formatCpf, formatDate, formatPhone, formatZip } from "@/lib/utils/formatter";

export function ColaboradorDetails({ colaboradorId }: Readonly<{ colaboradorId: string }>) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const colaborador = useFindColaboradorById(colaboradorId);
  const { archiveColaborador, unarchiveColaborador } = useColaboradorMutations();

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
  const isPending = archiveColaborador.isPending || unarchiveColaborador.isPending;

  function handleArchiveToggle() {
    const actionLabel = active ? "arquivar" : "desarquivar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este colaborador?`)) {
      return;
    }

    const action = active ? archiveColaborador : unarchiveColaborador;
    action.mutate({ colaboradorId });
  }

  return (
    <div className="space-y-6">
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
          <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
            <div className="space-y-3">
              <h2 className="text-xl font-bold text-base-content">Dados cadastrais</h2>
              <Badge variant={active ? "success" : "ghost"}>{active ? "Ativo" : "Arquivado"}</Badge>
            </div>

            <div className="flex flex-wrap gap-2">
              <Button type="button" size="sm" variant="primary" onClick={() => setIsEditOpen(true)}>
                Editar
              </Button>

              <Button type="button" size="sm" variant={active ? "warning" : "outline"} disabled={isPending} onClick={handleArchiveToggle}>
                {isPending ? "Processando..." : active ? "Arquivar" : "Desarquivar"}
              </Button>

              <Link className="btn btn-outline btn-sm" href="/colaboradores">
                Voltar
              </Link>
            </div>
          </div>

          <div className="mt-6 grid gap-4 rounded-2xl border border-base-300 bg-base-200/25 p-4">
            <div className="space-y-1">
              <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Colaborador</p>
              <p className="text-2xl font-bold text-base-content">{data.nome}</p>
            </div>
          </div>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <DetailField label="CPF" value={formatCpf(data.cpf)} />
            <DetailField label="Telefone" value={formatPhone(data.telefone)} />
            <DetailField label="E-mail" value={data.email} />
            <DetailField label="Data de nascimento" value={formatDate(data.dataNascimento)} />
            <DetailField label="Função" value={data.funcao} />
            <DetailField label="PIX" value={data.pix} />
            <DetailField label="Criado em" value={formatDate(data.createdAt)} />
          </div>
        </div>

        <div className="app-shell-card p-6">
          <h2 className="text-xl font-bold text-base-content">Endereço</h2>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <DetailField label="Rua" value={endereco.rua} />
            <DetailField label="Número" value={endereco.numero} />
            <DetailField label="Complemento" value={endereco.complemento} />
            <DetailField label="Bairro" value={endereco.bairro} />
            <DetailField label="Cidade" value={endereco.cidade} />
            <DetailField label="UF" value={endereco.estado} />
            <DetailField label="CEP" value={formatZip(endereco.cep)} />
          </div>
        </div>
      </section>
    </div>
  );
}
