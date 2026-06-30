"use client";

import Link from "next/link";
import { useState } from "react";
import { AlunoForm } from "@/components/alunos/AlunoForm";
import { useGetAlunoById } from "@/lib/api/generated/hooks/aluno/useGetAlunoById";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { useAlunoMutations } from "@/hooks/use-aluno-mutations";
import { formatCpf, formatPhone, formatZip } from "@/lib/utils/formatter";

function formatDate(value: string) {
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

export function AlunoDetails({ alunoId }: Readonly<{ alunoId: string }>) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const aluno = useGetAlunoById(alunoId);
  const { archiveAluno, unarchiveAluno } = useAlunoMutations();

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
  const isPending = archiveAluno.isPending || unarchiveAluno.isPending;

  function handleArchiveToggle() {
    const actionLabel = active ? "arquivar" : "desarquivar";

    if (!window.confirm(`Deseja mesmo ${actionLabel} este aluno?`)) {
      return;
    }

    const action = active ? archiveAluno : unarchiveAluno;
    action.mutate({ alunoId });
  }

  return (
    <div className="space-y-6">
      <section className="app-shell-card p-6 md:p-8">
        <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Aluno</p>
            <h1 className="mt-2 text-3xl font-bold text-base-content">{data.nome}</h1>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Dados cadastrais e endereço do aluno selecionado.
            </p>
          </div>

          <div className="flex flex-col items-start gap-3">
            <Badge variant={active ? "success" : "ghost"}>
              {active ? "Ativo" : "Arquivado"}
            </Badge>

            <div className="flex flex-wrap gap-2">
              <Button type="button" size="sm" variant="primary" onClick={() => setIsEditOpen(true)}>
                Editar
              </Button>

              <Button type="button" size="sm" variant={active ? "warning" : "outline"} disabled={isPending} onClick={handleArchiveToggle}>
                {isPending ? "Processando..." : active ? "Arquivar" : "Desarquivar"}
              </Button>

              <Link className="btn btn-outline btn-sm" href="/alunos">
                Voltar para alunos
              </Link>
            </div>
          </div>
        </div>
      </section>

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
          <h2 className="text-xl font-bold text-base-content">Dados cadastrais</h2>

          <div className="mt-6 grid gap-5 md:grid-cols-2">
            <Field label="Nome" value={data.nome} />
            <Field label="CPF" value={formatCpf(data.cpf)} />
            <Field label="E-mail" value={data.email} />
            <Field label="Telefone" value={formatPhone(data.telefone)} />
            <Field label="Data de nascimento" value={formatDate(data.dataNascimento)} />
            <Field label="Idade" value={data.idade} />
            <Field label="Escola" value={data.escola} />
            <Field label="Responsável vinculado" value={data.responsavelId} />
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
