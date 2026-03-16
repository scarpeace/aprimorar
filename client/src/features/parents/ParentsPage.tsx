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
  const [currentPage, setCurrentPage] = useState(0)
  const pageSize = 10

  const {
    data: response,
    isLoading,
    isError,
    error,
    refetch,
  } = useParentsQuery(currentPage, pageSize)

  const parentList = response?.content ?? []
  const pageInfo = response?.page

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

  const totalElements = pageInfo?.totalElements ?? 0
  const totalPages = pageInfo?.totalPages ?? 0

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
              {parentList.map((parent) => (
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

      {/* PAGINAÇÃO */}
      {totalPages > 1 && (
        <div className="mt-4 flex items-center justify-between px-2">
          <p className="text-sm app-text-muted">
            Mostrando {parentList.length} de {totalElements} responsáveis
          </p>
          <div className="join">
            <button
              className="btn btn-sm join-item"
              disabled={currentPage === 0}
              onClick={() => setCurrentPage((p) => p - 1)}
            >
              Anterior
            </button>
            <button className="btn btn-sm join-item no-animation cursor-default">
              Página {currentPage + 1} de {totalPages}
            </button>
            <button
              className="btn btn-sm join-item"
              disabled={currentPage >= totalPages - 1}
              onClick={() => setCurrentPage((p) => p + 1)}
            >
              Próxima
            </button>
          </div>
        </div>
      )}

      {parentList.length === 0 ? (
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
