import { Button } from "@/components/ui/button";
import { useAuth } from "@/features/auth/lib/use-auth";
import { LockKeyhole, LogOut, Mail, ShieldCheck, Sparkles } from "lucide-react";
import { useState } from "react";
import type { FormEvent } from "react";

export function LoginPage() {
  const { isAuthenticated, login, logout, user } = useAuth();
  const [email, setEmail] = useState("admin@aprimorar.local");
  const [password, setPassword] = useState("admin123");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);

    try {
      await login({ email, password });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="min-h-130 overflow-hidden rounded-3xl border border-base-300 bg-base-100 shadow-2xl">
        {/*<section className="relative flex min-h-80 flex-col justify-between overflow-hidden bg-linear-to-br from-success/20 via-primary/10 to-base-200 p-8 text-base-content lg:p-10">
          <div className="absolute -left-24 top-12 h-64 w-64 rounded-full bg-success/20 blur-3xl" />
          <div className="absolute -bottom-24 right-0 h-72 w-72 rounded-full bg-primary/20 blur-3xl" />

          <div className="relative z-10">
            <div className="mb-8 inline-flex items-center gap-2 rounded-full border border-success/25 bg-base-100/70 px-3 py-2 text-xs font-bold uppercase tracking-[0.18em] text-success shadow-sm">
              <ShieldCheck className="h-4 w-4" />
              Acesso restrito
            </div>

            <h1 className="max-w-sm text-4xl font-extrabold tracking-tight text-base-content sm:text-5xl">
              Entre para gerenciar a operação da Aprimorar.
            </h1>
            <p className="mt-4 max-w-md text-sm leading-6 text-base-content/65">
              Este fluxo ainda está em modo playground. O backend será conectado depois, mantendo a experiência aprovada aqui.
            </p>
          </div>

          <div className="relative z-10 rounded-2xl border border-base-300 bg-base-100/70 p-4 shadow-sm backdrop-blur">
            <div className="mb-2 flex items-center gap-2 text-sm font-semibold text-base-content">
              <Sparkles className="h-4 w-4 text-success" />
              Dica para teste
            </div>
            <p className="text-xs leading-5 text-base-content/60">
              Emails iniciados com <strong>admin</strong> entram como ADMIN. Qualquer outro email entra como EMPLOYEE.
            </p>
          </div>
        </section>*/}

        <section className="flex items-center justify-center p-6 sm:p-10">
          <div className="w-full max-w-md p-6 border border-base-300 rounded-xl">
            <div className="mb-8">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Login</p>
              <h2 className="mt-2 text-3xl font-bold text-base-content">Acessar sistema</h2>
              <p className="mt-2 text-sm text-base-content/60">
                Informe suas credenciais para iniciar uma sessão local.
              </p>
            </div>

            <form className="flex flex-col gap-3" onSubmit={handleSubmit}>
              <label className="form-control w-full">
                <span className="label-text mb-1 font-semibold text-base-content/75">Email</span>
                <div className="input input-bordered w-full flex items-center gap-2 bg-base-100">
                  <Mail className="h-4 w-4 text-base-content/40" />
                  <input
                    aria-label="Email"
                    className="grow"
                    onChange={(event) => setEmail(event.target.value)}
                    placeholder="voce@aprimorar.com"
                    type="email"
                    value={email}
                  />
                </div>
              </label>

              <label className="form-control w-full">
                <span className="label-text mb-1 font-semibold text-base-content/75">Senha</span>
                <div className="input input-bordered flex w-full items-center gap-2 bg-base-100">
                  <LockKeyhole className="h-4 w-4 text-base-content/40" />
                  <input
                    aria-label="Senha"
                    className="grow"
                    onChange={(event) => setPassword(event.target.value)}
                    placeholder="Digite sua senha"
                    type="password"
                    value={password}
                  />
                </div>
              </label>

              <Button
                className="w-full mt-3"
                disabled={isSubmitting || !email || !password}
                type="submit"
                variant="success"
              >
                {isSubmitting ? "Entrando..." : "Entrar"}
              </Button>
            </form>

            <div className="mt-6 rounded-2xl border border-base-300 bg-base-200/60 p-4">
              <p className="text-xs font-bold uppercase tracking-[0.16em] text-base-content/45">Sessão local</p>
              {isAuthenticated && user ? (
                <div className="mt-3 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                  <div>
                    <p className="font-semibold text-base-content">{user.name}</p>
                    <p className="text-sm text-base-content/60">{user.email} · {user.role}</p>
                  </div>
                  <Button onClick={logout} size="sm" type="button" variant="ghost">
                    <LogOut className="mr-2 h-4 w-4" />
                    Sair
                  </Button>
                </div>
              ) : (
                <p className="mt-2 text-sm text-base-content/60">Nenhuma sessão ativa neste navegador.</p>
              )}
            </div>
          </div>
        </section>
    </main>
  );
}
