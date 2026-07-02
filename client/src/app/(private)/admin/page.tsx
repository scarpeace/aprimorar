import { AdminUsersOverview } from "@/components/admin/AdminUsersOverview";

export default function AdminPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Admin</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Gestão de usuários</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Cadastre novos acessos e remova usuários que não devem mais entrar no sistema.
        </p>
      </div>

      <AdminUsersOverview />
    </section>
  );
}
