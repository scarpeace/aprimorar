import { useGetEmployeeOptions } from "@/kubb";
import { Button } from "@/components/ui/button";
import { useUserMutations } from "../hooks/user-management-hooks";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { userFormSchema, type UserFormSchema } from "../forms/userFormSchema";
import { TriangleAlert } from "lucide-react";

interface UserFormProps {
  onSuccess: () => void;
  onCancel: () => void;
}

export function UserForm({ onSuccess, onCancel }: UserFormProps) {
  const { createUser } = useUserMutations();
  const employeeOptions = useGetEmployeeOptions();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UserFormSchema>({
    resolver: zodResolver(userFormSchema),
    defaultValues: {
      employeeId: "",
      username: "",
      password: "",
      role: "EMPLOYEE",
    },
    mode: "onBlur",
  });

  const onSubmit = handleSubmit((data: UserFormSchema) => {
    createUser.mutate(data, {
      onSuccess: () => onSuccess(),
    });
  });

  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-3" autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-5">
        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Colaborador</legend>
          <select
            className={`select w-full ${errors.employeeId ? "select-error" : ""}`}
            {...register("employeeId")}
          >
            <option value="">Selecione um colaborador</option>
            {employeeOptions.data?.map((opt) => (
              <option key={opt.id} value={opt.id}>
                {opt.name}
              </option>
            ))}
          </select>
          {errors?.employeeId && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.employeeId.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Nome de Usuário</legend>
          <input
            type="text"
            className={`input w-full ${errors.username ? "input-error" : ""}`}
            placeholder="ex: joao.silva"
            {...register("username")}
          />
          {errors?.username && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.username.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Senha</legend>
          <input
            type="password"
            className={`input w-full ${errors.password ? "input-error" : ""}`}
            placeholder="Senha de acesso"
            {...register("password")}
          />
          {errors?.password && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.password.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Papel (Role)</legend>
          <select
            className={`select w-full ${errors.role ? "select-error" : ""}`}
            {...register("role")}
          >
            <option value="EMPLOYEE">Colaborador (Padrão)</option>
            <option value="ADMIN">Administrador</option>
            <option value="STUDENT">Aluno</option>
            <option value="PARENT">Responsável</option>
          </select>
          {errors?.role && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.role.message}
            </p>
          )}
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" variant="success" disabled={createUser.isPending}>
          {createUser.isPending ? "Salvando..." : "Criar Usuário"}
        </Button>
      </div>
    </form>
  );
}
