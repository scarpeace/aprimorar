import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Trash2 } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/students/StudentCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentInputSchema, type StudentFormInput } from "@/lib/schemas"
import { BRAZILIAN_STATES } from "@/lib/shared/enums/brazilianStates"
import { formatDateInputValue } from "@/lib/shared/formatter"
import { getFriendlyErrorMessage, eventsApi } from "@/services/api"
import { useStudentDetailQuery, useUpdateStudent, useDeleteStudent } from "./hooks/use-students"
import { useQuery } from "@tanstack/react-query"
import { useParentsListQuery } from "../parents/hooks/use-parents"

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""
  const [selectedParentId, setSelectedParentId] = useState("")

  const { data: student, isLoading: isStudentLoading, isError: isStudentError, error: studentError, refetch: refetchStudent } = useStudentDetailQuery(studentId)

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<StudentFormInput>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
  })
  const registerWithMask = useHookFormMask(register)

  const {
    data: parentsList,
    isLoading: isparentsListLoading,
    error: parentsListQueryError,
  } = useParentsListQuery();

  useEffect(() => {
    if (student) {
      setValue("name", student.name)
      setValue("birthdate", formatDateInputValue(student.birthdate))
      setValue("cpf", student.cpf)
      setValue("contact", student.contact)
      setValue("email", student.email)
      setValue("school", student.school)
      setValue("address.street", student.address.street)
      setValue("address.number", student.address.number)
      setValue("address.complement", student.address.complement ?? "")
      setValue("address.district", student.address.district)
      setValue("address.city", student.address.city)
      setValue("address.state", student.address.state)
      setValue("address.zip", student.address.zip)

      if (!selectedParentId && student.parent) {
        setSelectedParentId(student.parent.id)
      }
    }
  }, [student, setValue])

  useEffect(() => {
    if (selectedParentId) {
      const selectedParent = parentsList?.find((item: { id: string }) => item.id === selectedParentId)
      if (selectedParent) {
        setValue("parent.name", selectedParent.name)
        setValue("parent.email", selectedParent.email)
        setValue("parent.contact", selectedParent.contact)
        setValue("parent.cpf", selectedParent.cpf)
      }
    }
  }, [selectedParentId, parentsList, setValue])

  const studentEventsQuery = useQuery({
    queryKey: [...queryKeys.events, "student", studentId],
    queryFn: () =>
      eventsApi.listByStudent(studentId),
    enabled: Boolean(id),
  })

  const { mutate: updateStudent, isPending: isUpdating, error: updateError } = useUpdateStudent(studentId)
  const { mutate: deleteStudent, isPending: isDeleting, error: deleteError } = useDeleteStudent()

  const onSubmit = (data: StudentFormInput) => {
    updateStudent(data)
  }

  const handleDeleteStudent = () => {
    if (studentEventsQuery.isLoading) {
      globalThis.alert("Ainda estamos verificando se este aluno possui eventos vinculados. Tente novamente em instantes.")
      return
    }

    if (studentEventsQuery.isError) {
      globalThis.alert("Não foi possível verificar se existem eventos vinculados a este aluno. Tente novamente.")
      return
    }

    if ((studentEventsQuery.data?.page.totalElements ?? 0) > 0) {
      globalThis.alert("Este aluno possui eventos vinculados e não pode ser excluído. Arquive o aluno em vez de excluí-lo.")
      return
    }

    const confirmed = globalThis.confirm("Tem certeza que deseja excluir este aluno? Esta ação não pode ser desfeita.")

    if (!confirmed) {
      return
    }

    deleteStudent(studentId)
  }

  const isMutationPending = isUpdating || isDeleting
  const submitError = updateError || deleteError

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do aluno não informado." />
      </div>
    )
  }

  if (isStudentLoading) {
    return <PageLoading message="Carregando aluno para edição..." />
  }

  if (isStudentError || studentEventsQuery.isError || !student) {
    const error = studentError ?? studentEventsQuery.error
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(error)}
          onAction={() => Promise.all([refetchStudent(), studentEventsQuery.refetch()])}
        />
      </div>
    )
  }

  //TODO Tem que ver como vai funcionar a deleção de aluno e se vai ter o CASCADE
  const studentHasLinkedEvents = (studentEventsQuery.data?.page.totalElements ?? 0) > 0

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
      <SectionCard title="Responsável" description="Selecione um responsável já cadastrado no sistema.">
        <div className={styles.formGrid}>
          <FormField
            className={`${styles.field} ${styles.span2}`}
            label="Responsável"
            htmlFor="parentId"
            error={errors.parent?.name?.message ? "Selecione um responsável" : undefined}
          >
            <div className="flex flex-col gap-2">
              <select
                id="parentId"
                className="app-select"
                value={selectedParentId}
                onChange={(event) => setSelectedParentId(event.target.value)}
                disabled={isparentsListLoading}
              >
                <option value="">
                  {isparentsListLoading ? "Carregando responsáveis..." : "Selecione um responsável"}
                </option>
                {parentsList?.map((parent: any) => (
                  <option key={parent.id} value={parent.id}>
                    {parent.name} ({parent.cpf})
                  </option>
                ))}
              </select>

              {parentsListQueryError && (
                <p className="text-xs text-error">
                  {getFriendlyErrorMessage(parentsListQueryError)}
                </p>
              )}

              <p className="text-xs text-muted-foreground mt-1">
                Não encontrou o responsável? <ButtonLink to="/parents/new" variant="ghost" size="sm" className="h-auto p-0 underline">Cadastre um novo aqui</ButtonLink>
              </p>
            </div>
          </FormField>
        </div>
      </SectionCard>

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


        {submitError ? <div className="alert alert-error text-sm">{getFriendlyErrorMessage(submitError)}</div> : null}
        {studentHasLinkedEvents ? (
          <Badge variant="warning">
            Este aluno possui eventos vinculados e não pode ser excluído. Use o arquivamento para desativar o cadastro ou exclua todos os eventos relacionados a esse aluno.
          </Badge>
        ) : null}

        <div className={styles.actions}>
          <Button
            type="button"
            onClick={handleDeleteStudent}
            disabled={isMutationPending}
            variant="danger"
            className="sm:mr-auto"
          >
            {/* TODO: tem alguma forma mais limpa de usar os icones? */}
            <Trash2 className="h-4 w-4" />
            Excluir aluno
          </Button>
          <ButtonLink to={`/students/${studentId}`} variant="outline">
            Cancelar
          </ButtonLink>
          <Button type="submit" disabled={isMutationPending} variant="primary">
            {isUpdating ? "Salvando..." : "Salvar alterações"}
          </Button>
        </div>
      </form>
    </div>
  )
}
