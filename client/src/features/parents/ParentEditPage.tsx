import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { useNavigate, useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Trash2 } from "lucide-react"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "./ParentCreatePage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { parentFormSchema, type ParentFormInput } from "@/lib/schemas"
import { parentsApi, getFriendlyErrorMessage } from "@/services/api"


export function ParentEditPage() {

  const { id } = useParams<{ id: string }>()
  const parentId = id ?? ""
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const {
    isError: isParentError,
    error: parentError,
    isLoading: isParentLoading,
    data: parentData,
    isFetched: isParentFetched,
    refetch: refetchParent
  } = useQuery({
    queryKey: queryKeys.parentDetail(parentId),
    queryFn: () => parentsApi.getById(parentId),
    enabled: Boolean(id),
  })

  const {
    mutate: updateParentMutation,
    isPending: isUpdateParentPending } = useMutation({
      mutationFn: (data: ParentFormInput) =>
        parentsApi.update(parentId, data),
      onSuccess: async () => {
        await Promise.all([
          queryClient.invalidateQueries({ queryKey: queryKeys.parentDetail(parentId) }),
          queryClient.invalidateQueries({ queryKey: queryKeys.students }),
          queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
        ])
        navigate(`/parents/${parentId}`)
      },
    })

  const {
    mutate: deleteParentMutation,
    isError: isDeleteParentError,
    isPending: isDeleteParentPending } = useMutation({
      mutationFn: () =>
        parentsApi.delete(parentId),
      onSuccess: async () => {
        await Promise.all([
          queryClient.invalidateQueries({ queryKey: queryKeys.parentDetail(parentId) }),
          queryClient.invalidateQueries({ queryKey: queryKeys.students }),
          queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
        ])
      },
    })


  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm<ParentFormInput>({
    resolver: zodResolver(parentFormSchema),
  })

  const registerWithMask = useHookFormMask(register)

  useEffect(() => {
    if (!parentData) {
      return
    }

    reset({
      name: parentData.name,
      contact: parentData.contact,
      cpf: parentData.cpf,
      email: parentData.email,
    })

    // Campos com máscara podem não ser hidratados corretamente.
    setValue("contact", parentData.contact)
    setValue("cpf", parentData.cpf)
  }, [parentData, reset, setValue])

  const onSubmit = (data: ParentFormInput) => {
    updateParentMutation(data)
  }

  const handleDeleteParent = () => {
    if (isDeleteParentPending) {
      window.alert("Ainda estamos verificando se este responsável possui eventos vinculados. Tente novamente em instantes.")
      return
    }

    if (isDeleteParentError) {
      window.alert("Não foi possível verificar se existem eventos vinculados a este responsável. Tente novamente.")
      return
    }

    //TODO implementar
    // if ((parentData.students.length ?? 0) > 0) {
    //   window.alert("Este responsável possui eventos vinculados e não pode ser excluído. Arquive o responsável em vez de excluí-lo.")
    //   return
    // }

    const confirmed = window.confirm("Tem certeza que deseja excluir este responsável? Esta ação não pode ser desfeita.")

    if (!confirmed) {
      return
    }

    deleteParentMutation()
  }

  if (!isParentFetched) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do responsável não informado ou corrompido" />
      </div>
    )
  }

  if (isParentLoading) {
    return <PageLoading message="Carregando responsável para edição..." />
  }

  if (isParentError || !parentData) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(parentError)}
          onAction={() => refetchParent()}
        />
      </div>
    )
  }

  //TODO implementar
  // const parentHasStudents = (parentData.students.length ?? 0) > 0

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


          {/*
          TODO: Implementar
          {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}
          {employeeHasLinkedEvents ? (
            <Badge variant="warning">
              Este responsável possui eventos vinculados e não pode ser excluído. Use o arquivamento para desativar o cadastro ou exclua todos os eventos vinculados a este responsável.
            </Badge>
          ) : null} */}

          <div className={styles.actions}>
            <Button
              type="button"
              onClick={handleDeleteParent}
              disabled={isUpdateParentPending || isDeleteParentPending}
              variant="danger"
              className="sm:mr-auto"
            >
              <Trash2 className="h-4 w-4" />
              Excluir responsável
            </Button>

            <ButtonLink to={`/parents/${parentId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isUpdateParentPending || isDeleteParentPending} variant="primary">
              {isUpdateParentPending ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  )
}
