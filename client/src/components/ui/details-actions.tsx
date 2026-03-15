import { Button, ButtonLink } from "./button";

export function DetailsActions(props: Readonly<{
    data: any
    editTo: string
    isArchivePending: boolean
    isDeletePending: boolean
    handleArchive: () => void
    handleDelete: () => void
}>) {
    const { data, editTo, handleArchive, handleDelete, isArchivePending, isDeletePending } = props

    return (
        <div className="flex flex-col gap-2 sm:flex-row">
            <Button
                type="button"
                onClick={() => handleArchive()}
                disabled={isArchivePending}
                variant={data.archivedAt ? "warning" : "outlineWarning"}
                size="sm"
            >
                {data.archivedAt ? "Desarquivar" : "Arquivar"}
            </Button>

            <ButtonLink size="sm" to={editTo} variant="primary">
                Editar
            </ButtonLink>

            <Button
                type="button"
                onClick={handleDelete}
                disabled={isDeletePending}
                variant="error"
                size="sm"
            >
                Excluir
            </Button>
        </div>
    )
}