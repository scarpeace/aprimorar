"use client";

import { useRouter } from "next/navigation";
import { useGetAlunosByResponsavel } from "@/lib/api/generated/hooks/aluno/useGetAlunosByResponsavel";
import { Badge } from "@/components/ui/Badge";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { PageLoading } from "@/components/ui/PageLoading";
import { formatCpf, formatPhone } from "@/lib/utils/formatter";

export function ResponsavelAlunos({ responsavelId }: Readonly<{ responsavelId: string }>) {
  const router = useRouter();
  const alunos = useGetAlunosByResponsavel(responsavelId);

  if (alunos.isLoading) {
    return <PageLoading message="Carregando alunos vinculados..." />;
  }

  if (alunos.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar os alunos vinculados"
        description="A consulta da API falhou para o responsável informado."
        error={alunos.error}
      />
    );
  }

  if (!alunos.data || alunos.data.length === 0) {
    return (
      <EmptyCard
        title="Nenhum aluno vinculado"
        description="Os alunos associados a este responsável aparecerão aqui."
      />
    );
  }

  return (
    <section className="app-shell-card p-6">
      <div>
        <h2 className="text-xl font-bold text-base-content">Alunos vinculados</h2>
        <p className="mt-2 text-sm text-base-content/65">Veja os alunos associados a este responsável e navegue para os detalhes de cada um.</p>
      </div>

      <div className="mt-6 hidden overflow-x-auto rounded-2xl border border-base-300 bg-base-100 md:block">
        <table className="table table-zebra">
          <thead className="bg-base-200/80">
            <tr>
              <th>Nome</th>
              <th>CPF</th>
              <th>Telefone</th>
              <th>Escola</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {alunos.data.map((aluno) => {
              const active = aluno.active !== false;

              return (
                <tr
                  key={aluno.id}
                  className="cursor-pointer transition-colors hover:bg-base-200/70"
                  onClick={() => router.push(`/alunos/${aluno.id}`)}
                >
                  <td className="font-semibold text-base-content">{aluno.nome}</td>
                  <td>{formatCpf(aluno.cpf)}</td>
                  <td>{formatPhone(aluno.telefone)}</td>
                  <td>{aluno.escola || "—"}</td>
                  <td>
                    <Badge variant={active ? "success" : "ghost"}>{active ? "Ativo" : "Arquivado"}</Badge>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <div className="mt-6 grid gap-4 md:hidden">
        {alunos.data.map((aluno) => {
          const active = aluno.active !== false;

          return (
            <article
              key={aluno.id}
              className="cursor-pointer rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm transition-colors hover:bg-base-200/40"
              onClick={() => router.push(`/alunos/${aluno.id}`)}
            >
              <div className="flex items-start justify-between gap-3">
                <div>
                  <h3 className="font-semibold text-base-content">{aluno.nome}</h3>
                  <p className="text-sm text-base-content/65">{formatPhone(aluno.telefone)}</p>
                </div>

                <Badge variant={active ? "success" : "ghost"}>{active ? "Ativo" : "Arquivado"}</Badge>
              </div>

              <div className="mt-4 grid gap-2 rounded-2xl border border-base-200 bg-base-200/35 p-3 text-sm">
                <p>
                  <span className="font-medium text-base-content">CPF:</span> {formatCpf(aluno.cpf)}
                </p>
                <p>
                  <span className="font-medium text-base-content">Escola:</span> {aluno.escola || "—"}
                </p>
              </div>

            </article>
          );
        })}
      </div>
    </section>
  );
}
