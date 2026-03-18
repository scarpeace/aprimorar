import { ButtonLink } from "@/components/ui/button"
import { Edit } from "lucide-react"

export const EditParentButton = ({ parentId }: { parentId: string }) => {

    return (
        <ButtonLink to={`/parents/edit/${parentId}`} variant="primary">
            <Edit className="h-4 w-4" />
            Editar
        </ButtonLink>
    )
}
