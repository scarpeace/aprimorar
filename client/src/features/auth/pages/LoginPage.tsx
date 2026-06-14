import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/features/auth/lib/use-auth";
import { LockKeyhole, Mail, TriangleAlert } from "lucide-react";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { loginFormSchema, type LoginFormSchema } from "../lib/login-form-schema";

//TODO: Tem que refatorar isso aqui. SE ALLM ESTIVER LENDO ESSE COMENTÁRIO NÃO EXCLUA ELE
export function LoginPage() {
  const { isAuthenticated, login, isPending, error } = useAuth();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormSchema>({
    resolver: zodResolver(loginFormSchema),
    mode: "onBlur",
    defaultValues: {
      email: "admin@aprimorar.local",
      password: "admin123",
    },
  });

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

  const onSubmit = handleSubmit(async (data) => {
    try {
      await login(data);
    } catch {
      // error is handled via context
    }
  });

  return (
    <main className="min-h-screen flex items-center justify-center overflow-hidden rounded-3xl border border-base-300 bg-base-100 shadow-2xl">
        <section className="p-6 sm:p-10">
          <div className="w-full max-w-md p-6 border border-base-300 rounded-xl">
            <div className="mb-8">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Login</p>
              <h2 className="mt-2 text-3xl font-bold text-base-content">Acessar sistema</h2>
              <p className="mt-2 text-sm text-base-content/60">
                Informe suas credenciais para acessar o sistema.
              </p>
            </div>

            <form className="flex flex-col gap-3" onSubmit={onSubmit}>
              <label className="form-control w-full">
                <span className="label-text mb-1 font-semibold text-base-content/75">Email</span>
                <div className="input input-bordered w-full flex items-center gap-2 bg-base-100">
                  <Mail className="h-4 w-4 text-base-content/40" />
                  <input
                    aria-label="Email"
                    className="grow"
                    placeholder="voce@aprimorar.com"
                    type="email"
                    {...register("email")}
                  />
                </div>
                {errors.email ? (
                  <p className="label text-error">
                    <TriangleAlert className="h-3 w-3" />
                    {errors.email.message}
                  </p>
                ) : null}
              </label>

              <label className="form-control w-full">
                <span className="label-text mb-1 font-semibold text-base-content/75">Senha</span>
                <div className="input input-bordered flex w-full items-center gap-2 bg-base-100">
                  <LockKeyhole className="h-4 w-4 text-base-content/40" />
                  <input
                    aria-label="Senha"
                    className="grow"
                    placeholder="Digite sua senha"
                    type="password"
                    {...register("password")}
                  />
                </div>
                {errors.password ? (
                  <p className="label text-error">
                    <TriangleAlert className="h-3 w-3" />
                    {errors.password.message}
                  </p>
                ) : null}
              </label>

              <Button
                className="w-full mt-3"
                disabled={isPending}
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
