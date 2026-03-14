import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { Trash2 } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/events/EventCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventFormSchema, type EventFormInput } from "@/lib/schemas"
import { eventContentLabels, eventContentValues } from "@/lib/shared/enums"
import { getFriendlyErrorMessage, employeesApi, eventsApi, studentsApi } from "@/services/api"

const EVENT_OPTIONS_PARAMS = { page: 0, size: 100, sortBy: "name" }
const EMPTY_OPTIONS: EventCreateOptions = {
  students: [],
  employees: [],
}

type EventCreateOptions = {
  students: Awaited<ReturnType<typeof studentsApi.list>>["content"]
  employees: Awaited<ReturnType<typeof employeesApi.list>>["content"]
}

function createEmptyEventValues(): EventFormInput {
  return {
    title: "",
    description: "",
    startDate: "",
    endDate: "",
    price: 0,
    payment: 0,
    content: "AULA",
    studentId: "",
    employeeId: "",
  }
}

function formatDateTimeLocalValue(value: string | Date): string {
  const date = value instanceof Date ? value : new Date(value)

  if (Number.isNaN(date.getTime())) {
    return ""
  }

  return [
    date.getFullYear(),
    String(date.getMonth() + 1).padStart(2, "0"),
    String(date.getDate()).padStart(2, "0"),
  ].join("-") + `T${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`
}

export function EventEditPage() {
  const { id } = useParams<{ id: string }>()
  const eventId = id ?? ""
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
    defaultValues: createEmptyEventValues(),
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")

  const eventQuery = useQuery({
    queryKey: queryKeys.events.editDetail(eventId),
    queryFn: () => eventsApi.getByIdForEdit(eventId),
    enabled: Boolean(id),
  })

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

  useEffect(() => {
    if (!eventQuery.data) {
      return
    }

    setValue("title", eventQuery.data.title)
    setValue("description", eventQuery.data.description ?? "")
    setValue("startDate", formatDateTimeLocalValue(eventQuery.data.startDate))
    setValue("endDate", formatDateTimeLocalValue(eventQuery.data.endDate))
    setValue("price", eventQuery.data.price)
    setValue("payment", eventQuery.data.payment)
    setValue("content", eventQuery.data.content)
    setValue("studentId", eventQuery.data.studentId)
    setValue("employeeId", eventQuery.data.employeeId)
  }, [eventQuery.data, setValue])

  const students = (optionsQuery.data?.students ?? EMPTY_OPTIONS.students).filter((student) => !student.archivedAt)
  const employees = (optionsQuery.data?.employees ?? EMPTY_OPTIONS.employees).filter((employee) => !employee.archivedAt)

  const updateEventMutation = useMutation({
    mutationFn: (data: EventFormInput) => eventsApi.update(eventId, data),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (updatedEvent, data) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.events.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.detail(eventId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.editDetail(eventId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.byStudentRoot(data.studentId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.byEmployeeRoot(data.employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate(`/events/${updatedEvent.id}`)
    },
    onError: (error) => {
      console.error("Falha ao atualizar evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const deleteEventMutation = useMutation({
    mutationFn: () => eventsApi.delete(eventId),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async () => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.events.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.detail(eventId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.editDetail(eventId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate("/events")
    },
    onError: (error) => {
      console.error("Falha ao excluir evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: EventFormInput) => {
    updateEventMutation.mutate(data)
  }

  const handleDeleteEvent = () => {
    const confirmed = window.confirm("Tem certeza que deseja excluir este evento? Esta ação não pode ser desfeita.")

    if (!confirmed) {
      return
    }

    deleteEventMutation.mutate()
  }

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do evento não informado." />
      </div>
    )
  }

  if (eventQuery.isLoading || optionsQuery.isLoading) {
    return <PageLoading message="Carregando evento para edição..." />
  }

  if (eventQuery.isError || optionsQuery.isError || !eventQuery.data) {
    const queryError = eventQuery.error ?? optionsQuery.error

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(queryError)} onAction={() => Promise.all([eventQuery.refetch(), optionsQuery.refetch()])} />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar evento"
        description="Atualize os dados do atendimento."
        action={
          <ButtonLink to={`/events/${eventId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <SectionCard title="Dados do evento" description="Atualize data, valores e participantes do atendimento.">
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
            <Button
              type="button"
              onClick={handleDeleteEvent}
              disabled={updateEventMutation.isPending || deleteEventMutation.isPending}
              variant="danger"
              className="sm:mr-auto"
            >
              <Trash2 className="h-4 w-4" />
              Excluir evento
            </Button>
            <ButtonLink to={`/events/${eventId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={updateEventMutation.isPending || deleteEventMutation.isPending} variant="primary">
              {updateEventMutation.isPending ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  )
}
