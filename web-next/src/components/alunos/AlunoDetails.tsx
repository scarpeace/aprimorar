"use client";

import Link from "next/link";
import { PencilLine, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { AlunoAtendimentos } from "@/components/alunos/AlunoAtendimentos";
import { AlunoForm } from "@/components/alunos/AlunoForm";
import { useGetAlunoById } from "@/lib/api/generated/hooks/aluno/useGetAlunoById";
import { useGetResponsavelById } from "@/lib/api/generated/hooks/responsavel/useGetResponsavelById";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { DetailField } from "@/components/ui/DetailField";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { useAlunoMutations } from "@/hooks/use-aluno-mutations";
import { formatCpf, formatDate, formatPhone, formatZip } from "@/lib/utils/formatter";

export function AlunoDetails({ alunoId }: Readonly<{ alunoId: string }>) {
  const router = useRouter();
  const [isEditOpen, setIsEditOpen] = useState(false);
  const aluno = useGetAlunoById(alunoId);
  const responsavel = useGetResponsavelById(aluno.data?.responsavelId);
  const { archiveAluno, unarchiveAluno, deleteAluno } = useAlunoMutations();

  if (aluno.isLoading) {
    return <PageLoading message="Carregando aluno..." />;
  }

  if (aluno.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o aluno"
        description="A consulta do detalhe falhou para o identificador informado."
        error={aluno.error}
      />
    );
  }

  if (!aluno.data) {
    return (
      <ErrorCard
        title="Aluno não encontrado"
        description="A API respondeu sem conteúdo para este cadastro."
      />
    );
  }

  const { data } = aluno;
  const endereco = data.endereco;
  const active = data.active !== false;
  const isArchivePending = archiveAluno.isPending || unarchiveAluno.isPending;
  const isPending = isArchivePending || deleteAluno.isPending;

  function handleArchiveToggle() {
    const actionLabel = active ? "arquivar" : "desarquivar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este aluno?`)) {
      return;
    }

    const action = active ? archiveAluno : unarchiveAluno;
    action.mutate({ alunoId });
  }

  function handleDelete() {
    if (!window.confirm("Deseja mesmo excluir este aluno? Esta ação não pode ser desfeita.")) {
      return;
    }

    deleteAluno.mutate(
      { alunoId },
      {
        onSuccess: () => {
          router.push("/alunos");
        },
      },
    );
  }

  return (
    <div className="space-y-6">
      <Modal
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
        title="Editar aluno"
        description="Atualize os dados cadastrais do aluno sem sair da tela de detalhe."
        size="lg"
      >
        <AlunoForm initialData={data} onSuccess={() => setIsEditOpen(false)} onCancel={() => setIsEditOpen(false)} />
      </Modal>

      <section className="grid gap-6 xl:grid-cols-2">
        <div className="app-shell-card p-6">
          <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
            <div className="space-y-3">
              <h2 className="text-xl font-bold text-base-content">Dados cadastrais</h2>
              <Badge variant={active ? "success" : "ghost"}>
                {active ? "Ativo" : "Arquivado"}
              </Badge>
            </div>

            <div className="flex flex-wrap gap-2">
              <Button type="button" size="sm" variant="primary" className="btn-square" aria-label="Editar aluno" title="Editar aluno" onClick={() => setIsEditOpen(true)}>
                <PencilLine size={18} />
              </Button>

              <Button type="button" size="sm" variant={active ? "warning" : "outline"} disabled={isPending} onClick={handleArchiveToggle}>
                {isArchivePending ? "Processando..." : active ? "Arquivar" : "Desarquivar"}
              </Button>

              <Button type="button" size="sm" variant="error" className="btn-square" aria-label="Excluir aluno" title="Excluir aluno" disabled={isPending} onClick={handleDelete}>
                <Trash2 size={18} />
              </Button>

              <Link className="btn btn-outline btn-sm" href="/alunos">
                Voltar
              </Link>
            </div>
          </div>

          <div className="mt-6 grid gap-4 rounded-2xl border border-base-300 bg-base-200/25 p-4">
            <div className="space-y-1">
              <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Aluno</p>
              <p className="text-2xl font-bold text-base-content">{data.nome}</p>
            </div>
          </div>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <DetailField label="CPF" value={formatCpf(data.cpf)} />
            <DetailField label="E-mail" value={data.email} />
            <DetailField label="Telefone" value={formatPhone(data.telefone)} />
            <DetailField label="Data de nascimento" value={formatDate(data.dataNascimento)} />
            <DetailField label="Idade" value={data.idade} />
            <DetailField label="Escola" value={data.escola} />
          </div>

          <div className="mt-6 rounded-2xl border border-base-300 bg-base-200/25 p-4">
            <div className="mb-4 flex items-start justify-between gap-3">
              <div>
                <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Responsável vinculado</p>
                <p className="mt-1 text-sm text-base-content/65">Dados do responsável associado a este aluno.</p>
              </div>

              {responsavel.data ? (
                <Link className="btn btn-outline btn-sm" href={`/responsaveis/${responsavel.data.id}`}>
                  Ver responsável
                </Link>
              ) : null}
            </div>

            {responsavel.isLoading ? (
              <p className="text-sm text-base-content/60">Carregando responsável...</p>
            ) : responsavel.error ? (
              <p className="text-sm text-error">Não foi possível carregar os dados do responsável.</p>
            ) : responsavel.data ? (
              <div className="grid gap-5 md:grid-cols-2">
                <DetailField label="Nome" value={responsavel.data.nome} />
                <DetailField label="CPF" value={formatCpf(responsavel.data.cpf)} />
                <DetailField label="Telefone" value={formatPhone(responsavel.data.telefone)} />
                <DetailField label="E-mail" value={responsavel.data.email} />
              </div>
            ) : (
              <p className="text-sm text-base-content/60">Nenhum responsável vinculado encontrado.</p>
            )}
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

      <AlunoAtendimentos alunoId={alunoId} />
    </div>
  );
}
