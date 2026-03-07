import { zodResolver } from "@hookform/resolvers/zod"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
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
import { employeesApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useMemo, useState } from "react"
import { useForm } from "react-hook-form"
import { Link } from "react-router-dom"

type EventFormProps = {
  title: string
  description: string
  cardDescription: string
  submitLabel: string
  initialValues?: CreateEventInput
  submitError: string | null
  onSubmit: (data: CreateEventInput) => Promise<void>
}

export function EventForm({
  title,
  description,
  cardDescription,
  submitLabel,
  initialValues,
  submitError,
  onSubmit,
}: EventFormProps) {
  const [students, setStudents] = useState<StudentResponse[]>([])
  const [employees, setEmployees] = useState<EmployeeResponse[]>([])
  const [loadingOptions, setLoadingOptions] = useState(true)
  const [optionsError, setOptionsError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CreateEventInput>({
    resolver: zodResolver(createEventSchema),
    defaultValues: initialValues,
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")
  const selectedStudentId = watch("studentId")
  const selectedEmployeeId = watch("employeeId")

  useEffect(() => {
    if (initialValues) {
      reset(initialValues)
    }
  }, [initialValues, reset])

  const loadOptions = useCallback(async () => {
    try {
      setOptionsError(null)
      setLoadingOptions(true)

      const [studentsRes, employeesRes] = await Promise.all([
        studentsApi.list(0, 100, "name"),
        employeesApi.listActive(0, 100, "name"),
      ])

      const studentsPage: PageResponse<StudentResponse> = studentsRes.data
      const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data
      setStudents(studentsPage.content)
      setEmployees(employeesPage.content)
    } catch (error) {
      console.error("Falha ao carregar opções:", error)
      setOptionsError(getFriendlyErrorMessage(error))
    } finally {
      setLoadingOptions(false)
    }
  }, [])

  useEffect(() => {
    loadOptions()
  }, [loadOptions])

  const selectedStudentName = useMemo(() => {
    const student = students.find((item) => item.id === selectedStudentId)
    return student?.name ?? ""
  }, [selectedStudentId, students])

  const selectedEmployeeName = useMemo(() => {
    const employee = employees.find((item) => item.id === selectedEmployeeId)
    return employee?.name ?? ""
  }, [employees, selectedEmployeeId])

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{title}</h1>
          <p className="text-sm text-gray-600">{description}</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">← Voltar para eventos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Dados do evento</CardTitle>
          <CardDescription>{cardDescription}</CardDescription>
        </CardHeader>
        <CardContent>
          {loadingOptions ? (
            <div className="text-sm text-muted-foreground">Carregando opções...</div>
          ) : optionsError ? (
            <div className="space-y-3">
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
                {optionsError}
              </div>
              <Button type="button" onClick={loadOptions}>
                Tentar novamente
              </Button>
            </div>
          ) : (
            <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
              <div className={styles.formGrid}>
                <div className={styles.field}>
                  <label className={styles.label} htmlFor="title">
                    Título
                  </label>
                  <Input id="title" placeholder="Ex: Aula de matemática" {...register("title")} />
                  {errors.title?.message ? <p className={styles.error}>{errors.title.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="studentId">
                    Aluno
                  </label>
                  <select
                    id="studentId"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
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
                  {selectedStudentName ? <p className={styles.help}>Selecionado: {selectedStudentName}</p> : null}
                  {errors.studentId?.message ? <p className={styles.error}>{errors.studentId.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="employeeId">
                    Colaborador
                  </label>
                  <select
                    id="employeeId"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
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
                  {selectedEmployeeName ? <p className={styles.help}>Selecionado: {selectedEmployeeName}</p> : null}
                  {errors.employeeId?.message ? <p className={styles.error}>{errors.employeeId.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="content">
                    Conteúdo
                  </label>
                  <select
                    id="content"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                    {...register("content")}
                    defaultValue=""
                  >
                    <option value="" disabled>
                      Selecione um conteúdo
                    </option>
                    {eventContentValues.map((content) => (
                      <option key={content} value={content}>
                        {eventContentLabels[content]}
                      </option>
                    ))}
                  </select>
                  {errors.content?.message ? <p className={styles.error}>{errors.content.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="startDateTime">
                    Início
                  </label>
                  <Input id="startDateTime" type="datetime-local" {...register("startDateTime")} />
                  {errors.startDateTime?.message ? <p className={styles.error}>{errors.startDateTime.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="endDateTime">
                    Fim
                  </label>
                  <Input id="endDateTime" type="datetime-local" {...register("endDateTime")} />
                  {errors.endDateTime?.message ? <p className={styles.error}>{errors.endDateTime.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="price">
                    Preço (receita)
                  </label>
                  <Input id="price" type="number" step="0.01" min="0" {...register("price", { valueAsNumber: true })} />
                  {errors.price?.message ? <p className={styles.error}>{errors.price.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="payment">
                    Pagamento (custo)
                  </label>
                  <Input id="payment" type="number" step="0.01" min="0" {...register("payment", { valueAsNumber: true })} />
                  {errors.payment?.message ? <p className={styles.error}>{errors.payment.message}</p> : null}
                </div>

                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="description">
                    Descrição (opcional)
                  </label>
                  <Textarea id="description" placeholder="Observações do atendimento" {...register("description")} />
                  {errors.description?.message ? <p className={styles.error}>{errors.description.message}</p> : null}
                </div>
              </div>

              {submitError ? (
                <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">{submitError}</div>
              ) : null}

              <div className={styles.actions}>
                <Button asChild type="button" variant="outline">
                  <Link to="/events">Cancelar</Link>
                </Button>
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? "Salvando..." : submitLabel}
                </Button>
              </div>
            </form>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
