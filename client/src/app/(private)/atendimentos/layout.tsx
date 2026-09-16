import type { ReactNode } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { NovoAtendimentoButton } from "@/features/atendimentos/components/NovoAtendimentoButton";

export default function AtendimentosLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <>
      <Card className="mb-6">
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Atendimentos</p>
            <CardTitle className="mt-2">Gestão de atendimentos</CardTitle>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Acompanhe e gerencie os atendimentos registrados no sistema.
            </p>
          </div>

          <CardActions>
            <NovoAtendimentoButton />
          </CardActions>
        </CardHeader>
      </Card>

      {children}
    </>
  );
}
