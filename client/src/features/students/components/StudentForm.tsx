import { zodResolver } from "@hookform/resolvers/zod"
import { ActionErrorBanner } from "@/components/ui/action-error-banner"
import { Button } from "@/components/ui/button"
import { FormActions } from "@/components/ui/form-actions"
import { FormField } from "@/components/ui/form-field"
import { FormPageShell } from "@/components/ui/form-page-shell"
import { Input } from "@/components/ui/input"
import { SelectInput } from "@/components/ui/select-input"
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
    <FormPageShell
      title={title}
      description={description}
      backTo="/students"
      backLabel="← Voltar para alunos"
      cardTitle="Dados do aluno"
      cardDescription={cardDescription}
    >
      <form className={styles.form} onSubmit={handleSubmit((data) => onSubmit(data, parentMode))}>
            <div className={styles.sectionTitle}>Aluno</div>
            <div className={styles.formGrid}>
              <FormField label="Nome completo" htmlFor="name" error={errors.name?.message}>
                <Input id="name" placeholder="Ex: João Pedro" {...register("name")} />
              </FormField>

              <FormField label="Data de nascimento" htmlFor="birthdate" error={errors.birthdate?.message}>
                <Input id="birthdate" type="date" {...register("birthdate")} />
              </FormField>

              <FormField label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
                <Input id="cpf" placeholder="000.000.000-00" {...registerWithMask("cpf", "999.999.999-99")} />
              </FormField>

              <FormField label="Contato" htmlFor="contact" error={errors.contact?.message}>
                <Input id="contact" placeholder="(11) 99999-9999" {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])} />
              </FormField>

              <FormField label="Email" htmlFor="email" error={errors.email?.message}>
                <Input id="email" type="email" placeholder="exemplo@dominio.com" {...register("email")} />
              </FormField>

              <FormField label="Escola" htmlFor="school" error={errors.school?.message}>
                <Input id="school" placeholder="Ex: Escola Estadual X" {...register("school")} />
              </FormField>
            </div>

            <div className={styles.divider} />
            <div className={styles.sectionTitle}>Endereço</div>
            <div className={styles.formGrid}>
              <FormField label="Rua" htmlFor="address.street" error={errors.address?.street?.message} className={styles.span2}>
                <Input id="address.street" placeholder="Ex: Rua das Flores" {...register("address.street")} />
              </FormField>

              <FormField label="Número" htmlFor="address.number" error={errors.address?.number?.message}>
                <Input id="address.number" placeholder="Ex: 123" {...register("address.number")} />
              </FormField>

              <FormField label="Complemento (opcional)" htmlFor="address.complement" error={errors.address?.complement?.message}>
                <Input id="address.complement" placeholder="Apto, bloco, etc" {...register("address.complement")} />
              </FormField>

              <FormField label="Bairro" htmlFor="address.district" error={errors.address?.district?.message}>
                <Input id="address.district" placeholder="Ex: Centro" {...register("address.district")} />
              </FormField>

              <FormField label="Cidade" htmlFor="address.city" error={errors.address?.city?.message}>
                <Input id="address.city" placeholder="Ex: São Paulo" {...register("address.city")} />
              </FormField>

              <FormField label="Estado" htmlFor="address.state" error={errors.address?.state?.message}>
                <Input id="address.state" placeholder="Ex: SP" {...register("address.state")} />
              </FormField>

              <FormField label="CEP" htmlFor="address.zip" error={errors.address?.zip?.message}>
                <Input id="address.zip" placeholder="00000-000" {...registerWithMask("address.zip", "99999-999")} />
              </FormField>
            </div>

            <div className={styles.divider} />
            <div className={styles.sectionTitle}>Responsável</div>
            <ActionErrorBanner message={parentsError} />

            <div className={styles.formGrid}>
              {parents.length > 0 ? (
                <FormField label="Tipo de responsável" htmlFor="parentMode" help={parentsLoading ? "Carregando lista..." : undefined} className={styles.span2}>
                  <SelectInput id="parentMode" value={parentMode} onChange={(event) => setParentMode(event.target.value as StudentParentMode)} disabled={parentsLoading}>
                    <option value="new">Novo responsável</option>
                    <option value="existing">Responsável existente</option>
                  </SelectInput>
                </FormField>
              ) : (
                <div className={styles.span2}>
                  <p className={styles.help}>Nenhum responsável ativo encontrado. Cadastre um novo responsável abaixo.</p>
                  {!parentsLoading ? (
                    <Button type="button" variant="outline" onClick={loadParents}>
                      Recarregar responsáveis
                    </Button>
                  ) : null}
                </div>
              )}

              {parentMode === "existing" && parents.length > 0 ? (
                <FormField label="Responsável" htmlFor="parentId" error={errors.parentId?.message} help={selectedParentName ? `Selecionado: ${selectedParentName}` : undefined} className={styles.span2}>
                  <SelectInput
                    id="parentId"
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
                  </SelectInput>
                </FormField>
              ) : null}

              {parentMode === "new" ? (
                <>
                  <FormField label="Nome do responsável" htmlFor="parent.name" error={errors.parent?.name?.message}>
                    <Input id="parent.name" placeholder="Ex: Ana Souza" {...register("parent.name")} />
                  </FormField>

                  <FormField label="Email do responsável" htmlFor="parent.email" error={errors.parent?.email?.message}>
                    <Input id="parent.email" type="email" placeholder="exemplo@dominio.com" {...register("parent.email")} />
                  </FormField>

                  <FormField label="Contato do responsável" htmlFor="parent.contact" error={errors.parent?.contact?.message}>
                    <Input id="parent.contact" placeholder="(11) 99999-9999" {...registerWithMask("parent.contact", ["(99) 9999-9999", "(99) 99999-9999"])} />
                  </FormField>

                  <FormField label="CPF do responsável" htmlFor="parent.cpf" error={errors.parent?.cpf?.message}>
                    <Input id="parent.cpf" placeholder="000.000.000-00" {...registerWithMask("parent.cpf", "999.999.999-99")} />
                  </FormField>
                </>
              ) : null}
            </div>

            <ActionErrorBanner message={submitError} />

            <FormActions>
              <Button asChild type="button" variant="outline">
                <Link to="/students">Cancelar</Link>
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Salvando..." : submitLabel}
              </Button>
            </FormActions>
      </form>
    </FormPageShell>
  )
}
