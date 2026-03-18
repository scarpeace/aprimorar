import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"

import { Alert } from "@/components/ui/alert"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/students/StudentCreatePage.module.css"
import { studentInputSchema, type ParentResponse, type StudentFormInput } from "@/lib/schemas"
import { BRAZILIAN_STATES } from "@/lib/shared/enums/brazilianStates"
import { formatDateInputValue } from "@/lib/shared/formatter"
import { getFriendlyErrorMessage } from "@/services/api"
import { useStudentDetailQuery, useUpdateStudent } from "./hooks/use-students"
import { useParentsListQuery } from "../parents/hooks/use-parents"
import { DeleteStudentButton } from "./components/DeleteStudentButton"
import { ParentSelectDropdown } from "../parents/components/ParentSelectDropdown"

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""

  const { data: student, isLoading: isStudentLoading, isError: isStudentError, error: studentError, refetch: refetchStudent } = useStudentDetailQuery(studentId)

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<StudentFormInput>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? "",
      birthdate: formatDateInputValue(student?.birthdate ?? ""),
      cpf: student?.cpf ?? "",
      contact: student?.contact ?? "",
      email: student?.email ?? "",
      school: student?.school ?? "",
      address: {
        street: student?.address?.street ?? "",
        number: student?.address?.number ?? "",
        complement: student?.address?.complement ?? "",
        district: student?.address?.district ?? "",
        city: student?.address?.city ?? "",
        state: student?.address?.state ?? "",
        zip: student?.address?.zip ?? "",
      },
      parentId: student?.parent?.id ?? "Responsável não encontrado",
    }
  })
  const registerWithMask = useHookFormMask(register)

  const {
    data: parentsList,
    isLoading: isParentsListLoading,
    error: parentsListQueryError,
  } = useParentsListQuery()

  const { mutate: updateStudent, isPending: isUpdating, error: updateError } = useUpdateStudent(studentId)

  const onSubmit = (data: StudentFormInput) => {
    updateStudent(data)
  }

  const isMutationPending = isUpdating
  const submitError = updateError

  const selectedParentId = watch("parentId")

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

  if (isStudentError || !student) {
    const error = studentError
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(error)}
          onAction={() => refetchStudent()}
        />
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
      <SectionCard title="Responsável" description="Selecione um responsável já cadastrado no sistema.">
        <div className={styles.formGrid}>
          <FormField
            className={`${styles.field} ${styles.span2}`}
            label=""
            htmlFor="parentId"
            error={errors.parentId?.message}
          >
            {/* Registra o campo silenciosamente */}
            <input type="hidden" {...register("parentId")} />
            <ParentSelectDropdown
              value={selectedParentId}
              onChange={(id) => setValue("parentId", id, { shouldValidate: true, shouldDirty: true })}
              hasError={!!errors.parentId}
            />

            <p className="text-xs text-muted-foreground mt-1">
              Não encontrou o responsável? <ButtonLink to="/parents/new" variant="ghost" size="sm" className="h-auto p-0 underline">Cadastre um novo aqui</ButtonLink>
            </p>
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


          {submitError && (
            <Alert variant="error" className="text-sm">
              {getFriendlyErrorMessage(submitError)}
            </Alert>
          )}

          <div className={styles.actions}>
            <DeleteStudentButton studentId={studentId} />

            <ButtonLink to={`/students/${studentId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isMutationPending} variant="success">
              {isUpdating ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </SectionCard>

      </form>
    </div>
  )
}

