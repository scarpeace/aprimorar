import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { Button, ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import styles from "./ParentCreatePage.module.css";
import { useCreateParent } from "./query/useParentMutations";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { parentFormSchema, type ParentFormInput } from "@/lib/schemas";

const initialParentData: ParentFormInput = {
  name: "Gustavo Scarpellini",
  email: "gustavo.scarpellini@gmail.com",
  contact: "61999293945",
  cpf: "60558245253",
};

export function ParentCreatePage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ParentFormInput>({
    resolver: zodResolver(parentFormSchema),
    defaultValues: initialParentData,
  });
  const registerWithMask = useHookFormMask(register);

  const {
    mutate: createParent,
    isPending: isCreateParentPending,
    isError: isCreateParentError,
    error: createParentError,
  } = useCreateParent();

  const onSubmit = (data: ParentFormInput) => {
    createParent(data);
  };

  return (
    <div className={styles.page}>
      <PageHeader
        title="Novo responsável"
        description="Crie um novo cadastro de responsável."
        action={
          <ButtonLink to="/parents" variant="outline">
            Voltar para responsáveis
          </ButtonLink>
        }
      />

      {isCreateParentError ? (
        <div className="alert alert-error text-sm">
          {getFriendlyErrorMessage(createParentError)}
        </div>
      ) : null}

      <SectionCard
        title="Dados do responsável"
        description="Preencha as informações abaixo para criar o cadastro."
      >
        <form
          className={styles.form}
          onSubmit={handleSubmit(onSubmit)}
          autoComplete="off"
        >
          <div className={styles.formGrid}>
            <FormField
              className={styles.field}
              label="Nome completo"
              htmlFor="name"
              error={errors.name?.message}
            >
              <input
                className="app-input"
                id="name"
                placeholder="Ex: Maria Silva"
                {...register("name")}
              />
            </FormField>

            <FormField
              className={styles.field}
              label="Email"
              htmlFor="email"
              error={errors.email?.message}
            >
              <input
                className="app-input"
                id="email"
                type="email"
                placeholder="exemplo@dominio.com"
                {...register("email")}
              />
            </FormField>

            <FormField
              className={styles.field}
              label="Contato"
              htmlFor="contact"
              error={errors.contact?.message}
            >
              <input
                className="app-input"
                id="contact"
                placeholder="(11) 99999-9999"
                {...registerWithMask("contact", [
                  "(99) 9999-9999",
                  "(99) 99999-9999",
                ])}
              />
            </FormField>

            <FormField
              className={styles.field}
              label="CPF"
              htmlFor="cpf"
              error={errors.cpf?.message}
            >
              <input
                className="app-input"
                id="cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("cpf", "999.999.999-99")}
              />
            </FormField>
          </div>

          <div className={styles.actions}>
            <ButtonLink to="/parents" variant="outline">
              Cancelar
            </ButtonLink>
            <Button
              type="submit"
              disabled={isCreateParentPending}
              variant="primary"
            >
              {isCreateParentPending ? "Salvando..." : "Criar responsável"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  );
}
