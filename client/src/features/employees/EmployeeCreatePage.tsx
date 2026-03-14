import { useMutation, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Button, ButtonLink } from "@/components/ui/button"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/employees/EmployeeCreatePage.module.css"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { queryKeys } from "@/lib/query/queryKeys"
import { employeeFormSchema, type EmployeeFormInput } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage } from "@/services/api"

export function EmployeeCreatePage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmployeeFormInput>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: {
      duty: "TEACHER",
    },
  })

  const createEmployeeMutation = useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.create(data),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (createdEmployee) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.createOptions() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate(`/employees/${createdEmployee.id}`)
    },
    onError: (error) => {
      console.error("Falha ao criar colaborador:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: EmployeeFormInput) => {
    createEmployeeMutation.mutate(data)
  }

  const registerWithMask = useHookFormMask(register)
  const isSubmitting = createEmployeeMutation.isPending

  return (
    <div className={styles.page}>
      <PageHeader
        title="Novo colaborador"
        description="Crie um novo cadastro de colaborador."
        action={
          <ButtonLink to="/employees" variant="outline">
            Voltar para colaboradores
          </ButtonLink>
        }
      />

      <SectionCard title="Dados do colaborador" description="Preencha as informações abaixo para criar o cadastro.">
        <form className={styles.form} onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className={styles.formGrid}>
            <FormField className={styles.field} label="Nome completo" htmlFor="name" error={errors.name?.message}>
              <input className="app-input" id="name" placeholder="Ex: Maria Silva" {...register("name")} />
            </FormField>

            <FormField className={styles.field} label="Data de nascimento" htmlFor="birthdate" error={errors.birthdate?.message}>
              <input className="app-input" id="birthdate" type="date" {...register("birthdate")} />
            </FormField>

            <FormField className={styles.field} label="Email" htmlFor="email" error={errors.email?.message}>
              <input
                className="app-input"
                id="email"
                type="email"
                placeholder="exemplo@dominio.com"
                {...register("email")}
              />
            </FormField>

            <FormField className={styles.field} label="Contato" htmlFor="contact" error={errors.contact?.message}>
              <input
                className="app-input"
                id="contact"
                placeholder="(11) 99999-9999"
                {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])}
              />
            </FormField>

            <FormField className={styles.field} label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
              <input
                className="app-input"
                id="cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("cpf", "999.999.999-99")}
              />
            </FormField>

            <FormField className={styles.field} label="Chave PIX" htmlFor="pix" error={errors.pix?.message}>
              <input
                className="app-input"
                id="pix"
                placeholder="cpf/email/telefone/chave aleatória"
                {...register("pix")}
              />
            </FormField>

            <FormField className={styles.field} label="Função" htmlFor="duty" error={errors.duty?.message}>
              <select id="duty" className="app-select" {...register("duty")}>
                <option value="TEACHER">{dutyLabels.TEACHER}</option>
                <option value="ADM">{dutyLabels.ADM}</option>
                <option value="THERAPIST">{dutyLabels.THERAPIST}</option>
                <option value="MENTOR">{dutyLabels.MENTOR}</option>
              </select>
            </FormField>
          </div>

          {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}

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
  )
}
