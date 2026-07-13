"use client";

import { useState } from "react";
import { Plus } from "lucide-react";
import { useListUsers } from "@/lib/api/generated/hooks/user/useListUsers";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { Modal } from "@/components/ui/Modal";
import { PageLoading } from "@/components/ui/PageLoading";
import { Button } from "@/components/ui/Button";
import { UserForm } from "@/components/admin/UserForm";
import { useUserMutations } from "@/hooks/use-user-mutations";
import { formatDateShortYear } from "@/lib/utils/date-utils";

const roleLabels = {
  ADMIN: "Administrador",
  ALUNO: "Aluno",
  COLABORADOR: "Colaborador",
  RESPONSAVEL: "Responsável",
  SISTEMA: "Sistema",
} as const;

export function AdminUsersOverview() {
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const users = useListUsers();
  const { deleteUser } = useUserMutations();

  function handleDelete(id: string, username: string) {
    if (!window.confirm(`Deseja mesmo excluir o usuário ${username}?`)) {
      return;
    }

    deleteUser.mutate({ id });
  }

  return (
    <section className="app-shell-card p-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-bold text-base-content">Usuários</h2>

          <Button type="button" size="sm" className="btn-square" aria-label="Novo usuário" title="Novo usuário" onClick={() => setIsCreateOpen(true)}>
            <Plus size={18} />
          </Button>
        </div>

      </div>

      {users.isLoading ? (
        <PageLoading message="Carregando usuários..." />
      ) : users.error ? (
        <div className="mt-6">
          <ErrorCard
            title="Não foi possível carregar os usuários"
            description="A consulta da API falhou para a área administrativa."
            error={users.error}
          />
        </div>
      ) : !users.data || users.data.length === 0 ? (
        <EmptyCard title="Nenhum usuário encontrado" description="Cadastre o primeiro usuário para liberar acesso ao sistema." />
      ) : (
        <div className="mt-6 overflow-x-auto rounded-2xl border border-base-300 bg-base-100">
          <table className="table table-zebra">
            <thead className="bg-base-200/80">
              <tr>
                <th>E-mail</th>
                <th>Perfil</th>
                <th>Status</th>
                <th>Criado em</th>
                <th className="text-right">Ações</th>
              </tr>
            </thead>

            <tbody>
              {users.data.map((user) => {
                const canDelete = user.role !== "ADMIN" && user.role !== "SISTEMA";

                return (
                  <tr key={user.id}>
                    <td className="font-semibold text-base-content">{user.username}</td>
                    <td>{roleLabels[user.role] ?? user.role}</td>
                    <td>
                      <span className={`badge badge-sm ${user.active ? "badge-success" : "badge-ghost"}`}>
                        {user.active ? "Ativo" : "Inativo"}
                      </span>
                    </td>
                    <td>{formatDateShortYear(user.createdAt)}</td>
                    <td className="text-right">
                      {canDelete ? (
                        <Button
                          type="button"
                          size="sm"
                          variant="outline"
                          disabled={deleteUser.isPending}
                          onClick={() => handleDelete(user.id, user.username)}
                        >
                          {deleteUser.isPending ? "Processando..." : "Excluir"}
                        </Button>
                      ) : (
                        <span className="text-sm text-base-content/45">Protegido</span>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <Modal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
        title="Cadastrar usuário"
        description="Crie um novo acesso para colaborador."
        size="md"
      >
        <UserForm onSuccess={() => setIsCreateOpen(false)} onCancel={() => setIsCreateOpen(false)} />
      </Modal>
    </section>
  );
}
