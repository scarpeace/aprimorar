import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { zodResolver } from "@hookform/resolvers/zod"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate, useParams } from "react-router-dom"
import { useHookFormMask } from "use-mask-input"
import { Trash2 } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Button, ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { FormField } from "@/components/ui/form-field"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import styles from "@/features/employees/EmployeeCreatePage.module.css"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { queryKeys } from "@/lib/query/queryKeys"
import { employeeFormSchema, type EmployeeFormInput } from "@/lib/schemas"
import { formatDateInputValue } from "@/lib/shared/formatter"
import { employeesApi, eventsApi, getFriendlyErrorMessage } from "@/services/api"

const DELETE_BLOCKING_EVENTS_PARAMS = { page: 0, size: 1, sortBy: "startDate" }

function createEmptyEmployeeValues(): EmployeeFormInput {
  return {
    name: "",
    birthdate: "",
    pix: "",
    contact: "",
    cpf: "",
    email: "",
    duty: "TEACHER",
  }
}

export function EmployeeEditPage() {
  const { id } = useParams<{ id: string }>()
  const employeeId = id ?? ""
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm<EmployeeFormInput>({
    resolver: zodResolver(employeeFormSchema),
    defaultValues: createEmptyEmployeeValues(),
  })

  const registerWithMask = useHookFormMask(register)

  const employeeQuery = useQuery({
    queryKey: queryKeys.employees.editDetail(employeeId),
    queryFn: () => employeesApi.getByIdForEdit(employeeId),
    enabled: Boolean(id),
  })

  const employeeEventsQuery = useQuery({
    queryKey: queryKeys.events.byEmployee(employeeId, DELETE_BLOCKING_EVENTS_PARAMS),
    queryFn: () =>
      eventsApi.listByEmployee(
        employeeId,
        DELETE_BLOCKING_EVENTS_PARAMS.page,
        DELETE_BLOCKING_EVENTS_PARAMS.size,
        DELETE_BLOCKING_EVENTS_PARAMS.sortBy
      ),
    enabled: Boolean(id),
  })

  useEffect(() => {
    if (!employeeQuery.data) {
      return
    }

    reset({
      name: employeeQuery.data.name,
      birthdate: formatDateInputValue(employeeQuery.data.birthdate),
      pix: employeeQuery.data.pix,
      contact: employeeQuery.data.contact,
      cpf: employeeQuery.data.cpf,
      email: employeeQuery.data.email,
      duty: employeeQuery.data.duty,
    })

    // Campos com máscara podem não ser reidratados corretamente só com reset.
    setValue("contact", employeeQuery.data.contact)
    setValue("cpf", employeeQuery.data.cpf)
  }, [employeeQuery.data, reset, setValue])

  const updateEmployeeMutation = useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.update(employeeId, data),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async (updatedEmployee) => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.detail(employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.editDetail(employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.createOptions() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate(`/employees/${updatedEmployee.id}`)
    },
    onError: (error) => {
      console.error("Falha ao atualizar colaborador:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const deleteEmployeeMutation = useMutation({
    mutationFn: () => employeesApi.delete(employeeId),
    onMutate: () => {
      setSubmitError(null)
    },
    onSuccess: async () => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.lists() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.detail(employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.employees.editDetail(employeeId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events.createOptions() }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard.summary() }),
      ])

      navigate("/employees")
    },
    onError: (error) => {
      console.error("Falha ao excluir colaborador:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    },
  })

  const onSubmit = (data: EmployeeFormInput) => {
    updateEmployeeMutation.mutate(data)
  }

  const handleDeleteEmployee = () => {
    if (employeeEventsQuery.isLoading) {
      window.alert("Ainda estamos verificando se este colaborador possui eventos vinculados. Tente novamente em instantes.")
      return
    }

    if (employeeEventsQuery.isError) {
      window.alert("Não foi possível verificar se existem eventos vinculados a este colaborador. Tente novamente.")
      return
    }

    if ((employeeEventsQuery.data?.page.totalElements ?? 0) > 0) {
      window.alert("Este colaborador possui eventos vinculados e não pode ser excluído. Arquive o colaborador em vez de excluí-lo.")
      return
    }

    const confirmed = window.confirm("Tem certeza que deseja excluir este colaborador? Esta ação não pode ser desfeita.")

    if (!confirmed) {
      return
    }

    deleteEmployeeMutation.mutate()
  }

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do colaborador não informado." />
      </div>
    )
  }

  if (employeeQuery.isLoading) {
    return <PageLoading message="Carregando colaborador para edição..." />
  }

  if (employeeQuery.isError || employeeEventsQuery.isError || !employeeQuery.data) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(employeeQuery.error ?? employeeEventsQuery.error)}
          onAction={() => Promise.all([employeeQuery.refetch(), employeeEventsQuery.refetch()])}
        />
      </div>
    )
  }

  const employeeHasLinkedEvents = (employeeEventsQuery.data?.page.totalElements ?? 0) > 0

  return (
    <div className={styles.page}>
      <PageHeader
        title="Editar colaborador"
        description="Atualize os dados do colaborador."
        action={
          <ButtonLink to={`/employees/${employeeId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <SectionCard title="Dados do colaborador" description="Atualize as informações de cadastro e contato.">
        <form className={styles.form} onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className={styles.formGrid}>
            <FormField className={styles.field} label="Nome completo" htmlFor="name" error={errors.name?.message}>
              <input className="app-input" id="name" placeholder="Ex: Maria Silva" {...register("name")} />
            </FormField>

            <FormField className={styles.field} label="Data de nascimento" htmlFor="birthdate" error={errors.birthdate?.message}>
              <input className="app-input" id="birthdate" type="date" {...register("birthdate")} />
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

            <FormField className={styles.field} label="Chave PIX" htmlFor="pix" error={errors.pix?.message}>
              <input
                className="app-input"
                id="pix"
                placeholder="cpf/email/telefone/chave aleatória"
                {...register("pix")}
              />
            </FormField>

            <FormField className={styles.field} label="Função" htmlFor="duty" error={errors.duty?.message}>
              <select id="duty" className="app-select" {...register("duty")}>
                <option value="TEACHER">{dutyLabels.TEACHER}</option>
                <option value="ADM">{dutyLabels.ADM}</option>
                <option value="THERAPIST">{dutyLabels.THERAPIST}</option>
                <option value="MENTOR">{dutyLabels.MENTOR}</option>
              </select>
            </FormField>
          </div>

          {submitError ? <div className="alert alert-error text-sm">{submitError}</div> : null}
          {employeeHasLinkedEvents ? (
            <Badge variant="warning">
              Este colaborador possui eventos vinculados e não pode ser excluído. Use o arquivamento para desativar o cadastro ou exclua todos os eventos vinculados a este colaborador.
            </Badge>
          ) : null}

          <div className={styles.actions}>
            <Button
              type="button"
              onClick={handleDeleteEmployee}
              disabled={updateEmployeeMutation.isPending || deleteEmployeeMutation.isPending}
              variant="danger"
              className="sm:mr-auto"
            >
              <Trash2 className="h-4 w-4" />
              Excluir colaborador
            </Button>
            <ButtonLink to={`/employees/${employeeId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={updateEmployeeMutation.isPending || deleteEmployeeMutation.isPending} variant="primary">
              {updateEmployeeMutation.isPending ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </div>
  )
}
