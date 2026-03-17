import { ButtonLink } from "@/components/ui/button"
import { Edit } from "lucide-react"

export const EditEmployeeButton = ({ employeeId }: { employeeId: string }) => {

    return (
        <ButtonLink to={`/employees/edit/${employeeId}`} variant="primary">
            <Edit className="h-4 w-4" />
            Editar colaborador
        </ButtonLink>
    )
}