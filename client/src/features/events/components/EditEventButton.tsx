import { ButtonLink } from "@/components/ui/button"
import { Edit } from "lucide-react"

export const EditEventButton = ({ eventId }: { eventId: string }) => {

    return (
        <ButtonLink to={`/events/edit/${eventId}`} variant="primary">
            <Edit className="h-4 w-4" />
            Editar
        </ButtonLink>
    )
}
