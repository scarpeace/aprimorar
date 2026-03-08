import { zodResolver } from "@hookform/resolvers/zod"
import { ActionErrorBanner } from "@/components/ui/action-error-banner"
import { Button } from "@/components/ui/button"
import { FormActions } from "@/components/ui/form-actions"
import { FormField } from "@/components/ui/form-field"
import { FormPageShell } from "@/components/ui/form-page-shell"
import { Input } from "@/components/ui/input"
import { SelectInput } from "@/components/ui/select-input"
import { Textarea } from "@/components/ui/textarea"
import styles from "@/features/events/components/EventForm.module.css"
import {
  createEventSchema,
  eventContentLabels,
  eventContentValues,
  type CreateEventInput,
  type EmployeeResponse,
  type StudentResponse,
} from "@/lib/schemas"
import { useEffect, useMemo } from "react"
import { useForm, useWatch } from "react-hook-form"
import { Link } from "react-router-dom"

type EventFormProps = {
  title: string
  description: string
  cardDescription: string
  submitLabel: string
  initialValues?: CreateEventInput
  options: {
    students: StudentResponse[]
    employees: EmployeeResponse[]
    loading: boolean
    error: string | null
    onReload: () => Promise<void>
  }
  submitError: string | null
  onSubmit: (data: CreateEventInput) => Promise<void>
}

export function EventForm({
  title,
  description,
  cardDescription,
  submitLabel,
  initialValues,
  options,
  submitError,
  onSubmit,
}: EventFormProps) {
  const { students, employees, loading, error, onReload } = options

  const {
    register,
    handleSubmit,
    setValue,
    reset,
    control,
    formState: { errors, isSubmitting },
  } = useForm<CreateEventInput>({
    resolver: zodResolver(createEventSchema),
    defaultValues: initialValues,
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")
  const selectedStudentId = useWatch({ control, name: "studentId" })
  const selectedEmployeeId = useWatch({ control, name: "employeeId" })

  useEffect(() => {
    if (initialValues) {
      reset(initialValues)
    }
  }, [initialValues, reset])

  const selectedStudentName = useMemo(() => {
    const student = students.find((item) => item.id === selectedStudentId)
    return student?.name ?? ""
  }, [selectedStudentId, students])

  const selectedEmployeeName = useMemo(() => {
    const employee = employees.find((item) => item.id === selectedEmployeeId)
    return employee?.name ?? ""
  }, [employees, selectedEmployeeId])

  return (
    <FormPageShell
      title={title}
      description={description}
      backTo="/events"
      backLabel="← Voltar para eventos"
      cardTitle="Dados do evento"
      cardDescription={cardDescription}
    >
      {loading ? (
        <div className="text-sm text-muted-foreground">Carregando opções...</div>
      ) : error ? (
        <div className="space-y-3">
          <ActionErrorBanner message={error} />
          <Button type="button" onClick={onReload}>
            Tentar novamente
          </Button>
        </div>
      ) : (
        <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
          <div className={styles.formGrid}>
            <FormField label="Título" htmlFor="title" error={errors.title?.message}>
              <Input id="title" placeholder="Ex: Aula de matemática" {...register("title")} />
            </FormField>

            <FormField label="Aluno" htmlFor="studentId" error={errors.studentId?.message} help={selectedStudentName ? `Selecionado: ${selectedStudentName}` : undefined}>
              <SelectInput
                id="studentId"
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
              </SelectInput>
            </FormField>

            <FormField label="Colaborador" htmlFor="employeeId" error={errors.employeeId?.message} help={selectedEmployeeName ? `Selecionado: ${selectedEmployeeName}` : undefined}>
              <SelectInput
                id="employeeId"
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
              </SelectInput>
            </FormField>

            <FormField label="Conteúdo" htmlFor="content" error={errors.content?.message}>
              <SelectInput id="content" {...register("content")} defaultValue="">
                <option value="" disabled>
                  Selecione um conteúdo
                </option>
                {eventContentValues.map((content) => (
                  <option key={content} value={content}>
                    {eventContentLabels[content]}
                  </option>
                ))}
              </SelectInput>
            </FormField>

            <FormField label="Início" htmlFor="startDateTime" error={errors.startDateTime?.message}>
              <Input id="startDateTime" type="datetime-local" {...register("startDateTime")} />
            </FormField>

            <FormField label="Fim" htmlFor="endDateTime" error={errors.endDateTime?.message}>
              <Input id="endDateTime" type="datetime-local" {...register("endDateTime")} />
            </FormField>

            <FormField label="Preço (receita)" htmlFor="price" error={errors.price?.message}>
              <Input id="price" type="number" step="0.01" min="0" {...register("price", { valueAsNumber: true })} />
            </FormField>

            <FormField label="Pagamento (custo)" htmlFor="payment" error={errors.payment?.message}>
              <Input id="payment" type="number" step="0.01" min="0" {...register("payment", { valueAsNumber: true })} />
            </FormField>

            <FormField label="Descrição (opcional)" htmlFor="description" error={errors.description?.message} className={styles.span2}>
              <Textarea id="description" placeholder="Observações do atendimento" {...register("description")} />
            </FormField>
          </div>

          <ActionErrorBanner message={submitError} />

          <FormActions>
            <Button asChild type="button" variant="outline">
              <Link to="/events">Cancelar</Link>
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Salvando..." : submitLabel}
            </Button>
          </FormActions>
        </form>
      )}
    </FormPageShell>
  )
}
