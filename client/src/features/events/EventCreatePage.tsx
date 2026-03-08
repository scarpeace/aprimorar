import { Link } from "react-router-dom"
import { useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { InlineLoading } from "@/components/ui/inline-loading"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import styles from "@/features/events/EventCreatePage.module.css"
import { useEffect, useMemo, useState } from "react"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import type { EmployeeResponse, StudentResponse } from "@/lib/schemas"
import { createEventSchema, eventContentLabels, eventContentValues, type CreateEventInput } from "@/lib/schemas"
import { employeesApi, eventsApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"

export function EventCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [students, setStudents] = useState<StudentResponse[]>([])
  const [employees, setEmployees] = useState<EmployeeResponse[]>([])
  const [loadingOptions, setLoadingOptions] = useState(true)
  const [optionsError, setOptionsError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<CreateEventInput>({
    resolver: zodResolver(createEventSchema),
  })

  const studentIdField = register("studentId")
  const employeeIdField = register("employeeId")

  const selectedStudentId = watch("studentId")
  const selectedEmployeeId = watch("employeeId")

  const selectedStudentName = useMemo(() => {
    const s = students.find((x) => x.id === selectedStudentId)
    return s?.name ?? ""
  }, [students, selectedStudentId])

  const selectedEmployeeName = useMemo(() => {
    const e = employees.find((x) => x.id === selectedEmployeeId)
    return e?.name ?? ""
  }, [employees, selectedEmployeeId])

  const loadOptions = async () => {
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
  }

  useEffect(() => {
    loadOptions()
  }, [])

  const onSubmit = async (data: CreateEventInput) => {
    try {
      setSubmitError(null)
      const res = await eventsApi.create(data)
      navigate(`/events/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo evento</h1>
          <p className="text-sm text-gray-600">Crie um novo atendimento/aula.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">← Voltar para eventos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Dados do evento</CardTitle>
          <CardDescription>Informe data, valores e participantes do atendimento.</CardDescription>
        </CardHeader>
        <CardContent>
          {loadingOptions ? (
            <InlineLoading message="Carregando opções..." />
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
                    onChange={(e) => {
                      studentIdField.onChange(e)
                      setValue("studentId", e.target.value, { shouldValidate: true })
                    }}
                    defaultValue=""
                  >
                    <option value="" disabled>
                      Selecione um aluno
                    </option>
                    {students.map((s) => (
                      <option key={s.id} value={s.id}>
                        {s.name}
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
                    onChange={(e) => {
                      employeeIdField.onChange(e)
                      setValue("employeeId", e.target.value, { shouldValidate: true })
                    }}
                    defaultValue=""
                  >
                    <option value="" disabled>
                      Selecione um colaborador
                    </option>
                    {employees.map((e) => (
                      <option key={e.id} value={e.id}>
                        {e.name}
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
                {errors.startDateTime?.message ? (
                  <p className={styles.error}>{errors.startDateTime.message}</p>
                ) : null}
                </div>

                <div className={styles.field}>
                <label className={styles.label} htmlFor="endDateTime">
                  Fim
                </label>
                <Input id="endDateTime" type="datetime-local" {...register("endDateTime")} />
                {errors.endDateTime?.message ? (
                  <p className={styles.error}>{errors.endDateTime.message}</p>
                ) : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="price">
                    Preço (receita)
                  </label>
                  <Input
                    id="price"
                    type="number"
                    step="0.01"
                    min="0"
                    {...register("price", {
                      valueAsNumber: true,
                      required: "Preço é obrigatório",
                    })}
                  />
                  {errors.price?.message ? <p className={styles.error}>{errors.price.message}</p> : null}
                </div>

                <div className={styles.field}>
                  <label className={styles.label} htmlFor="payment">
                    Pagamento (custo)
                  </label>
                  <Input
                    id="payment"
                    type="number"
                    step="0.01"
                    min="0"
                    {...register("payment", {
                      valueAsNumber: true,
                      required: "Pagamento é obrigatório",
                    })}
                  />
                  {errors.payment?.message ? <p className={styles.error}>{errors.payment.message}</p> : null}
                </div>

                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="description">
                    Descrição (opcional)
                  </label>
                  <Textarea id="description" placeholder="Observações do atendimento" {...register("description")} />
                  {errors.description?.message ? (
                    <p className={styles.error}>{errors.description.message}</p>
                  ) : null}
                </div>
              </div>

              {submitError ? (
                <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
                  {submitError}
                </div>
              ) : null}

              <div className={styles.actions}>
                <Button asChild type="button" variant="outline">
                  <Link to="/events">Cancelar</Link>
                </Button>
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? "Salvando..." : "Criar evento"}
                </Button>
              </div>
            </form>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
