import { zodResolver } from "@hookform/resolvers/zod"
import { ActionErrorBanner } from "@/components/ui/action-error-banner"
import { Button } from "@/components/ui/button"
import { FormActions } from "@/components/ui/form-actions"
import { FormField } from "@/components/ui/form-field"
import { FormPageShell } from "@/components/ui/form-page-shell"
import { Input } from "@/components/ui/input"
import { SelectInput } from "@/components/ui/select-input"
import { createEmployeeSchema, type CreateEmployeeInput } from "@/lib/schemas"
import styles from "@/features/employees/components/EmployeeForm.module.css"
import { EMPLOYEE_ROLE_OPTIONS } from "@/features/employees/utils/employeeFormUtils"
import { useForm } from "react-hook-form"
import { Link } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"

type EmployeeFormProps = {
  title: string
  description: string
  cardDescription: string
  submitLabel: string
  submitError: string | null
  initialValues?: CreateEmployeeInput
  onSubmit: (data: CreateEmployeeInput) => Promise<void>
}

export function EmployeeForm({
  title,
  description,
  cardDescription,
  submitLabel,
  submitError,
  initialValues,
  onSubmit,
}: EmployeeFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateEmployeeInput>({
    resolver: zodResolver(createEmployeeSchema),
    defaultValues: initialValues ?? {
      role: "EMPLOYEE",
    },
  })

  const registerWithMask = useHookFormMask(register)

  return (
    <FormPageShell
      title={title}
      description={description}
      backTo="/employees"
      backLabel="← Voltar para colaboradores"
      cardTitle="Dados do colaborador"
      cardDescription={cardDescription}
    >
      <form className={styles.form} onSubmit={handleSubmit(onSubmit)} autoComplete="off">
        <div className={styles.formGrid}>
          <FormField label="Nome completo" htmlFor="name" error={errors.name?.message}>
            <Input id="name" placeholder="Ex: Maria Silva" {...register("name")} />
          </FormField>

          <FormField label="Data de nascimento" htmlFor="birthdate" error={errors.birthdate?.message}>
            <Input id="birthdate" type="date" {...register("birthdate")} />
          </FormField>

          <FormField label="Email" htmlFor="email" error={errors.email?.message}>
            <Input id="email" type="email" placeholder="exemplo@dominio.com" {...register("email")} />
          </FormField>

          <FormField label="Contato" htmlFor="contact" error={errors.contact?.message}>
            <Input id="contact" placeholder="(11) 99999-9999" {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])} />
          </FormField>

          <FormField label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
            <Input id="cpf" placeholder="000.000.000-00" {...registerWithMask("cpf", "999.999.999-99")} />
          </FormField>

          <FormField label="Chave PIX" htmlFor="pix" error={errors.pix?.message}>
            <Input id="pix" placeholder="cpf/email/telefone/chave aleatória" {...register("pix")} />
          </FormField>

          <FormField label="Perfil" htmlFor="role" error={errors.role?.message}>
            <SelectInput id="role" {...register("role")}>
              {EMPLOYEE_ROLE_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </SelectInput>
          </FormField>
        </div>

        <ActionErrorBanner message={submitError} />

        <FormActions>
          <Button asChild type="button" variant="outline">
            <Link to="/employees">Cancelar</Link>
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Salvando..." : submitLabel}
          </Button>
        </FormActions>
      </form>
    </FormPageShell>
  )
}
