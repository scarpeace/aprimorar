import { ButtonLink } from "@/components/ui/button"
import { Edit } from "lucide-react"

export const EditStudentButton = ({ studentId }: { studentId: string }) => {

    return (
        <ButtonLink to={`/students/edit/${studentId}`} variant="primary">
            <Edit className="h-4 w-4" />
            Editar
        </ButtonLink>
    )
}
