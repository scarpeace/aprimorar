import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { Button, ButtonLink } from "@/components/ui/button";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import styles from "@/features/employees/EmployeeCreatePage.module.css";
import { dutyLabels } from "@/features/employees/schemas/dutyEnum";
import { employeeFormSchema, type EmployeeFormInput } from "@/features/employees/schemas/employee";
import { useCreateEmployee } from "./query/useEmployeeMutations";

export function EmployeeCreatePage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormInput>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: {
      duty: "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const { mutate: createEmployee, isPending: isSubmitting } =
    useCreateEmployee();
  const onSubmit = (data: EmployeeFormInput) => {
    createEmployee(data);
  };

  return (
    <div className={styles.page}>
      <PageHeader
        title="Novo colaborador"
        description="Crie um novo cadastro de colaborador."
        action={
          <ButtonLink className="sm:ml-auto" to="/employees" variant="outline">
            Voltar
          </ButtonLink>
        }
      />

      <SectionCard
        title="Dados do colaborador"
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
              label="Data de nascimento"
              htmlFor="birthdate"
              error={errors.birthdate?.message}
            >
              <input
                className="app-input"
                id="birthdate"
                type="date"
                {...register("birthdate")}
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

            <FormField
              className={styles.field}
              label="Chave PIX"
              htmlFor="pix"
              error={errors.pix?.message}
            >
              <input
                className="app-input"
                id="pix"
                placeholder="cpf/email/telefone/chave aleatória"
                {...register("pix")}
              />
            </FormField>

            <FormField
              className={styles.field}
              label="Função"
              htmlFor="duty"
              error={errors.duty?.message}
            >
              <select id="duty" className="app-select" {...register("duty")}>
                <option value="TEACHER">{dutyLabels.TEACHER}</option>
                <option value="ADM">{dutyLabels.ADM}</option>
                <option value="THERAPIST">{dutyLabels.THERAPIST}</option>
                <option value="MENTOR">{dutyLabels.MENTOR}</option>
              </select>
            </FormField>
          </div>

          <div className={styles.actions}>
            <ButtonLink to="/employees" variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isSubmitting} variant="primary">
              {isSubmitting ? "Salvando..." : "Criar colaborador"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  );
}
