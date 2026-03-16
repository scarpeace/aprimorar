import { useQuery } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useHookFormMask } from "use-mask-input"
import { Button, ButtonLink } from "@/components/ui/button"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/students/StudentCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentInputSchema, type StudentFormInput } from "@/lib/schemas"
import { BRAZILIAN_STATES } from "@/lib/shared/enums/brazilianStates"
import { getFriendlyErrorMessage, parentsApi } from "@/services/api"
import { useCreateStudent } from "./hooks/use-students"
import { useParentsListQuery } from "../parents/hooks/use-parents"

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
}

export function StudentCreatePage() {
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
    data: parentsList,
    isLoading: isparentsListLoading,
    error: parentsListQueryError,
  } = useParentsListQuery();

  const registerWithMask = useHookFormMask(register)

  useEffect(() => {
    if (selectedParentId) {
      const selectedParent = parentsList?.find((item: { id: string }) => item.id === selectedParentId)
      if (!selectedParent) {
        return
      }

      setValue("parent.name", selectedParent.name)
      setValue("parent.email", selectedParent.email)
      setValue("parent.contact", selectedParent.contact)
      setValue("parent.cpf", selectedParent.cpf)
    } else {
      // @ts-ignore - limpando o objeto parent para o zod ignorar ou falhar se obrigatório no backend
      setValue("parent", undefined)
    }
  }, [selectedParentId, parentsList, setValue])

  const { mutate: createStudent, isPending: isSubmitting, error: submitError } = useCreateStudent()

  const onSubmit = (data: StudentFormInput) => {
    createStudent(data)
  }

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


        {submitError ? <div className="alert alert-error text-sm">{getFriendlyErrorMessage(submitError)}</div> : null}

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
