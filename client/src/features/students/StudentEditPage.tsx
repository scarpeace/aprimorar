import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate, useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/students/StudentCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentFormSchema, type StudentFormInput } from "@/lib/schemas"
import { BRAZILIAN_STATES } from "@/lib/shared/enums/brazilianStates"
import { formatDateInputValue } from "@/lib/shared/formatter"
import { getFriendlyErrorMessage, parentsApi, studentsApi } from "@/services/api"

function createEmptyStudentValues(): StudentFormInput {
  return {
    name: "",
    birthdate: "",
    cpf: "",
    contact: "",
    email: "",
    school: "",
    address: {
      street: "",
      number: "",
      complement: "",
      district: "",
      city: "",
      state: "",
      zip: "",
    },
    parent: {
      name: "",
      email: "",
      contact: "",
      cpf: "",
    },
  }
}

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const [submitError, setSubmitError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<StudentFormInput>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
    shouldUnregister: true,
    defaultValues: createEmptyStudentValues(),
  })

  const registerWithMask = useHookFormMask(register)

  const studentQuery = useQuery({
    queryKey: queryKeys.students.editDetail(studentId),
    queryFn: () => studentsApi.getByIdForEdit(studentId),
    enabled: Boolean(id),
  })

  useEffect(() => {
    if (!studentQuery.data) {
      return
    }

    setValue("name", studentQuery.data.name)
    setValue("birthdate", formatDateInputValue(studentQuery.data.birthdate))
    setValue("cpf", studentQuery.data.cpf)
    setValue("contact", studentQuery.data.contact)
    setValue("email", studentQuery.data.email)
    setValue("school", studentQuery.data.school)
    setValue("address.street", studentQuery.data.address.street)
    setValue("address.number", studentQuery.data.address.number)
    setValue("address.complement", studentQuery.data.address.complement ?? "")
    setValue("address.district", studentQuery.data.address.district)
    setValue("address.city", studentQuery.data.address.city)
    setValue("address.state", studentQuery.data.address.state)
    setValue("address.zip", studentQuery.data.address.zip)
    setValue("parent.name", studentQuery.data.parent.name)
    setValue("parent.email", studentQuery.data.parent.email)
    setValue("parent.contact", studentQuery.data.parent.contact)
    setValue("parent.cpf", studentQuery.data.parent.cpf)
  }, [setValue, studentQuery.data])

  const updateStudentMutation = useMutation({
    mutationFn: async (data: StudentFormInput) => {
      if (!studentQuery.data) {
        throw new Error("Aluno não carregado para edição.")
      }

      const updatedParent = await parentsApi.update(studentQuery.data.parent.id, data.parent!)

      return studentsApi.update(studentId, {
        ...data,
        parent: {
          name: updatedParent.name,
          email: updatedParent.email,
          contact: updatedParent.contact,
          cpf: updatedParent.cpf,
        },
      })
    },
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (updatedStudent) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.students.detail(studentId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.students.editDetail(studentId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.parents.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.parents.detail(studentQuery.data?.parent.id ?? "") }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.createOptions() }),
      ])

      navigate(`/students/${updatedStudent.id}`)
    },
    onError: (error) => {
      console.error("Falha ao atualizar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: StudentFormInput) => {
    updateStudentMutation.mutate(data)
  }

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do aluno não informado." />
      </div>
    )
  }

  if (studentQuery.isLoading) {
    return <PageLoading message="Carregando aluno para edição..." />
  }

  if (studentQuery.isError || !studentQuery.data) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(studentQuery.error)} onAction={studentQuery.refetch} />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar aluno"
        description="Atualize os dados do aluno e do responsável."
        action={
          <ButtonLink to={`/students/${studentId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
        <SectionCard title="Dados do aluno" description="Atualize os dados pessoais e endereço.">
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
              <input className="app-input" id="address.street" placeholder="Ex: Rua das Flores" {...register("address.street")} />
            </FormField>

            <FormField className={styles.field} label="Número" htmlFor="address.number" error={errors.address?.number?.message}>
              <input className="app-input" id="address.number" placeholder="Ex: 123" {...register("address.number")} />
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
              <input className="app-input" id="address.district" placeholder="Ex: Centro" {...register("address.district")} />
            </FormField>

            <FormField className={styles.field} label="Cidade" htmlFor="address.city" error={errors.address?.city?.message}>
              <input className="app-input" id="address.city" placeholder="Ex: São Paulo" {...register("address.city")} />
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

        <SectionCard title="Responsável" description="Atualize os dados do responsável vinculado a este aluno.">
          <div className={styles.formGrid}>
            <FormField className={styles.field} label="Nome do responsável" htmlFor="parent.name" error={errors.parent?.name?.message}>
              <input className="app-input" id="parent.name" placeholder="Ex: Ana Souza" {...register("parent.name")} />
            </FormField>

            <FormField className={styles.field} label="E-mail do responsável" htmlFor="parent.email" error={errors.parent?.email?.message}>
              <input
                className="app-input"
                id="parent.email"
                type="email"
                placeholder="exemplo@dominio.com"
                {...register("parent.email")}
              />
            </FormField>

            <FormField className={styles.field} label="Contato do responsável" htmlFor="parent.contact" error={errors.parent?.contact?.message}>
              <input
                className="app-input"
                id="parent.contact"
                placeholder="(11) 99999-9999"
                {...registerWithMask("parent.contact", ["(99) 9999-9999", "(99) 99999-9999"])}
              />
            </FormField>

            <FormField className={styles.field} label="CPF do responsável" htmlFor="parent.cpf" error={errors.parent?.cpf?.message}>
              <input
                className="app-input"
                id="parent.cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("parent.cpf", "999.999.999-99")}
              />
            </FormField>
          </div>
        </SectionCard>

        {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}

        <div className={styles.actions}>
          <ButtonLink to={`/students/${studentId}`} variant="outline">
            Cancelar
          </ButtonLink>
          <Button type="submit" disabled={updateStudentMutation.isPending} variant="primary">
            {updateStudentMutation.isPending ? "Salvando..." : "Salvar alterações"}
          </Button>
        </div>
      </form>
    </div>
  )
}
