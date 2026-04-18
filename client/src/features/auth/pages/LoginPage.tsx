import { zodResolver } from "@hookform/resolvers/zod";
import { LockKeyhole, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { Navigate, useNavigate } from "react-router-dom";

import { Alert } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { SectionCard } from "@/components/ui/section-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { AuthGate } from "../components/AuthGate";
import { loginSchema, type LoginSchema } from "../forms/loginSchema";
import { useAuthSession } from "../hooks/useAuthSession";

function getLoginErrorMessage(error: unknown) {
  const message = getFriendlyErrorMessage(error);

  if (message) {
    return message;
  }

  return "Não foi possível entrar. Confira suas credenciais e tente novamente.";
}

export function LoginPage() {
  const navigate = useNavigate();
  const { currentUserQuery, isAuthenticated, login } = useAuthSession();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<LoginSchema>({
    resolver: zodResolver(loginSchema),
    mode: "onBlur",
    defaultValues: {
      identifier: "",
      password: "",
    },
  });

  const onSubmit = handleSubmit(async (data) => {
    let loggedIn = false;

    try {
      await login.mutateAsync({ data });
      loggedIn = true;
      navigate("/");
    } catch {
      // error state is handled by the mutation itself
    } finally {
      reset(
        loggedIn
          ? { identifier: "", password: "" }
          : { identifier: data.identifier, password: "" },
      );
    }
  });

  const loginForm = (
    <div className="flex min-h-screen items-center justify-center bg-base-200 px-4 py-10">
      <div className="w-full max-w-xl">
        <SectionCard
          title="Entrar"
          description="Acesse o Aprimorar com seu usuário ou email institucional."
        >
          <div className="mb-2 flex items-center gap-3 text-base-content">
            <div className="rounded-full bg-success/10 p-3 text-success">
              <LockKeyhole className="h-6 w-6" />
            </div>
            <p className="text-sm text-base-content/70">
              Sua sessão será mantida com autenticação baseada em cookies seguros.
            </p>
          </div>

          {login.isError ? (
            <Alert variant="error" error={getLoginErrorMessage(login.error)} />
          ) : null}

          <form className="flex flex-col gap-4" onSubmit={onSubmit} autoComplete="off">
            <fieldset className="fieldset">
              <legend className="fieldset-legend">Usuário ou email</legend>
              <input
                type="text"
                className="input w-full"
                placeholder="Digite seu usuário ou email"
                {...register("identifier")}
              />
              {errors.identifier ? (
                <p className="label text-error">
                  <TriangleAlert className="h-3 w-3" />
                  {errors.identifier.message}
                </p>
              ) : null}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Senha</legend>
              <input
                type="password"
                className="input w-full"
                placeholder="Digite sua senha"
                {...register("password")}
              />
              {errors.password ? (
                <p className="label text-error">
                  <TriangleAlert className="h-3 w-3" />
                  {errors.password.message}
                </p>
              ) : null}
            </fieldset>

            <div className="mt-2 flex justify-end">
              <Button type="submit" disabled={login.isPending || currentUserQuery.isPending}>
                {login.isPending ? "Entrando..." : "Entrar"}
              </Button>
            </div>
          </form>
        </SectionCard>
      </div>
    </div>
  );

  return (
    <AuthGate
      isPending={currentUserQuery.isPending}
      isAuthenticated={isAuthenticated}
      fallback={loginForm}
    >
      <Navigate to="/" replace />
    </AuthGate>
  );
}
