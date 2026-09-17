import type { ReactNode } from "react";
import { Card, CardActions, CardHeader, CardTitle } from "@/components/ui/Card";
import { NovaDespesaButton } from "@/features/despesas/components/NovaDespesaButton";

export default function FinanceiroLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <>
      <Card className="mb-6">
        <CardHeader>
          <div>
            <p className="text-sm font-semibold uppercase text-success">Financeiro</p>
            <CardTitle className="mt-2">Gestão financeira</CardTitle>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Acompanhe despesas, cobranças e repasses da instituição.
            </p>
          </div>

          <CardActions>
            <NovaDespesaButton />
          </CardActions>
        </CardHeader>
      </Card>

      {children}
    </>
  );
}
