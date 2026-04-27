import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { Plus, ShieldCheck } from "lucide-react";
import { useState } from "react";
import { UserForm } from "../components/UserForm";
import { UsersTable } from "../components/UsersTable";
import { useUserMutations, useUsers } from "../hooks/user-management-hooks";



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

        <UsersTable
          query={usersQuery}
          filteredUsers={filteredUsers}
          onDelete={setUserToDelete}
          isDeleting={deleteUser.isPending}
        />
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
