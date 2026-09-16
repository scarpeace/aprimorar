import type { ReactNode } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { NovoColaboradorButton } from "@/features/colaboradores/components/NovoColaboradorButton";

export default function ColaboradoresLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <>
      <Card className="mb-6">
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Colaboradores</p>
            <CardTitle className="mt-2">Gestão de colaboradores</CardTitle>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Acompanhe e gerencie os colaboradores registrados no sistema.
            </p>
          </div>

          <CardActions>
            <NovoColaboradorButton />
          </CardActions>
        </CardHeader>
      </Card>

      {children}
    </>
  );
}
