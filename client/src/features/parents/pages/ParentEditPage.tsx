import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { SectionCard } from "@/components/ui/section-card";
import { useGetParentById } from "@/kubb";
import { zodResolver } from "@hookform/resolvers/zod";
import { Handshake, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import {
  parentFormSchema,
  type ParentFormSchema,
} from "../forms/parentFormSchema";
import { useParentMutations } from "../hooks/parent-mutations";

export function ParentEditPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";

  const { updateParent } = useParentMutations();
  const parentQuery = useGetParentById(parentId);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ParentFormSchema>({
    resolver: zodResolver(parentFormSchema),
    values: {
      name: parentQuery.data?.name ?? "",
      email: parentQuery.data?.email ?? "",
      contact: parentQuery.data?.contact ?? "",
      cpf: parentQuery.data?.cpf ?? "",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: ParentFormSchema) => {
    updateParent.mutate({ parentId, data });
  });

  const headerProps = {
    title: "Editar responsável",
    description: "Atualize os dados do responsável.",
    backLink: `/parents/${parentId}`,
    Icon: Handshake,
  };

  if (parentQuery.error) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do responsável"
          error={parentQuery.error}
        />
      </PageLayout>
    );
  }

  if (parentQuery.isPending || !parentQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do responsável" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title={"Editar responsável"}
        description={"Atualize os dados do responsável."}
      >
        {updateParent.isError && (
          <Alert error={updateParent.error} variant="error" />
        )}

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-4 md:gap-5">
            <fieldset className="fieldset">
              <legend className="fieldset-legend">Nome</legend>
              <input
                type="text"
                className="input"
                {...register("name")}
                placeholder="Nome Completo"
              />
              {errors?.name && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.name.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Email</legend>
              <input
                type="text"
                className="input"
                {...register("email")}
                placeholder="email@email.com"
              />
              {errors?.email && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.email.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Contato</legend>
              <input
                type="text"
                className="input"
                placeholder="Ex: (61) 99633-2332"
                {...registerWithMask("contact", ["(##) #####-####", "(##) ####-####"])}
              />
              {errors?.contact && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.contact.message}
                </p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">CPF</legend>
              <input
                type="text"
                className="input"
                disabled={true}
                placeholder="Ex: 123.456.789-00"
                {...registerWithMask("cpf", ["###.###.###-##"])}
              />
              {errors?.cpf && (
                <p className="label text-error">
                  <TriangleAlert className="w-3 h-3" />
                  {errors.cpf.message}
                </p>
              )}
            </fieldset>
          </div>

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to={`/parents/${parentId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={updateParent.isPending} variant="primary">
              {updateParent.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
