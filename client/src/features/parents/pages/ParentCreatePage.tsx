import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { SectionCard } from "@/components/ui/section-card";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import {
  parentFormSchema,
  type ParentFormSchema,
} from "../forms/parentFormSchema";
import { useParentMutations } from "../hooks/parent-mutations";

export function ParentCreatePage() {
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm<ParentFormSchema>({
    resolver: zodResolver(parentFormSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);

  const { createParent } = useParentMutations();

  const onSubmit = handleSubmit((data: ParentFormSchema) => {
    createParent.mutate({ data });
  });

  const headerProps = {
    title: "Criar responsável",
    description: "Preencha os dados do responsável.",
    Icon: GraduationCap,
    backLink: "/parents",
  };

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title={"Cadastre um novo responsável"}
        description={"Informe os dados do responsável."}
      >
        {createParent.error && (
          <Alert error={createParent.error} variant="error" />
        )}

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-5">

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Nome</legend>
              <input type="text" className="input" {...register("name")}placeholder="Nome Completo"/>
              {errors?.name && ( <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.name.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Email</legend>
              <input type="text" className="input" {...register("email")} placeholder="email@email.com"/>
              {errors?.email && ( <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.email.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Contato</legend>
              <input type="text" className="input" placeholder="Ex: (61) 99633-2332" {...registerWithMask("contact", ["(##) #####-####", "(##) ####-####"])} />
              {errors?.contact && ( <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.contact.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">CPF</legend>
              <input type="text" className="input" placeholder="Ex: 123.456.789-00" {...registerWithMask("cpf", ["###.###.###-##"])} />
              {errors?.cpf && ( <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.cpf.message}</p>)}
            </fieldset>
          </div>

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to="/parents" variant="outline">Cancelar</ButtonLink>
            <Button type="submit" disabled={createParent.isPending} variant="primary">
              {createParent.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
