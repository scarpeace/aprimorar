"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { FormProvider, useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useAuthMutations } from "@/features/auth/hooks/use-auth-mutations";
import {
  loginFormSchema,
  type LoginFormData,
} from "@/lib/validators/login-form-schema";

export function LoginForm() {
  const router = useRouter();
  const { login } = useAuthMutations();

  const methods = useForm<LoginFormData>({
    resolver: zodResolver(loginFormSchema),
    mode: "onBlur",
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = methods.handleSubmit((data) => {
    login.mutate(
      { data },
      {
        onSuccess: () => {
          router.replace("/");
          router.refresh();
        },
      },
    );
  });

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col gap-6" autoComplete="on" onSubmit={onSubmit}>
        <div className="grid gap-4">
          <TextInput
            name="email"
            type="email"
            label="E-mail"
            autoComplete="username"
            disabled={login.isPending}
          />

          <TextInput
            name="password"
            type="password"
            label="Senha"
            autoComplete="current-password"
            disabled={login.isPending}
          />
        </div>

        <Button type="submit" disabled={login.isPending}>
          {login.isPending ? "Entrando..." : "Entrar"}
        </Button>
      </form>
    </FormProvider>
  );
}
