import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { Button } from "@/components/button.tsx";
import { Modal } from "@/components/modal.tsx";
import { ShieldCheck } from "lucide-react";
import { useState } from "react";

import { UsuarioForm } from "../../components/Admin/UsuarioForm.tsx";
import { UsuariosTable } from "../../components/Admin/UsuariosTable.tsx";

const headerProps = {
  description: "Gerencie usuarios da plataforma com criacao e exclusao de acesso.",
  title: "Settings",
  Icon: ShieldCheck,
  iconBg: "primary",
} as const;

export function AdminPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h3 className="text-lg font-bold text-base-content">Usuarios cadastrados</h3>
            <p className="text-sm text-base-content/60">
              Crie contas novas e exclua acessos que nao devem mais entrar no sistema.
            </p>
          </div>
          <Button className="sm:ml-auto" onClick={() => setIsFormOpen(true)} variant="success">
            Novo usuario
          </Button>
        </div>

        <UsuariosTable />
      </section>

      <Modal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        title="Cadastrar novo usuario"
        description="Defina e-mail, senha e perfil para liberar acesso ao sistema."
        size="sm"
      >
        <UsuarioForm onSuccess={() => setIsFormOpen(false)} onCancel={() => setIsFormOpen(false)} />
      </Modal>
    </PageLayout>
  );
}
