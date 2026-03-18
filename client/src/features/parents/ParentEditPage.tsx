import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Save } from "lucide-react"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "./ParentCreatePage.module.css"
import { parentFormSchema, type ParentFormInput } from "@/lib/schemas"
import { getFriendlyErrorMessage } from "@/services/api"
import { useParentDetailQuery, useUpdateParent } from "./hooks/use-parents"
import { DeleteParentButton } from "./components/DeleteParentButton"


export function ParentEditPage() {

  const { id } = useParams<{ id: string }>()
  const parentId = id ?? ""

  const {
    isError: isParentError,
    error: parentError,
    isLoading: isParentLoading,
    data: parentData,
    refetch: refetchParent
  } = useParentDetailQuery(parentId)


  const {
    mutate: updateParent,
    isPending: isUpdating
  } = useUpdateParent(parentId)

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm<ParentFormInput>({
    resolver: zodResolver(parentFormSchema),
    values: {
      name: parentData?.name ?? "",
      email: parentData?.email ?? "",
      contact: parentData?.contact ?? "",
      cpf: parentData?.cpf ?? "",
    },
  })
  const registerWithMask = useHookFormMask(register)

  const onSubmit = (data: ParentFormInput) => {
    updateParent(data)
  }


  if (isParentLoading) {
    return <PageLoading message="Carregando responsável para edição..." />
  }

  if (isParentError || !parentData) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(parentError)}
          onAction={refetchParent}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar responsável"
        description="Atualize os dados do responsável."
        action={
          <ButtonLink to="/parents" variant="outline">
            Voltar para responsáveis
          </ButtonLink>
        }
      />

      <SectionCard title="Dados do responsável" description="Atualize as informações de cadastro e contato.">
        <form className={styles.form} onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className={styles.formGrid}>
            <FormField className={styles.field} label="Nome completo" htmlFor="name" error={errors.name?.message}>
              <input className="app-input" id="name" placeholder="Ex: Maria Silva" {...register("name")} />
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

            <FormField className={styles.field} label="Contato" htmlFor="contact" error={errors.contact?.message}>
              <input
                className="app-input"
                id="contact"
                placeholder="(11) 99999-9999"
                {...registerWithMask("contact", ["(99) 9999-9999", "(99) 99999-9999"])}
              />
            </FormField>

            <FormField className={styles.field} label="CPF" htmlFor="cpf" error={errors.cpf?.message}>
              <input
                className="app-input"
                id="cpf"
                placeholder="000.000.000-00"
                {...registerWithMask("cpf", "999.999.999-99")}
              />
            </FormField>
          </div>

          <div className={styles.actions}>
            <DeleteParentButton parentId={parentId} />
            <ButtonLink to={`/parents/${parentId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isUpdating} variant="success">
              <Save />
              {isUpdating ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  )
}
