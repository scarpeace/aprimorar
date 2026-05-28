import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ShieldCheck } from "lucide-react";
import { useState } from "react";

import { UsuarioForm } from "../components/UsuarioForm";
import { UsuariosTable } from "../components/UsuariosTable";

const headerProps = {
  description: "Gerencie usuarios da plataforma com criacao e exclusao de acesso.",
  title: "Admin",
  Icon: ShieldCheck,
};

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

      {isFormOpen ? (
        <div className="modal modal-open">
          <div className="modal-box max-w-xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Cadastrar novo usuario</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Defina e-mail, senha e perfil para liberar acesso ao sistema.
            </p>
            <UsuarioForm onSuccess={() => setIsFormOpen(false)} onCancel={() => setIsFormOpen(false)} />
          </div>
        </div>
      ) : null}
    </PageLayout>
  );
}
