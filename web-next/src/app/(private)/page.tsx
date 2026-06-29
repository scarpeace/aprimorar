import { requireSession } from "@/lib/auth/session";

export default async function DashboardPage() {
  const { user } = await requireSession();

  return (
    <section className="app-shell-card p-6 md:p-8">
      <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Etapa 1</p>
      <h1 className="mt-2 text-3xl font-bold text-base-content">Sessão autenticada</h1>
      <p className="mt-3 max-w-2xl text-sm text-base-content/65">
        O login, o cookie de sessão e a proteção de rotas já estão funcionando no shell do Next.
      </p>

      <div className="mt-6 grid gap-3 md:grid-cols-2">
        <div className="rounded-xl border border-base-300 bg-base-100 p-4">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Usuário</p>
          <p className="mt-2 text-lg font-semibold text-base-content">{user.username}</p>
        </div>

        <div className="rounded-xl border border-base-300 bg-base-100 p-4">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Role</p>
          <p className="mt-2 text-lg font-semibold text-base-content">{user.role}</p>
        </div>
      </div>
    </section>
  );
}
