import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { Button, ButtonLink } from "@/components/ui/button"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/events/EventCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventFormSchema, type EventFormInput } from "@/lib/schemas"
import { eventContentLabels, eventContentValues } from "@/lib/shared/enums"
import { employeesApi, eventsApi, getFriendlyErrorMessage, studentsApi } from "@/services/api"

const EVENT_OPTIONS_PARAMS = { page: 0, size: 100, sortBy: "name" }
const EMPTY_OPTIONS: EventCreateOptions = {
  students: [],
  employees: [],
}

type EventCreateOptions = {
  students: Awaited<ReturnType<typeof studentsApi.list>>["content"]
  employees: Awaited<ReturnType<typeof employeesApi.list>>["content"]
}

export function EventCreatePage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const [submitError, setSubmitError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<EventFormInput>({
    resolver: zodResolver(eventFormSchema),
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")

  const optionsQuery = useQuery({
    queryKey: queryKeys.events.createOptions(),
    queryFn: async (): Promise<EventCreateOptions> => {
      const [studentsRes, employeesRes] = await Promise.all([
        studentsApi.list(EVENT_OPTIONS_PARAMS.page, EVENT_OPTIONS_PARAMS.size, EVENT_OPTIONS_PARAMS.sortBy),
        employeesApi.list(EVENT_OPTIONS_PARAMS.page, EVENT_OPTIONS_PARAMS.size, EVENT_OPTIONS_PARAMS.sortBy),
      ])

      return {
        students: studentsRes.content,
        employees: employeesRes.content,
      }
    },
    staleTime: 5 * 60_000,
  })

  const students = (optionsQuery.data?.students ?? EMPTY_OPTIONS.students).filter((student) => !student.archivedAt)
  const employees = (optionsQuery.data?.employees ?? EMPTY_OPTIONS.employees).filter((employee) => !employee.archivedAt)

  const createEventMutation = useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.create(data),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (createdEvent, data) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.events.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.byStudentRoot(data.studentId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.byEmployeeRoot(data.employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate(`/events/${createdEvent.id}`)
    },
    onError: (error) => {
      console.error("Falha ao criar evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: EventFormInput) => {
    createEventMutation.mutate(data)
  }

  const isSubmitting = createEventMutation.isPending

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
        {optionsQuery.isLoading ? (
          <div className="app-inline-loading">
            <span className="loading loading-spinner loading-sm text-primary" />
            <span>Carregando opções...</span>
          </div>
        ) : optionsQuery.isError ? (
          <div className="space-y-3">
            <div className="alert alert-error text-sm">{getFriendlyErrorMessage(optionsQuery.error)}</div>
            <Button type="button" onClick={() => void optionsQuery.refetch()} variant="primary">
              Tentar novamente
            </Button>
          </div>
        ) : (
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

            {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}

            <div className={styles.actions}>
              <ButtonLink to="/events" variant="outline">
                Cancelar
              </ButtonLink>
              <Button type="submit" disabled={isSubmitting} variant="primary">
                {isSubmitting ? "Salvando..." : "Criar evento"}
              </Button>
            </div>
          </form>
        )}
      </SectionCard>
    </div>
  )
}
