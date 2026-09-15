import { LoginForm } from "@/auth/components/LoginForm";

export default function LoginPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-base-200 px-4 py-8">
      <section className="w-full max-w-md rounded-2xl border border-base-300 bg-base-100 p-6 shadow-xl sm:p-8">
        <div className="mb-8">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-success">Aprimorar</p>
          <h1 className="mt-2 text-2xl font-bold text-base-content">Entrar na sua conta</h1>
          <p className="mt-2 text-sm text-base-content/65">Informe suas credenciais para acessar o sistema.</p>
        </div>

        <LoginForm />
      </section>
    </main>
  );
}
