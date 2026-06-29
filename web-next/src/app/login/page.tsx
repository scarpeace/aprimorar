import { redirect } from "next/navigation";
import { LoginForm } from "@/components/auth/LoginForm";
import { getSession } from "@/lib/auth/session";

export default async function LoginPage() {
  const session = await getSession();

  if (session) {
    redirect("/");
  }

  return (
    <main className="flex min-h-screen items-center justify-center overflow-hidden rounded-3xl border border-base-300 bg-base-100 p-6 shadow-2xl">
      <section className="w-full max-w-md p-6 sm:p-10">
        <div className="w-full rounded-xl border border-base-300 p-6">
          <div className="mb-8">
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Login</p>
            <h1 className="mt-2 text-3xl font-bold text-base-content">Acessar sistema</h1>
            <p className="mt-2 text-sm text-base-content/60">
              Informe suas credenciais para acessar o sistema.
            </p>
          </div>

          <LoginForm />
        </div>
      </section>
    </main>
  );
}
