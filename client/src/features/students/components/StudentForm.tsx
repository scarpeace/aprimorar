import { zodResolver } from "@hookform/resolvers/zod"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import styles from "@/features/students/components/StudentForm.module.css"
import {
  createStudentSchema,
  type CreateStudentInput,
  type ParentSummary,
} from "@/lib/schemas"
import { getFriendlyErrorMessage, parentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useMemo, useState } from "react"
import { useForm } from "react-hook-form"
import { Link } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import type { StudentParentMode } from "@/features/students/utils/studentFormUtils"

type StudentFormProps = {
  title: string
  description: string
  cardDescription: string
  submitLabel: string
  initialValues?: CreateStudentInput
  initialParentMode?: StudentParentMode
  submitError: string | null
  onSubmit: (data: CreateStudentInput, parentMode: StudentParentMode) => Promise<void>
}

export function StudentForm({
  title,
  description,
  cardDescription,
  submitLabel,
  initialValues,
  initialParentMode = "new",
  submitError,
  onSubmit,
}: StudentFormProps) {
  const [parents, setParents] = useState<ParentSummary[]>([])
  const [parentsLoading, setParentsLoading] = useState(true)
  const [parentsError, setParentsError] = useState<string | null>(null)
  const [parentMode, setParentMode] = useState<StudentParentMode>(initialParentMode)

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CreateStudentInput>({
    resolver: zodResolver(createStudentSchema),
    shouldUnregister: true,
    defaultValues: initialValues,
  })

  const parentIdField = register("parentId")
  const registerWithMask = useHookFormMask(register)
  const selectedParentId = watch("parentId")

  useEffect(() => {
    if (initialValues) {
      reset(initialValues)
    }
  }, [initialValues, reset])

  useEffect(() => {
    setParentMode(initialParentMode)
  }, [initialParentMode])

  const loadParents = useCallback(async () => {
    try {
      setParentsError(null)
      setParentsLoading(true)

      const res = await parentsApi.listActive(0, 100, "name")
      const page: PageResponse<ParentSummary> = res.data

      setParents(page.content)
      if (page.content.length === 0) {
        setParentMode("new")
      }
    } catch (error) {
      console.error("Falha ao carregar responsáveis:", error)
      setParents([])
      setParentsError(getFriendlyErrorMessage(error))
      setParentMode("new")
    } finally {
      setParentsLoading(false)
    }
  }, [])

  useEffect(() => {
    loadParents()
  }, [loadParents])

  const selectedParentName = useMemo(() => {
    const parent = parents.find((item) => item.id === selectedParentId)
    return parent?.name ?? ""
  }, [parents, selectedParentId])

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{title}</h1>
          <p className="text-sm text-gray-600">{description}</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">← Voltar para alunos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Dados do aluno</CardTitle>
          <CardDescription>{cardDescription}</CardDescription>
        </CardHeader>
        <CardContent>
          <form className={styles.form} onSubmit={handleSubmit((data) => onSubmit(data, parentMode))}>
            <div className={styles.sectionTitle}>Aluno</div>
            <div className={styles.formGrid}>
              <div className={styles.field}>
                <label className={styles.label} htmlFor="name">
                  Nome completo
                </label>
                <Input id="name" placeholder="Ex: João Pedro" {...register("name")} />
                {errors.name?.message ? <p className={styles.error}>{errors.name.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="birthdate">
                  Data de nascimento
                </label>
                <Input id="birthdate" type="date" {...register("birthdate")} />
                {errors.birthdate?.message ? <p className={styles.error}>{errors.birthdate.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="cpf">
                  CPF
                </label>
                <Input id="cpf" placeholder="000.000.000-00" {...registerWithMask("cpf", "999.999.999-99")} />
                {errors.cpf?.message ? <p className={styles.error}>{errors.cpf.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="contact">
                  Contato
                </label>
                <Input
                  id="contact"
                  placeholder="(11) 99999-9999"
                  {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])}
                />
                {errors.contact?.message ? <p className={styles.error}>{errors.contact.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="email">
                  Email
                </label>
                <Input id="email" type="email" placeholder="exemplo@dominio.com" {...register("email")} />
                {errors.email?.message ? <p className={styles.error}>{errors.email.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="school">
                  Escola
                </label>
                <Input id="school" placeholder="Ex: Escola Estadual X" {...register("school")} />
                {errors.school?.message ? <p className={styles.error}>{errors.school.message}</p> : null}
              </div>
            </div>

            <div className={styles.divider} />
            <div className={styles.sectionTitle}>Endereço</div>
            <div className={styles.formGrid}>
              <div className={styles.field + " " + styles.span2}>
                <label className={styles.label} htmlFor="address.street">
                  Rua
                </label>
                <Input id="address.street" placeholder="Ex: Rua das Flores" {...register("address.street")} />
                {errors.address?.street?.message ? <p className={styles.error}>{errors.address.street.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.number">
                  Número
                </label>
                <Input id="address.number" placeholder="Ex: 123" {...register("address.number")} />
                {errors.address?.number?.message ? <p className={styles.error}>{errors.address.number.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.complement">
                  Complemento (opcional)
                </label>
                <Input id="address.complement" placeholder="Apto, bloco, etc" {...register("address.complement")} />
                {errors.address?.complement?.message ? <p className={styles.error}>{errors.address.complement.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.district">
                  Bairro
                </label>
                <Input id="address.district" placeholder="Ex: Centro" {...register("address.district")} />
                {errors.address?.district?.message ? <p className={styles.error}>{errors.address.district.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.city">
                  Cidade
                </label>
                <Input id="address.city" placeholder="Ex: São Paulo" {...register("address.city")} />
                {errors.address?.city?.message ? <p className={styles.error}>{errors.address.city.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.state">
                  Estado
                </label>
                <Input id="address.state" placeholder="Ex: SP" {...register("address.state")} />
                {errors.address?.state?.message ? <p className={styles.error}>{errors.address.state.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.zip">
                  CEP
                </label>
                <Input id="address.zip" placeholder="00000-000" {...registerWithMask("address.zip", "99999-999")} />
                {errors.address?.zip?.message ? <p className={styles.error}>{errors.address.zip.message}</p> : null}
              </div>
            </div>

            <div className={styles.divider} />
            <div className={styles.sectionTitle}>Responsável</div>
            {parentsError ? (
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">{parentsError}</div>
            ) : null}

            <div className={styles.formGrid}>
              {parents.length > 0 ? (
                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="parentMode">
                    Tipo de responsável
                  </label>
                  <select
                    id="parentMode"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background"
                    value={parentMode}
                    onChange={(event) => setParentMode(event.target.value as StudentParentMode)}
                    disabled={parentsLoading}
                  >
                    <option value="new">Novo responsável</option>
                    <option value="existing">Responsável existente</option>
                  </select>
                  {parentsLoading ? <p className={styles.help}>Carregando lista...</p> : null}
                </div>
              ) : (
                <div className={styles.field + " " + styles.span2}>
                  <p className={styles.help}>Nenhum responsável ativo encontrado. Cadastre um novo responsável abaixo.</p>
                  {!parentsLoading ? (
                    <Button type="button" variant="outline" onClick={loadParents}>
                      Recarregar responsáveis
                    </Button>
                  ) : null}
                </div>
              )}

              {parentMode === "existing" && parents.length > 0 ? (
                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="parentId">
                    Responsável
                  </label>
                  <select
                    id="parentId"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                    {...parentIdField}
                    onChange={(event) => {
                      parentIdField.onChange(event)
                      setValue("parentId", event.target.value, { shouldValidate: true })
                    }}
                    defaultValue=""
                    disabled={parentsLoading}
                  >
                    <option value="" disabled>
                      Selecione um responsável
                    </option>
                    {parents.map((parent) => (
                      <option key={parent.id} value={parent.id}>
                        {parent.name}
                      </option>
                    ))}
                  </select>
                  {selectedParentName ? <p className={styles.help}>Selecionado: {selectedParentName}</p> : null}
                  {errors.parentId?.message ? <p className={styles.error}>{errors.parentId.message}</p> : null}
                </div>
              ) : null}

              {parentMode === "new" ? (
                <>
                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.name">
                      Nome do responsável
                    </label>
                    <Input id="parent.name" placeholder="Ex: Ana Souza" {...register("parent.name")} />
                    {errors.parent?.name?.message ? <p className={styles.error}>{errors.parent.name.message}</p> : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.email">
                      Email do responsável
                    </label>
                    <Input id="parent.email" type="email" placeholder="exemplo@dominio.com" {...register("parent.email")} />
                    {errors.parent?.email?.message ? <p className={styles.error}>{errors.parent.email.message}</p> : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.contact">
                      Contato do responsável
                    </label>
                    <Input
                      id="parent.contact"
                      placeholder="(11) 99999-9999"
                      {...registerWithMask("parent.contact", ["(99) 9999-9999", "(99) 99999-9999"])}
                    />
                    {errors.parent?.contact?.message ? <p className={styles.error}>{errors.parent.contact.message}</p> : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.cpf">
                      CPF do responsável
                    </label>
                    <Input
                      id="parent.cpf"
                      placeholder="000.000.000-00"
                      {...registerWithMask("parent.cpf", "999.999.999-99")}
                    />
                    {errors.parent?.cpf?.message ? <p className={styles.error}>{errors.parent.cpf.message}</p> : null}
                  </div>
                </>
              ) : null}
            </div>

            {submitError ? (
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">{submitError}</div>
            ) : null}

            <div className={styles.actions}>
              <Button asChild type="button" variant="outline">
                <Link to="/students">Cancelar</Link>
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Salvando..." : submitLabel}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
