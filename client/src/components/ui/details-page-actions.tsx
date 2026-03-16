import { Button, ButtonLink } from "./button"

type DetailsPageActionsProps = {
  data?: Record<string, unknown> | null
  editTo: string
  isArchivePending?: boolean
  isDeletePending: boolean
  handleArchive?: () => void
  handleDelete: () => void
}

export function DetailsPageActions(props: Readonly<DetailsPageActionsProps>) {
  const { data, editTo, handleArchive, handleDelete, isArchivePending, isDeletePending } = props

  const isArchived = Boolean(data && "archivedAt" in data && data.archivedAt)

  return (
    <div className="flex flex-col gap-2 sm:flex-row">
      {handleArchive && (
        <Button
          type="button"
          onClick={() => handleArchive()}
          disabled={isArchivePending}
          variant={isArchived ? "warning" : "outlineWarning"}
          size="sm"
        >
          {isArchived ? "Desarquivar" : "Arquivar"}
        </Button>
      )}

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