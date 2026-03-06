import { Link } from "react-router-dom"
import { useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import styles from "@/features/students/StudentCreatePage.module.css"
import { useEffect, useMemo, useState } from "react"
import { useForm } from "react-hook-form"
import { useHookFormMask } from "use-mask-input"
import { zodResolver } from "@hookform/resolvers/zod"
import { createStudentSchema, type CreateStudentInput, type ParentSummary } from "@/lib/schemas"
import { getFriendlyErrorMessage, parentsApi, studentsApi, type PageResponse } from "@/services/api"

export function StudentCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const [parents, setParents] = useState<ParentSummary[]>([])
  const [parentsLoading, setParentsLoading] = useState(true)
  const [parentsError, setParentsError] = useState<string | null>(null)
  const [parentMode, setParentMode] = useState<"existing" | "new">("new")

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<CreateStudentInput>({
    resolver: zodResolver(createStudentSchema),
    shouldUnregister: true,
  })

  const loadParents = async () => {
    try {
      setParentsError(null)
      setParentsLoading(true)

      const res = await parentsApi.listActive(0, 100, "name")
      const page: PageResponse<ParentSummary> = res.data
      setParents(page.content)
      if (page.content.length === 0) setParentMode("new")
    } catch (error) {
      console.error("Falha ao carregar responsaveis:", error)
      setParents([])
      setParentsError(getFriendlyErrorMessage(error))
      setParentMode("new")
    } finally {
      setParentsLoading(false)
    }
  }

  useEffect(() => {
    loadParents()
  }, [])

  const parentIdField = register("parentId")
  const registerWithMask = useHookFormMask(register)
  const selectedParentId = watch("parentId")
  const selectedParentName = useMemo(() => {
    const p = parents.find((x) => x.id === selectedParentId)
    return p?.name ?? ""
  }, [parents, selectedParentId])

  const onSubmit = async (data: CreateStudentInput) => {
    try {
      setSubmitError(null)

      const payload: CreateStudentInput =
        parentMode === "existing"
          ? { ...data, parent: undefined }
          : { ...data, parentId: undefined }

      const res = await studentsApi.create(payload)
      navigate(`/students/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Novo aluno</h1>
          <p className="text-sm text-gray-600">Crie um novo cadastro de aluno.</p>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">← Voltar para alunos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Dados do aluno</CardTitle>
          <CardDescription>Preencha os dados pessoais, endereco e responsavel.</CardDescription>
        </CardHeader>
        <CardContent>
          <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
            <div className={styles.sectionTitle}>Aluno</div>
            <div className={styles.formGrid}>
              <div className={styles.field}>
                <label className={styles.label} htmlFor="name">
                  Nome completo
                </label>
                <Input id="name" placeholder="Ex: Joao Pedro" {...register("name")} />
                {errors.name?.message ? <p className={styles.error}>{errors.name.message}</p> : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="birthdate">
                  Data de nascimento
                </label>
                <Input id="birthdate" type="date" {...register("birthdate")} />
                {errors.birthdate?.message ? (
                  <p className={styles.error}>{errors.birthdate.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="cpf">
                  CPF
                </label>
                <Input
                  id="cpf"
                  placeholder="000.000.000-00"
                  {...registerWithMask("cpf", "999.999.999-99")}
                />
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
            <div className={styles.sectionTitle}>Endereco</div>
            <div className={styles.formGrid}>
              <div className={styles.field + " " + styles.span2}>
                <label className={styles.label} htmlFor="address.street">
                  Rua
                </label>
                <Input id="address.street" placeholder="Ex: Rua das Flores" {...register("address.street")} />
                {errors.address?.street?.message ? (
                  <p className={styles.error}>{errors.address.street.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.number">
                  Numero
                </label>
                <Input id="address.number" placeholder="Ex: 123" {...register("address.number")} />
                {errors.address?.number?.message ? (
                  <p className={styles.error}>{errors.address.number.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.complement">
                  Complemento (opcional)
                </label>
                <Input id="address.complement" placeholder="Apto, bloco, etc" {...register("address.complement")} />
                {errors.address?.complement?.message ? (
                  <p className={styles.error}>{errors.address.complement.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.district">
                  Bairro
                </label>
                <Input id="address.district" placeholder="Ex: Centro" {...register("address.district")} />
                {errors.address?.district?.message ? (
                  <p className={styles.error}>{errors.address.district.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.city">
                  Cidade
                </label>
                <Input id="address.city" placeholder="Ex: Sao Paulo" {...register("address.city")} />
                {errors.address?.city?.message ? (
                  <p className={styles.error}>{errors.address.city.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.state">
                  Estado
                </label>
                <Input id="address.state" placeholder="Ex: SP" {...register("address.state")} />
                {errors.address?.state?.message ? (
                  <p className={styles.error}>{errors.address.state.message}</p>
                ) : null}
              </div>

              <div className={styles.field}>
                <label className={styles.label} htmlFor="address.zip">
                  CEP
                </label>
                <Input
                  id="address.zip"
                  placeholder="00000-000"
                  {...registerWithMask("address.zip", "99999-999")}
                />
                {errors.address?.zip?.message ? (
                  <p className={styles.error}>{errors.address.zip.message}</p>
                ) : null}
              </div>
            </div>

            <div className={styles.divider} />
            <div className={styles.sectionTitle}>Responsavel</div>
            {parentsError ? (
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
                {parentsError}
              </div>
            ) : null}

            <div className={styles.formGrid}>
              {parents.length > 0 ? (
                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="parentMode">
                    Tipo de responsavel
                  </label>
                  <select
                    id="parentMode"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background"
                    value={parentMode}
                    onChange={(e) => setParentMode(e.target.value as "existing" | "new")}
                    disabled={parentsLoading}
                  >
                    <option value="new">Novo responsavel</option>
                    <option value="existing">Responsavel existente</option>
                  </select>
                  {parentsLoading ? <p className={styles.help}>Carregando lista...</p> : null}
                </div>
              ) : (
                <div className={styles.field + " " + styles.span2}>
                  <p className={styles.help}>
                    Nenhum responsavel ativo encontrado. Cadastre um novo responsavel abaixo.
                  </p>
                  {!parentsLoading ? (
                    <Button type="button" variant="outline" onClick={loadParents}>
                      Recarregar responsaveis
                    </Button>
                  ) : null}
                </div>
              )}

              {parentMode === "existing" && parents.length > 0 ? (
                <div className={styles.field + " " + styles.span2}>
                  <label className={styles.label} htmlFor="parentId">
                    Responsavel
                  </label>
                  <select
                    id="parentId"
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                    {...parentIdField}
                    onChange={(e) => {
                      parentIdField.onChange(e)
                      setValue("parentId", e.target.value, { shouldValidate: true })
                    }}
                    defaultValue=""
                    disabled={parentsLoading}
                  >
                    <option value="" disabled>
                      Selecione um responsavel
                    </option>
                    {parents.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name}
                      </option>
                    ))}
                  </select>
                  {selectedParentName ? (
                    <p className={styles.help}>Selecionado: {selectedParentName}</p>
                  ) : null}
                  {errors.parentId?.message ? <p className={styles.error}>{errors.parentId.message}</p> : null}
                </div>
              ) : null}

              {parentMode === "new" ? (
                <>
                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.name">
                      Nome do responsavel
                    </label>
                    <Input id="parent.name" placeholder="Ex: Ana Souza" {...register("parent.name")} />
                    {errors.parent?.name?.message ? (
                      <p className={styles.error}>{errors.parent.name.message}</p>
                    ) : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.email">
                      Email do responsavel
                    </label>
                    <Input
                      id="parent.email"
                      type="email"
                      placeholder="exemplo@dominio.com"
                      {...register("parent.email")}
                    />
                    {errors.parent?.email?.message ? (
                      <p className={styles.error}>{errors.parent.email.message}</p>
                    ) : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.contact">
                      Contato do responsavel
                    </label>
                    <Input
                      id="parent.contact"
                      placeholder="(11) 99999-9999"
                      {...registerWithMask("parent.contact", ["(99) 9999-9999", "(99) 99999-9999"])}
                    />
                    {errors.parent?.contact?.message ? (
                      <p className={styles.error}>{errors.parent.contact.message}</p>
                    ) : null}
                  </div>

                  <div className={styles.field}>
                    <label className={styles.label} htmlFor="parent.cpf">
                      CPF do responsavel
                    </label>
                    <Input
                      id="parent.cpf"
                      placeholder="000.000.000-00"
                      {...registerWithMask("parent.cpf", "999.999.999-99")}
                    />
                    {errors.parent?.cpf?.message ? (
                      <p className={styles.error}>{errors.parent.cpf.message}</p>
                    ) : null}
                  </div>
                </>
              ) : null}
            </div>

            {submitError ? (
              <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
                {submitError}
              </div>
            ) : null}

            <div className={styles.actions}>
              <Button asChild type="button" variant="outline">
                <Link to="/students">Cancelar</Link>
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Salvando..." : "Criar aluno"}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
