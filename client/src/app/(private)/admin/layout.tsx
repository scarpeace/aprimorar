import type { ReactNode } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { NovoUsuarioButton } from "@/features/usuarios/components/NovoUsuarioButton";

export default function AdminLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <>
      <Card className="mb-6">
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Admin</p>
            <CardTitle className="mt-2">Gestão de usuários</CardTitle>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Cadastre novos acessos e gerencie os usuários do sistema.
            </p>
          </div>

          <CardActions>
            <NovoUsuarioButton />
          </CardActions>
        </CardHeader>
      </Card>

      {children}
    </>
  );
}
