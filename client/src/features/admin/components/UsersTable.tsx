import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { SectionCard } from "@/components/ui/section-card";
import { User, Trash2 } from "lucide-react";
import type { UserResponse } from "../hooks/user-management-hooks";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";

interface UsersTableProps {
  query: any;
  filteredUsers?: UserResponse[];
  onDelete: (id: string) => void;
  isDeleting: boolean;
}

export function UsersTable({ query, filteredUsers, onDelete, isDeleting }: UsersTableProps) {
  if (query.isPending) {
    return (
      <div className="flex justify-center py-12">
        <LoadingSpinner text="Carregando usuários..." />
      </div>
    );
  }

  if (query.isError) {
    return (
      <ErrorCard
        title="Não foi possível carregar a listagem de usuários"
        error={query.error}
      />
    );
  }

  if (!filteredUsers || filteredUsers.length === 0) {
    return (
      <EmptyCard
        title="Nenhum usuário encontrado"
        description="Tente ajustar sua busca ou cadastre um novo usuário de sistema."
      />
    );
  }

  return (
    <SectionCard title={""} description={""}>
      <div className="overflow-x-auto">
        <table className="table w-full">
          <thead>
            <tr>
              <th>Usuário</th>
              <th>Colaborador</th>
              <th>Papel</th>
              <th>Status</th>
              <th className="text-right">Ações</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user) => (
              <tr key={user.id}>
                <td>
                  <div className="flex items-center gap-2">
                    <User className="h-4 w-4 text-base-content/50" />
                    <span className="font-medium">{user.username}</span>
                  </div>
                </td>
                <td>{user.employeeName}</td>
                <td>
                  <Badge variant={user.role === "ADMIN" ? "warning" : "neutral"}>
                    {user.role}
                  </Badge>
                </td>
                <td>
                  <Badge variant={user.active ? "success" : "error"}>
                    {user.active ? "Ativo" : "Inativo"}
                  </Badge>
                </td>
                <td className="text-right">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="text-error"
                    onClick={() => onDelete(user.id)}
                    disabled={isDeleting}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </SectionCard>
  );
}
