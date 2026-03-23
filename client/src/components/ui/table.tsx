import { ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { LoadingCard } from "@/components/ui/loading-card"
import { useState, useEffect, type ReactNode } from "react"
import { Pagination } from "@/components/ui/pagination"
import type { UseQueryResult } from "@tanstack/react-query"
import type { PageResponse } from "@/lib/shared/page-response"

export type ColumnDef<T> = {
    header: string
    accessor: (row: T) => ReactNode
}

type TableProps<T> = {
    variant?: "page" | "embedded"
    data?: PageResponse<T>
    columns: ColumnDef<T>[]
    isLoading: boolean
    error: Error | null
    ownerId?: string
    searchTerm?: string
    actionPrefix?: string
}

export function Table<T>({ variant = "page", data, columns, isLoading, error, ownerId, searchTerm, actionPrefix }: Readonly<TableProps<T>>) {
    const [currentPage, setCurrentPage] = useState(0)

    // Reset pagination when search changes
    useEffect(() => {
        setCurrentPage(0)
    }, [searchTerm])

    if (isLoading) {
        return <LoadingCard description="Carregando dados..." />
    }

    if (data?.page?.totalElements === 0) {
        return <EmptyCard description="Nenhum dado encontrado." title={"Nenhum dado encontrado."} />
    }

    if (error) {
        return <ErrorCard description={error.message} title="Erro ao carregar dados" />
    }

    return (
        <div className="">
            <table className="table table-zebra w-full">
                <thead className="bg-base-200/90">
                    <tr>
                        {columns.map((col, index) => (
                            <th key={index} className="app-th">{col.header}</th>
                        ))}
                        {actionPrefix && <th className="app-th">Ações</th>}
                    </tr>
                </thead>
                <tbody>
                    {/* TODO: Implementar a tabela */}
                    {/* Lista os itens (linhas da tabela) */}
                    {/* {data?.content?.map((rowItem, rowIndex) => (
                        <tr className="transition-colors hover:bg-base-200/70" key={rowIndex}> */}

                    {/* Para cada coluna, pede para extrair seu valor da linha */}
                    {/* {columns.map((col, colIndex) => (
                                <td key={colIndex} className="app-td">
                                    {col.accessor(rowItem)}
                                </td>
                            ))} */}
                    {/* {actionPrefix && (
                                <td>
                                    <ButtonLink size="sm" to={`${actionPrefix}/${rowItem.id}`} variant="outline">
                                        Detalhes
                                    </ButtonLink>
                                </td>
                            )}
                        </tr>
                    ))} */}
                </tbody>
            </table>
            <Pagination
                currentPage={currentPage}
                totalElements={data?.page?.totalElements ?? 0}
                totalPages={data?.page?.totalPages ?? 0}
                currentElementsCount={data?.content?.length ?? 0}
                itemName="eventos"
                onPageChange={setCurrentPage}
            />
        </div>
    )
}
