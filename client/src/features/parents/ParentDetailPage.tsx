import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import type { ReactNode } from "react"
import { UserCog } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
import { Button, ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import styles from "./ParentDetailPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { getFriendlyErrorMessage, parentsApi, studentsApi } from "@/services/api"

import { StudentsTable } from "@/features/students/components/StudentsTable"

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const parentId = id ?? ""
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const {
    isError: isParentError,
    error: parentError,
    isLoading: isParentLoading,
    data: parentData,
    refetch: refetchParent
  } = useQuery({
    queryKey: queryKeys.parentDetail(parentId),
    queryFn: () => parentsApi.getById(parentId),
    enabled: Boolean(id),
  })

  const {
    isError: isParentStudentsError,
    error: parentStudentsError,
    isLoading: isParentStudentsLoading,
    data: parentStudentsData,
    refetch: refetchParentStudents
  } = useQuery({
    queryKey: queryKeys.studentsByParent(parentId),
    queryFn: () => studentsApi.listByParent(parentId),
    enabled: Boolean(id),
  })

  const {
    mutate: archiveParentMutation,
    isError: isArchiveParentError,
    error: archiveParentError,
    isPending: isArchiveParentPending } = useMutation({
      mutationFn: () =>
        parentData?.archivedAt ? parentsApi.unarchive(parentId) : parentsApi.archive(parentId),
      onSuccess: async () => {
        await Promise.all([
          queryClient.invalidateQueries({ queryKey: queryKeys.parentDetail(parentId) }),
          queryClient.invalidateQueries({ queryKey: queryKeys.students }),
          queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
        ])
      },
    })

  const {
    mutate: deleteParentMutation,
    isError: isDeleteParentError,
    error: deleteParentError,
    isPending: isDeleteParentPending } = useMutation({
      mutationFn: () =>
        parentsApi.delete(parentId),
      onSuccess: async () => {
        await Promise.all([
          queryClient.invalidateQueries({ queryKey: queryKeys.parentDetail(parentId) }),
          queryClient.invalidateQueries({ queryKey: queryKeys.students }),
          queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
        ])
        navigate("/parents")
      },
    })

  const refetchAll = async () => {
    refetchParent()
    refetchParentStudents()
  }

  const handleParentDelete = () => {
    const confirmed = globalThis.confirm("Tem certeza que deseja excluir este responsável? Esta ação não pode ser desfeita.")
    if (confirmed) {
      deleteParentMutation()
    }
  }

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID de responsável inválida." />
      </div>
    )
  }

  if (isParentLoading) {
    return <PageLoading message="Carregando responsável..." />
  }

  if (isParentError) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(parentError)} onAction={refetchAll} />
      </div>
    )
  }

  if (!parentData) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Responsável não encontrado" description="Não encontramos os dados deste responsável." />
      </div>
    )
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parentData.name },
    { label: "E-mail", value: parentData.email },
    { label: "Contato", value: parentData.contact },
    { label: "CPF", value: parentData.cpf },
    { label: "Status", value: parentData.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Criado em", value: parentData.createdAt },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <ButtonLink to="/parents" variant="outline">
            Voltar para responsáveis
          </ButtonLink>
        }
        description="Veja e gerencie as informações do responsável"
        leading={
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-success/15">
            <UserCog className="h-6 w-6 text-success" />
          </div>
        }
        title="Detalhes do responsável"
        titleClassName="text-2xl font-bold app-text"
      />

      {/* TODO Melhorar isso aqui */}
      <SectionCard
        title="Resumo do responsável"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <div className="flex flex-col gap-2 sm:flex-row">
            <ButtonLink size="sm" to={`/parents/edit/${parentData.id}`} variant="primary">
              Editar responsável
            </ButtonLink>

            <Button
              type="button"
              onClick={() => archiveParentMutation()}
              disabled={isArchiveParentPending}
              variant={parentData.archivedAt ? "success" : "warning"}
              size="sm"
            >
              {parentData.archivedAt ? "Ativar responsável" : "Arquivar responsável"}
            </Button>

            <Button
              type="button"
              onClick={handleParentDelete}
              disabled={isDeleteParentPending}
              variant="error"
              size="sm"
            >
              Excluir responsável
            </Button>
          </div>
        }
      >
        {(isArchiveParentError || isDeleteParentError) && (
          <div className="alert alert-error text-sm">
            {getFriendlyErrorMessage(archiveParentError || deleteParentError)}
          </div>
        )}

        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>

      <SectionCard
        title="Alunos vinculados"
        description={`Alunos registrados sob a responsabilidade de ${parentData.name}.`}
      >
        {isParentStudentsLoading && <PageLoading message="Carregando alunos..." />}

        {isParentStudentsError && (
          <div className="alert alert-error text-sm mb-4">
            {getFriendlyErrorMessage(parentStudentsError)}
          </div>
        )}

        {parentStudentsData && (
          <StudentsTable variant="parentPage" students={parentStudentsData} />
        )}
      </SectionCard>
    </div>
  )
}
