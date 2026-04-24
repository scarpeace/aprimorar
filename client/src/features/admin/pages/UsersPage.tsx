import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { SectionCard } from "@/components/ui/section-card";
import { Badge } from "@/components/ui/badge";
import { ShieldCheck, Plus, Trash2, User } from "lucide-react";
import { useState } from "react";
import { useUsers, useUserMutations } from "../hooks/user-management-hooks";
import { UserForm } from "../components/UserForm";
import { ConfirmModal } from "@/components/ui/confirm-modal";

export function UsersPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<string | null>(null);

  const usersQuery = useUsers();
  const { deleteUser } = useUserMutations();

  const filteredUsers = usersQuery.data?.filter(u =>
    u.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    u.employeeName?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const headerProps = {
    title: "Gerenciamento de Usuários",
    description: "Controle quem tem acesso ao sistema e seus respectivos papéis.",
    Icon: ShieldCheck,
    backLink: "/",
  };

  const handleDelete = () => {
    if (userToDelete) {
      deleteUser.mutate(userToDelete, {
        onSuccess: () => setUserToDelete(null)
      });
    }
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-6">
        <div className="flex flex-col sm:flex-row gap-4 justify-between items-center">
          <ListSearchInput
            className="w-full sm:max-w-md"
            placeholder="Buscar por usuário ou nome..."
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <Button variant="success" onClick={() => setIsFormOpen(true)}>
            <Plus className="h-4 w-4 mr-2" />
            Novo Usuário
          </Button>
        </div>

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
                {usersQuery.isPending && (
                  <tr>
                    <td colSpan={5} className="text-center py-8">Carregando usuários...</td>
                  </tr>
                )}
                {filteredUsers?.map((user) => (
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
                      <Badge variant={user.active ? "success" : "danger"}>
                        {user.active ? "Ativo" : "Inativo"}
                      </Badge>
                    </td>
                    <td className="text-right">
                      <Button
                        variant="ghost"
                        size="sm"
                        className="text-error"
                        onClick={() => setUserToDelete(user.id)}
                        disabled={deleteUser.isPending}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </td>
                  </tr>
                ))}
                {!usersQuery.isPending && filteredUsers?.length === 0 && (
                  <tr>
                    <td colSpan={5} className="text-center py-8 text-base-content/50">
                      Nenhum usuário encontrado.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </SectionCard>
      </div>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg mb-6 flex items-center gap-2">
              <Plus className="h-5 w-5" />
              Novo Usuário do Sistema
            </h3>
            <UserForm
              onSuccess={() => setIsFormOpen(false)}
              onCancel={() => setIsFormOpen(false)}
            />
          </div>
        </div>
      )}

      <ConfirmModal
        isOpen={!!userToDelete}
        onClose={() => setUserToDelete(null)}
        onConfirm={handleDelete}
        title="Excluir Usuário"
        message="Tem certeza que deseja remover o acesso deste usuário? Ele não poderá mais logar no sistema."
        confirmText="Sim, remover"
        variant="danger"
        isPending={deleteUser.isPending}
      />
    </PageLayout>
  );
}
