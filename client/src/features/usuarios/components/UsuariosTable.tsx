"use client";

import { Card, CardHeader, CardTitle } from "@/components/ui/Card";
import { EmptyCard } from "@/components/ui/EmptyCard";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { ExcluirUsuarioButton } from "@/features/usuarios/components/ExcluirUsuarioButton";
import { UsuarioStatusBadge } from "@/features/usuarios/components/UsuarioStatusBadge";
import { UsuarioStatusButton } from "@/features/usuarios/components/UsuarioStatusButton";
import { useGetUsers } from "@/lib/api/generated/hooks/usuário/useGetUsers";

const roleLabels = {
  ADMIN: "Administrador",
  SECRETARIA: "Secretaria",
} as const;

export function UsuariosTable() {
  const users = useGetUsers();

  return (
    <Card>
      <CardHeader>
        <div>
          <CardTitle>Usuários cadastrados</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">
            Consulte os perfis e controle o acesso dos usuários ao sistema.
          </p>
        </div>
      </CardHeader>

      {users.isLoading ? (
        <LoadingSkeleton className="h-64 w-full" />
      ) : users.error ? (
        <ErrorCard
          title="Não foi possível carregar os usuários"
          description="A consulta da API falhou para a área administrativa."
          error={users.error}
        />
      ) : !users.data || users.data.length === 0 ? (
        <EmptyCard title="Nenhum usuário encontrado" description="Cadastre o primeiro usuário para liberar acesso ao sistema." />
      ) : (
        <div className="overflow-x-auto">
          <table className="table table-zebra">
            <thead>
              <tr>
                <th>E-mail</th>
                <th>Perfil</th>
                <th>Status</th>
                <th className="text-right">Ações</th>
              </tr>
            </thead>

            <tbody>
              {users.data.map((user) => (
                <tr key={user.id}>
                  <td className="font-semibold text-base-content">{user.email}</td>
                  <td>{roleLabels[user.role]}</td>
                  <td>
                    <UsuarioStatusBadge enabled={user.enabled} />
                  </td>
                  <td className="text-right">
                    {user.role === "ADMIN" ? (
                      <span className="text-sm text-base-content/45">Protegido</span>
                    ) : (
                      <div className="flex justify-end gap-2">
                        <UsuarioStatusButton user={user} />
                        <ExcluirUsuarioButton user={user} />
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </Card>
  );
}
