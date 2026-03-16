import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { Trash2 } from "lucide-react"
import { useParams } from "react-router-dom"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/events/EventCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventInputSchema, type EmployeeResponse, type EventFormInput, type StudentResponse } from "@/lib/schemas"
import { eventContentLabels, eventContentValues } from "@/lib/shared/enums"
import { getFriendlyErrorMessage, employeesApi, studentsApi } from "@/services/api"
import { useEventDetailQuery, useUpdateEvent, useDeleteEvent } from "./hooks/use-events"
import { useQuery } from "@tanstack/react-query"
import { formatDateTimeLocal } from "@/lib/shared/formatter"

export function EventEditPage() {
  const { id } = useParams<{ id: string }>()
  const eventId = id ?? ""

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<EventFormInput>({
    resolver: zodResolver(eventInputSchema) as any,
    mode: "onBlur",
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")

  const { data: eventData, isLoading: isEventLoading, isError: isEventError, error: eventError, refetch: refetchEvent } = useEventDetailQuery(eventId)

  const dropDownOptionsQuery = useQuery({
    queryKey: [queryKeys.students, queryKeys.employees, "options"],
    queryFn: async (): Promise<{ students: StudentResponse[], employees: EmployeeResponse[] }> => {
      const [studentsRes, employeesRes] = await Promise.all([
        studentsApi.list(0, 100),
        employeesApi.list(0, 100),
      ])
      return {
        students: studentsRes.content,
        employees: employeesRes.content,
      }
    },
  })

  useEffect(() => {
    if (!eventData) return

    setValue("title", eventData.title)
    setValue("description", eventData.description ?? "")
    setValue("startDate", formatDateTimeLocal(eventData.startDate) as any)
    setValue("endDate", formatDateTimeLocal(eventData.endDate) as any)
    setValue("price", Number(eventData.price))
    setValue("payment", Number(eventData.payment))
    setValue("content", eventData.content)
    setValue("studentId", eventData.studentId)
    setValue("employeeId", eventData.employeeId)
  }, [eventData, setValue])

  const students = (dropDownOptionsQuery.data?.students ?? []).filter((student) => !student.archivedAt)
  const employees = (dropDownOptionsQuery.data?.employees ?? []).filter((employee) => !employee.archivedAt)

  const { mutate: updateEvent, isPending: isUpdating, error: updateError } = useUpdateEvent(eventId)
  const { mutate: deleteEvent, isPending: isDeleting, error: deleteError } = useDeleteEvent()

  const onSubmit = (data: EventFormInput) => {
    updateEvent(data)
  }

  const handleDeleteEvent = () => {
    if (!globalThis.confirm("Tem certeza que deseja excluir este evento? Esta ação não pode ser desfeita.")) return
    deleteEvent(eventId)
  }

  const isMutationPending = isUpdating || isDeleting
  const submitError = updateError || deleteError


  if (isEventLoading || dropDownOptionsQuery.isLoading) {
    return <PageLoading message="Carregando evento para edição..." />
  }

  if (isEventError || dropDownOptionsQuery.isError || !eventData) {
    const queryError = eventError ?? dropDownOptionsQuery.error

    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(queryError)}
          onAction={() => Promise.all([refetchEvent(), dropDownOptionsQuery.refetch()])} />
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

          {submitError ? <div className="alert alert-error text-sm">{getFriendlyErrorMessage(submitError)}</div> : null}

          <div className={styles.actions}>
            <Button
              type="button"
              onClick={handleDeleteEvent}
              disabled={isMutationPending}
              variant="error"
              className="sm:mr-auto"
            >
              <Trash2 className="h-4 w-4" />
              Excluir evento
            </Button>
            <ButtonLink to={`/events/${eventId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isMutationPending} variant="primary">
              {isUpdating ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  )
}
