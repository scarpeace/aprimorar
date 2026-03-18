import { useState } from "react"
import { Button } from "@/components/ui/button"
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react"
import { useArchiveParent, useUnarchiveParent } from "../hooks/use-parents"
import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert"

export const ArchiveParentButton = ({
    parentId,
    isArchived,
}: {
    parentId: string;
    isArchived: boolean | null;
}) => {
    const [showConfirm, setShowConfirm] = useState(false)
    const { mutate: unarchiveParent, isPending: isUnarchiving } = useUnarchiveParent()
    const { mutate: archiveParent, isPending: isArchiving } = useArchiveParent()

    const isLoading = isArchiving || isUnarchiving

    const handleArchiveConfirm = () => {
        archiveParent(parentId, {
            onSuccess: () => setShowConfirm(false)
        })
    }

    const handleUnarchive = () => {
        unarchiveParent(parentId)
    }

    if (isLoading && !showConfirm) {
        return (
            <Button type="button" disabled variant="outline" className="sm:mr-auto">
                <Loader2Icon className="h-4 w-4 animate-spin" />
                Processando...
            </Button>
        )
    }

    if (isArchived) {
        return (
            <Button
                type="button"
                onClick={handleUnarchive}
                variant="outline"
                className="sm:mr-auto"
            >
                <ArchiveRestoreIcon className="h-4 w-4" />
                Desarquivar
            </Button>
        )
    }

    if (showConfirm) {
        return (
            <InlineConfirmAlert
                variant="warning"
                message="Deseja realmente arquivar este responsável?"
                confirmText="Sim, arquivar"
                cancelText="Cancelar"
                onConfirm={handleArchiveConfirm}
                onCancel={() => setShowConfirm(false)}
                className="sm:mr-auto"
                isLoading={isArchiving}
            />
        )
    }

    return (
        <Button
            type="button"
            onClick={() => setShowConfirm(true)}
            variant="warning"
            className="sm:mr-auto"
        >
            <ArchiveIcon className="h-4 w-4" />
            Arquivar
        </Button>
    )
}
