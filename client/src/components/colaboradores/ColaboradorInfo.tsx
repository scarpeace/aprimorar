"use client";

import Link from "next/link";
import { Archive, ArchiveRestore, PencilLine, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { ColaboradorForm } from "@/components/colaboradores/ColaboradorForm";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { DetailField } from "@/components/ui/DetailField";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { useColaboradorMutations } from "@/hooks/use-colaborador-mutations";
import { useFindColaboradorById } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";
import { formatCpf, formatDate, formatPhone, formatZip } from "@/lib/utils/formatter";

export function ColaboradorInfo({ colaboradorId }: Readonly<{ colaboradorId: string }>) {
  const router = useRouter();
  const [isEditOpen, setIsEditOpen] = useState(false);
  const colaborador = useFindColaboradorById(colaboradorId);
  const { archiveColaborador, unarchiveColaborador, deleteColaborador } = useColaboradorMutations();

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
    return <ErrorCard title="Colaborador não encontrado" description="A API respondeu sem conteúdo para este cadastro." />;
  }

  const { data } = colaborador;
  const endereco = data.endereco;
  const active = data.active !== false;
  const isArchivePending = archiveColaborador.isPending || unarchiveColaborador.isPending;
  const isPending = isArchivePending || deleteColaborador.isPending;

  function handleArchiveToggle() {
    const actionLabel = active ? "arquivar" : "desarquivar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este colaborador?`)) {
      return;
    }

    const action = active ? archiveColaborador : unarchiveColaborador;
    action.mutate({ colaboradorId });
  }

  function handleDelete() {
    if (!window.confirm("Deseja mesmo excluir este colaborador? Esta ação não pode ser desfeita.")) {
      return;
    }

    deleteColaborador.mutate(
      { colaboradorId },
      {
        onSuccess: () => {
          router.push("/colaboradores");
        },
      },
    );
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

      <section className="app-shell-card p-6">
        <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div className="flex flex-col">
            <div className="flex gap-3">
              <h2 className="text-2xl font-bold uppercase text-base-content">{data.nome}</h2>
              <Badge variant={active ? "success" : "ghost"}>{active ? "Ativo" : "Arquivado"}</Badge>
            </div>
            <p className="text-base font-bold text-base-content/75">{data.funcao}</p>
          </div>

          <div className="flex flex-wrap gap-2">
            <Button
              type="button"
              size="sm"
              variant="primary"
              aria-label="Editar colaborador"
              title="Editar colaborador"
              onClick={() => setIsEditOpen(true)}
            >
              <PencilLine size={18} />
            </Button>

            <Button
              type="button"
              size="sm"
              variant={active ? "warning" : "outline"}
              disabled={isPending}
              onClick={handleArchiveToggle}
            >
              {isArchivePending ? <LoadingSpinner /> : active ? <Archive size={16} /> : <ArchiveRestore size={16} />}
            </Button>

            <Button
              type="button"
              size="sm"
              variant="error"
              aria-label="Excluir colaborador"
              title="Excluir colaborador"
              disabled={isPending}
              onClick={handleDelete}
            >
              <Trash2 size={18} />
            </Button>

            <Link className="btn btn-outline btn-sm" href="/colaboradores">
              Voltar
            </Link>
          </div>
        </div>

        <div className="mt-6 rounded-2xl border border-base-300 bg-base-100 p-5">
          <h3 className="text-lg font-bold text-base-content">Dados cadastrais</h3>

          <div className="mt-5 grid gap-6 lg:grid-cols-2 lg:gap-8">
            <div className="space-y-4">
              <h4 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Dados pessoais</h4>

              <div className="grid gap-5 sm:grid-cols-2">
                <DetailField label="CPF" value={formatCpf(data.cpf)} />
                <DetailField label="Telefone" value={formatPhone(data.telefone)} />
                <DetailField label="E-mail" value={data.email} />
                <DetailField label="Data de nascimento" value={formatDate(data.dataNascimento)} />
                <DetailField label="PIX" value={data.pix} />
                <DetailField label="Criado em" value={formatDate(data.createdAt)} />
              </div>
            </div>

            <div className="space-y-4 lg:border-l lg:border-base-300 lg:pl-8">
              <h4 className="text-sm font-bold uppercase tracking-wider text-base-content/60">Endereço</h4>

              <div className="grid gap-5 sm:grid-cols-2">
                <DetailField label="Rua" value={endereco.rua} />
                <DetailField label="Número" value={endereco.numero} />
                <DetailField label="Complemento" value={endereco.complemento} />
                <DetailField label="Bairro" value={endereco.bairro} />
                <DetailField label="Cidade" value={endereco.cidade} />
                <DetailField label="UF" value={endereco.estado} />
                <DetailField label="CEP" value={formatZip(endereco.cep)} />
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
