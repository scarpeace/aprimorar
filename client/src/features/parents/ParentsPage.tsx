import { useState } from "react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { ButtonLink } from "@/components/ui/button"
import styles from "@/features/parents/ParentsPage.module.css"
import { useParentsQuery } from "./hooks/use-parents"
import { getFriendlyErrorMessage } from "@/services/api"

export function ParentsPage() {
  const [hideArchived, setHideArchived] = useState(true)

  const {
    data: parentsResponse,
    isLoading,
    isError,
    error,
    refetch,
  } = useParentsQuery()

  const parentList = parentsResponse?.content ?? []

  const visibleParents = hideArchived ? parentList.filter((parent) => !parent.archivedAt) : parentList

  if (isLoading) {
    return <PageLoading message="Carregando responsáveis..." />
  }

  if (isError) {
    return (
      <div className={styles.page}>
        <PageHeader title="Responsáveis" description="Gerencie pais responsáveis." />
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie pais e responsáveis."
        title="Responsáveis"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar responsável por nome, função ou email"
            ariaLabel="Buscar responsável"
          />
          <label className="app-text-muted flex items-center gap-2 whitespace-nowrap text-sm">
            <input
              className="checkbox checkbox-sm"
              type="checkbox"
              checked={hideArchived}
              onChange={(event) => setHideArchived(event.target.checked)}
            />
            Ocultar arquivados
          </label>
          <ButtonLink className="sm:ml-auto" to="/parents/new" variant="success">
            Novo responsável
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th hidden lg:table-cell">Email</th>
                <th className="app-th hidden lg:table-cell">CPF</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {visibleParents.map((parent) => (
                <tr className="transition-colors hover:bg-base-200/70" key={parent.id}>
                  <td>{parent.name}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{parent.email}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{parent.cpf}</td>
                  <td>
                    <ButtonLink size="sm" to={`/parents/${parent.id}`} variant="outline">
                      Detalhes
                    </ButtonLink>
                  </td>
                  <td className="text-center">
                    <span className={`badge ${parent.archivedAt ? "badge-warning" : "badge-success"}`}>
                      {parent.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {visibleParents.length === 0 ? (
        <EmptyCard
          title="Nenhum responsável cadastrado"
          description="Quando você cadastrar o primeiro responsável, ele aparecerá na tabela acima."
          action={
            <ButtonLink to="/parents/new" variant="secondary">
              Novo responsável
            </ButtonLink>
          }
        />
      ) : null}
    </div>
  )
}
