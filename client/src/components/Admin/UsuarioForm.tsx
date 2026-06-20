import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";

import { Button } from "@/components/button.tsx";
import { userRequestDTORoleEnum } from "@/kubb";

import { useUsuarioMutations } from "../../services/use-usuario-mutations.ts";
import { usuarioFormSchema, type UsuarioFormSchema } from "../../utils/zod/usuario-form-schema.ts";

type UsuarioFormProps = {
  onSuccess: () => void;
  onCancel: () => void;
};

const roleLabels = {
  ADMIN: "Administrador",
  EMPLOYEE: "Colaborador",
  PARENT: "Responsavel",
  STUDENT: "Aluno",
} as const;

export function UsuarioForm({ onSuccess, onCancel }: UsuarioFormProps) {
  const { createUsuario } = useUsuarioMutations();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UsuarioFormSchema>({
    resolver: zodResolver(usuarioFormSchema),
    mode: "onBlur",
    defaultValues: {
      username: "",
      password: "",
      role: userRequestDTORoleEnum.EMPLOYEE,
    },
  });

  const onSubmit = handleSubmit((data) => {
    createUsuario.mutate(
      { data },
      {
        onSuccess: () => {
          onSuccess();
        },
      },
    );
  });

  return (
    <form className="flex flex-col gap-4" onSubmit={onSubmit} autoComplete="off">
      <fieldset className="fieldset">
        <legend className="fieldset-legend">E-mail</legend>
        <input
          type="email"
          className="input w-full"
          placeholder="usuario@aprimorar.local"
          {...register("username")}
        />
        {errors.username ? (
          <p className="label text-error">
            <TriangleAlert className="h-3 w-3" />
            {errors.username.message}
          </p>
        ) : null}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Senha</legend>
        <input
          type="password"
          className="input w-full"
          placeholder="Minimo de 6 caracteres"
          {...register("password")}
        />
        {errors.password ? (
          <p className="label text-error">
            <TriangleAlert className="h-3 w-3" />
            {errors.password.message}
          </p>
        ) : null}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Perfil</legend>
        <select className="select select-bordered w-full" {...register("role")}>
          {Object.entries(roleLabels).map(([value, label]) => (
            <option key={value} value={value}>
              {label}
            </option>
          ))}
        </select>
        {errors.role ? (
          <p className="label text-error">
            <TriangleAlert className="h-3 w-3" />
            {errors.role.message}
          </p>
        ) : null}
      </fieldset>

      <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" variant="success" disabled={createUsuario.isPending}>
          {createUsuario.isPending ? "Salvando..." : "Salvar usuario"}
        </Button>
      </div>
    </form>
  );
}
