"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useUserMutations } from "@/hooks/use-user-mutations";
import { userFormSchema, type UserFormData } from "@/lib/validators/user-form-schema";

type UserFormProps = {
  onSuccess: () => void;
  onCancel: () => void;
};

const roleOptions = [{ value: "COLABORADOR", label: "Colaborador" }];

export function UserForm({ onSuccess, onCancel }: Readonly<UserFormProps>) {
  const { createUser } = useUserMutations();

  const methods = useForm<UserFormData>({
    resolver: zodResolver(userFormSchema),
    mode: "onBlur",
    defaultValues: {
      username: "",
      password: "",
      role: "COLABORADOR",
    },
  });

  const onSubmit = methods.handleSubmit((data) => {
    createUser.mutate(
      { data },
      {
        onSuccess,
      },
    );
  });

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col gap-6" autoComplete="off" onSubmit={onSubmit}>
        <section className="space-y-4">
          <div>
            <h4 className="text-base font-bold text-base-content">Novo usuário</h4>
            <p className="text-sm text-base-content/60">O backend atual permite criar apenas usuário colaborador.</p>
          </div>

          <div className="grid gap-4">
            <TextInput name="username" type="email" label="E-mail" disabled={createUser.isPending} />
            <TextInput name="password" type="password" label="Senha" disabled={createUser.isPending} />
            <SelectInput name="role" label="Perfil" options={roleOptions} disabled={createUser.isPending} />
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={createUser.isPending} onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={createUser.isPending}>
            {createUser.isPending ? "Salvando..." : "Cadastrar usuário"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
