import { Button } from "@/components/ui/button";
import { useAuth } from "@/features/auth/lib/use-auth";
import { LockKeyhole, Mail } from "lucide-react";
import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";

export function LoginPage() {
  const { isAuthenticated, login, isPending, error } = useAuth();
  const [email, setEmail] = useState("admin@aprimorar.local");
  const [password, setPassword] = useState("admin123");
  const navigate = useNavigate();

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      await login({ email, password });
    } catch {
      // error is handled via context
    }
  };

  return (
    <main className="min-h-130 overflow-hidden rounded-3xl border border-base-300 bg-base-100 shadow-2xl">
        <section className="flex items-center justify-center p-6 sm:p-10">
          <div className="w-full max-w-md p-6 border border-base-300 rounded-xl">
            <div className="mb-8">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Login</p>
              <h2 className="mt-2 text-3xl font-bold text-base-content">Acessar sistema</h2>
              <p className="mt-2 text-sm text-base-content/60">
                Informe suas credenciais para acessar o sistema.
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
                disabled={isPending || !email || !password}
                type="submit"
                variant="success"
              >
                {isPending ? "Entrando..." : "Entrar"}
              </Button>
            </form>
          </div>
        </section>
    </main>
  );
}
