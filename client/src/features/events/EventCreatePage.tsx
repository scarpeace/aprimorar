import { zodResolver } from "@hookform/resolvers/zod"
import { useForm, type Resolver } from "react-hook-form"
import { Button, ButtonLink } from "@/components/ui/button"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/events/EventCreatePage.module.css"
import { eventInputSchema, type EventFormInput } from "@/lib/schemas"
import { eventContentLabels, eventContentValues } from "@/lib/shared/enums"
import { getFriendlyErrorMessage } from "@/services/api"
import { useCreateEvent } from "./hooks/use-events"
import { useStudentOptionsQuery } from "@/features/students/hooks/use-students"
import { useEmployeeOptionsQuery } from "@/features/employees/hooks/use-employees"

export function EventCreatePage() {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<EventFormInput>({
    resolver: zodResolver(eventInputSchema) as unknown as Resolver<EventFormInput>,
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")

  // Opções para os dropdowns de aluno e colaborador
  const studentsQuery = useStudentOptionsQuery()
  const employeesQuery = useEmployeeOptionsQuery()

  const students = studentsQuery.data ?? []
  const employees = employeesQuery.data ?? []

  const { mutate: createEvent, isPending: isSubmitting, error: submitError } = useCreateEvent()

  const onSubmit = (data: EventFormInput) => {
    createEvent(data)
  }

  const renderContent = () => {
    if (studentsQuery.isLoading || employeesQuery.isLoading) {
      return (
        <div className="app-inline-loading">
          <span className="loading loading-spinner loading-sm text-primary" />
          <span>Carregando opções...</span>
        </div>
      )
    }

    if (studentsQuery.isError || employeesQuery.isError) {
      const error = studentsQuery.error || employeesQuery.error
      return (
        <div className="space-y-3">
          <div className="alert alert-error text-sm">{getFriendlyErrorMessage(error)}</div>
          <Button
            type="button"
            onClick={() => {
              void studentsQuery.refetch()
              void employeesQuery.refetch()
            }}
            variant="primary"
          >
            Tentar novamente
          </Button>
        </div>
      )
    }

    return (
      <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
        <div className={styles.formGrid}>
          <FormField className={styles.field} label="Título" htmlFor="title" error={errors.title?.message}>
            <input className="app-input" id="title" placeholder="Ex: Aula de matemática" {...register("title")} />
          </FormField>

          <FormField
            className={styles.field}
            label="Aluno"
            htmlFor="studentId"
            error={errors.studentId?.message}
          >
            <select
              id="studentId"
              className="app-select"
              {...studentIdField}
              onChange={(event) => {
                studentIdField.onChange(event)
                setValue("studentId", event.target.value, { shouldValidate: true })
              }}
              defaultValue=""
            >
              <option value="" disabled>
                Selecione um aluno
              </option>
              {students.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.name}
                </option>
              ))}
            </select>
          </FormField>

          <FormField
            className={styles.field}
            label="Colaborador"
            htmlFor="employeeId"
            error={errors.employeeId?.message}
          >
            <select
              id="employeeId"
              className="app-select"
              {...employeeIdField}
              onChange={(event) => {
                employeeIdField.onChange(event)
                setValue("employeeId", event.target.value, { shouldValidate: true })
              }}
              defaultValue=""
            >
              <option value="" disabled>
                Selecione um colaborador
              </option>
              {employees.map((employee) => (
                <option key={employee.id} value={employee.id}>
                  {employee.name}
                </option>
              ))}
            </select>
          </FormField>

          <FormField className={styles.field} label="Conteúdo" htmlFor="content" error={errors.content?.message}>
            <select id="content" className="app-select" {...register("content")} defaultValue="">
              <option value="" disabled>
                Selecione um conteúdo
              </option>
              {eventContentValues.map((content) => (
                <option key={content} value={content}>
                  {eventContentLabels[content]}
                </option>
              ))}
            </select>
          </FormField>

          <FormField className={styles.field} label="Início" htmlFor="startDate" error={errors.startDate?.message}>
            <input className="app-input" id="startDate" type="datetime-local" {...register("startDate")} />
          </FormField>

          <FormField className={styles.field} label="Fim" htmlFor="endDate" error={errors.endDate?.message}>
            <input className="app-input" id="endDate" type="datetime-local" {...register("endDate")} />
          </FormField>

          <FormField className={styles.field} label="Preço (receita)" htmlFor="price" error={errors.price?.message}>
            <input
              className="app-input"
              id="price"
              type="number"
              step="0.01"
              min="0"
              {...register("price", {
                valueAsNumber: true,
                required: "Preço é obrigatório",
              })}
            />
          </FormField>

          <FormField className={styles.field} label="Pagamento (custo)" htmlFor="payment" error={errors.payment?.message}>
            <input
              className="app-input"
              id="payment"
              type="number"
              step="0.01"
              min="0"
              {...register("payment", {
                valueAsNumber: true,
                required: "Pagamento é obrigatório",
              })}
            />
          </FormField>

          <FormField
            className={`${styles.field} ${styles.span2}`}
            label="Descrição (opcional)"
            htmlFor="description"
            error={errors.description?.message}
          >
            <textarea
              className="app-textarea"
              id="description"
              placeholder="Observações do atendimento"
              {...register("description")}
            />
          </FormField>
        </div>

        {submitError ? <div className="alert alert-error text-sm">{getFriendlyErrorMessage(submitError)}</div> : null}

        <div className={styles.actions}>
          <ButtonLink to="/events" variant="outline">
            Cancelar
          </ButtonLink>
          <Button type="submit" disabled={isSubmitting} variant="primary">
            {isSubmitting ? "Salvando..." : "Criar evento"}
          </Button>
        </div>
      </form>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        title="Novo evento"
        description="Crie um novo atendimento/aula."
        action={
          <ButtonLink to="/events" variant="outline">
            Voltar para eventos
          </ButtonLink>
        }
      />

      <SectionCard title="Dados do evento" description="Informe data, valores e participantes do atendimento.">
        {renderContent()}
      </SectionCard>
    </div>
  )
}
