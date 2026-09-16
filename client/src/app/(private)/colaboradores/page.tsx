import { ColaboradoresTable } from "@/features/colaboradores/components/ColaboradoresTable";
import { NovoColaboradorButton } from "@/features/colaboradores/components/NovoColaboradorButton";

export default function ColaboradoresPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <div className="flex flex-col gap-5 md:flex-row md:items-start md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Colaboradores</p>
            <h1 className="mt-2 text-3xl font-bold text-base-content">Listagem de colaboradores</h1>
            <p className="mt-3 max-w-2xl text-sm text-base-content/65">
              Acompanhe e gerencie os colaboradores registrados no sistema.
            </p>
          </div>

          <NovoColaboradorButton />
        </div>
      </div>

      <ColaboradoresTable />
    </section>
  );
}
