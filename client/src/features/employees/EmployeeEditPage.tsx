import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import { Badge } from "@/components/ui/badge";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import styles from "@/features/employees/EmployeeCreatePage.module.css";
import { dutyLabels } from "@/features/employees/schemas/dutyEnum";
import { employeeFormSchema, type EmployeeFormInput } from "@/features/employees/schemas/employee";
import { formatDateInputValue } from "@/lib/utils/formatter";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useEmployeeEditQuery } from "./query/useEmployeeQueries";
import { useUpdateEmployee } from "./query/useEmployeeMutations";
import { DeleteEmployeeButton } from "./components/DeleteEmployeeButton";
import { EditEmployeeButton } from "./components/EditEmployeeButton";
import { Save } from "lucide-react";

export function EmployeeEditPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";

  const {
    data: employeeData,
    isLoading: isEmployeeLoading,
    isError: isEmployeeError,
    error: employeeError,
    refetch: refetchEmployee,
  } = useEmployeeEditQuery(employeeId);

  const { mutate: updateEmployee, isPending: isUpdating } =
    useUpdateEmployee(employeeId);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormInput>({
    resolver: zodResolver(employeeFormSchema),
    values: {
      name: employeeData?.name ?? "",
      email: employeeData?.email ?? "",
      contact: employeeData?.contact ?? "",
      cpf: employeeData?.cpf ?? "",
      pix: employeeData?.pix ?? "",
      birthdate: employeeData?.birthdate
        ? formatDateInputValue(employeeData.birthdate)
        : "",
      duty: employeeData?.duty ?? "TEACHER",
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = (data: EmployeeFormInput) => {
    updateEmployee(data);
  };

  if (isEmployeeLoading) {
    return <PageLoading message="Carregando colaborador para edição..." />;
  }

  if (isEmployeeError || !employeeData) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(employeeError)}
          onAction={refetchEmployee}
        />
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar colaborador"
        description="Atualize os dados do colaborador."
        action={
          <ButtonLink to={`/employees/${employeeId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <SectionCard
        title="Dados do colaborador"
        description="Atualize as informações de cadastro e contato."
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
            <DeleteEmployeeButton employeeId={employeeId} />
            <ButtonLink to={`/employees/${employeeId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isUpdating} variant="success">
              <Save />
              {isUpdating ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  );
}
