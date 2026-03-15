import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Button, ButtonLink } from "@/components/ui/button"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/students/StudentCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentInputSchema, type StudentFormInput } from "@/lib/schemas"
import { BRAZILIAN_STATES } from "@/lib/shared/enums/brazilianStates"
import { getFriendlyErrorMessage, parentsApi, studentsApi } from "@/services/api"

const PARENTS_LIST_PARAMS = { page: 0, size: 100, sortBy: "name" }
const STUDENT_PARANA: StudentFormInput = {
  name: "Lucas Pereira",
  birthdate: "2016-07-30",
  cpf: "456.789.012-33",
  contact: "(41) 99911-2233",
  email: "lucas.pereira@exemplo.com",
  school: "Escola Nossa Senhora",
  address: {
    street: "Rua das Rosas",
    number: "15",
    complement: "",
    district: "Batel",
    city: "Curitiba",
    state: "PR",
    zip: "80420-000",
  },
  parent: {
    name: "Marcos Pereira",
    email: "marcos.pereira@exemplo.com",
    contact: "(41) 95544-3322",
    cpf: "334.445.556-78",
  },
}

export function StudentCreatePage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const [submitError, setSubmitError] = useState<string | null>(null)
  const [parentMode, setParentMode] = useState<"existing" | "new">("new")
  const [selectedParentId, setSelectedParentId] = useState("")

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<StudentFormInput>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
    shouldUnregister: true,
    defaultValues: STUDENT_PARANA,
  })

  const {
    data: parentsPage,
    isLoading: parentsLoading,
    isError: isParentsError,
    error: parentsQueryError,
    refetch: refetchParents,
  } = useQuery({
    queryKey: [...queryKeys.parents, PARENTS_LIST_PARAMS],
    queryFn: () =>
      parentsApi.listPaginated(PARENTS_LIST_PARAMS.page, PARENTS_LIST_PARAMS.size, PARENTS_LIST_PARAMS.sortBy),
  })

  const parents = parentsPage?.content ?? []
  const parentsError = isParentsError ? getFriendlyErrorMessage(parentsQueryError) : null
  const effectiveParentMode = parents.length > 0 ? parentMode : "new"

  const registerWithMask = useHookFormMask(register)

  useEffect(() => {
    if (effectiveParentMode !== "existing") {
      return
    }

    const selectedParent = parents.find((item: { id: string }) => item.id === selectedParentId)
    if (!selectedParent) {
      return
    }

    setValue("parent.name", selectedParent.name, { shouldDirty: true, shouldValidate: true })
    setValue("parent.email", selectedParent.email, { shouldDirty: true, shouldValidate: true })
    setValue("parent.contact", selectedParent.contact, { shouldDirty: true, shouldValidate: true })
    setValue("parent.cpf", selectedParent.cpf, { shouldDirty: true, shouldValidate: true })
  }, [effectiveParentMode, parents, selectedParentId, setValue])

  const createStudentMutation = useMutation({
    mutationFn: async (data: StudentFormInput) => {
      const payload: StudentFormInput =
        effectiveParentMode === "existing" && selectedParentId
          ? {
            ...data,
            parent: await parentsApi.getById(selectedParentId),
          }
          : data

      return studentsApi.create(payload)
    },
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (createdStudent) => {
      queryClient.removeQueries({ queryKey: queryKeys.students })
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.parents }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
      ])

      navigate(`/students/${createdStudent.id}`)
    },
    onError: (error) => {
      console.error("Falha ao criar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: StudentFormInput) => {
    createStudentMutation.mutate(data)
  }

  const isSubmitting = createStudentMutation.isPending

  return (
    <div className={styles.page}>
      <PageHeader
        title="Novo aluno"
        description="Crie um novo cadastro de aluno."
        action={
          <ButtonLink to="/students" variant="outline">
            Voltar para alunos
          </ButtonLink>
        }
      />

      <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
        <SectionCard title="Dados do aluno" description="Preencha os dados pessoais e endereço.">
          <div className={styles.sectionTitle}>Aluno</div>

          <div className={styles.formGrid}>
            <FormField className={styles.field} label="Nome completo" htmlFor="name" error={errors.name?.message}>
              <input className="app-input" id="name" placeholder="Ex: João Pedro" {...register("name")} />
            </FormField>

            <FormField className={styles.field} label="Data de nascimento" htmlFor="birthdate" error={errors.birthdate?.message}>
              <input className="app-input" id="birthdate" type="date" {...register("birthdate")} />
            </FormField>

            <FormField className={styles.field} label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
              <input
                className="app-input"
                id="cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("cpf", "999.999.999-99")}
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

            <FormField className={styles.field} label="Email" htmlFor="email" error={errors.email?.message}>
              <input
                className="app-input"
                id="email"
                type="email"
                placeholder="exemplo@dominio.com"
                {...register("email")}
              />
            </FormField>

            <FormField className={styles.field} label="Escola" htmlFor="school" error={errors.school?.message}>
              <input className="app-input" id="school" placeholder="Ex: Escola Estadual X" {...register("school")} />
            </FormField>
          </div>

          <div className={styles.divider} />
          <div className={styles.sectionTitle}>Endereço</div>

          <div className={styles.formGrid}>
            <FormField
              className={`${styles.field} ${styles.span2}`}
              label="Rua"
              htmlFor="address.street"
              error={errors.address?.street?.message}
            >
              <input
                className="app-input"
                id="address.street"
                placeholder="Ex: Rua das Flores"
                {...register("address.street")}
              />
            </FormField>

            <FormField className={styles.field} label="Número" htmlFor="address.number" error={errors.address?.number?.message}>
              <input
                className="app-input"
                id="address.number"
                placeholder="Ex: 123"
                {...register("address.number")}
              />
            </FormField>

            <FormField
              className={styles.field}
              label="Complemento (opcional)"
              htmlFor="address.complement"
              error={errors.address?.complement?.message}
            >
              <input
                className="app-input"
                id="address.complement"
                placeholder="Apto, bloco, etc"
                {...register("address.complement")}
              />
            </FormField>

            <FormField className={styles.field} label="Bairro" htmlFor="address.district" error={errors.address?.district?.message}>
              <input
                className="app-input"
                id="address.district"
                placeholder="Ex: Centro"
                {...register("address.district")}
              />
            </FormField>

            <FormField className={styles.field} label="Cidade" htmlFor="address.city" error={errors.address?.city?.message}>
              <input
                className="app-input"
                id="address.city"
                placeholder="Ex: São Paulo"
                {...register("address.city")}
              />
            </FormField>

            <FormField className={styles.field} label="Estado" htmlFor="address.state" error={errors.address?.state?.message}>
              <select className="app-select" id="address.state" {...register("address.state")}>
                <option value="">Selecione um estado</option>
                {BRAZILIAN_STATES.map((state) => (
                  <option key={state.value} value={state.value}>
                    {state.label}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField className={styles.field} label="CEP" htmlFor="address.zip" error={errors.address?.zip?.message}>
              <input
                className="app-input"
                id="address.zip"
                placeholder="00000-000"
                {...registerWithMask("address.zip", "99999-999")}
              />
            </FormField>
          </div>
        </SectionCard>

        <SectionCard title="Responsável" description="Selecione um responsável existente ou cadastre um novo.">
          {parentsError ? <div className="alert alert-error text-sm">{parentsError}</div> : null}

          <div className={styles.formGrid}>
            {parents.length > 0 ? (
              <FormField
                className={`${styles.field} ${styles.span2}`}
                label="Tipo de responsável"
                htmlFor="parentMode"
                hint={
                  parentsLoading ? (
                    <span className="app-inline-loading">
                      <span className="loading loading-spinner loading-sm text-primary" />
                      <span>Carregando lista...</span>
                    </span>
                  ) : undefined
                }
              >
                <select
                  id="parentMode"
                  className="app-select"
                  value={parentMode}
                  onChange={(event) => {
                    const mode = event.target.value as "existing" | "new"
                    setParentMode(mode)
                    if (mode === "new") setSelectedParentId("")
                  }}
                  disabled={parentsLoading}
                >
                  <option value="new">Novo responsável</option>
                  <option value="existing">Responsável existente</option>
                </select>
              </FormField>
            ) : (
              <div className={`${styles.field} ${styles.span2}`}>
                <p className={styles.help}>Nenhum responsável ativo encontrado. Cadastre um novo responsável abaixo.</p>
                {!parentsLoading ? (
                  <Button type="button" onClick={() => void refetchParents()} variant="outline">
                    Recarregar responsáveis
                  </Button>
                ) : null}
              </div>
            )}

            {effectiveParentMode === "existing" && parents.length > 0 ? (
              <FormField
                className={`${styles.field} ${styles.span2}`}
                label="Responsável"
                htmlFor="parentId"
              >
                <select
                  id="parentId"
                  className="app-select"
                  value={selectedParentId}
                  onChange={(event) => setSelectedParentId(event.target.value)}
                  disabled={parentsLoading}
                >
                  <option value="" disabled>
                    Selecione um responsável
                  </option>
                  {parents.map((parent: any) => (
                    <option key={parent.id} value={parent.id}>
                      {parent.name}
                    </option>
                  ))}
                </select>
              </FormField>
            ) : null}

            {effectiveParentMode === "new" || effectiveParentMode === "existing" ? (
              <>
                <FormField
                  className={styles.field}
                  label="Nome do responsável"
                  htmlFor="parent.name"
                  error={errors.parent?.name?.message}
                >
                  <input
                    className="app-input"
                    id="parent.name"
                    placeholder="Ex: Ana Souza"
                    readOnly={effectiveParentMode === "existing"}
                    {...register("parent.name")}
                  />
                </FormField>

                <FormField
                  className={styles.field}
                  label="E-mail do responsável"
                  htmlFor="parent.email"
                  error={errors.parent?.email?.message}
                >
                  <input
                    className="app-input"
                    id="parent.email"
                    type="email"
                    placeholder="exemplo@dominio.com"
                    readOnly={effectiveParentMode === "existing"}
                    {...register("parent.email")}
                  />
                </FormField>

                <FormField
                  className={styles.field}
                  label="Contato do responsável"
                  htmlFor="parent.contact"
                  error={errors.parent?.contact?.message}
                >
                  <input
                    className="app-input"
                    id="parent.contact"
                    placeholder="(11) 99999-9999"
                    readOnly={effectiveParentMode === "existing"}
                    {...registerWithMask("parent.contact", ["(99) 9999-9999", "(99) 99999-9999"])}
                  />
                </FormField>

                <FormField
                  className={styles.field}
                  label="CPF do responsável"
                  htmlFor="parent.cpf"
                  error={errors.parent?.cpf?.message}
                >
                  <input
                    className="app-input"
                    id="parent.cpf"
                    placeholder="000.000.000-00"
                    readOnly={effectiveParentMode === "existing"}
                    {...registerWithMask("parent.cpf", "999.999.999-99")}
                  />
                </FormField>
              </>
            ) : null}
          </div>
        </SectionCard>

        {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}

        <div className={styles.actions}>
          <ButtonLink to="/students" variant="outline">
            Cancelar
          </ButtonLink>
          <Button type="submit" disabled={isSubmitting} variant="primary">
            {isSubmitting ? "Salvando..." : "Criar aluno"}
          </Button>
        </div>
      </form>
    </div>
  )
}
