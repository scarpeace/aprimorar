import type { ReactNode } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { NovoAlunoButton } from "@/features/alunos/components/NovoAlunoButton";

export default function AlunosLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <>
      <Card className="mb-6">
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Alunos</p>
            <CardTitle className="mt-2">Gestão de alunos</CardTitle>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Acompanhe e gerencie os alunos registrados no sistema.
            </p>
          </div>

          <CardActions>
            <NovoAlunoButton />
          </CardActions>
        </CardHeader>
      </Card>

      {children}
    </>
  );
}
