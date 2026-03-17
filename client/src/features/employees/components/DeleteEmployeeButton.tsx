import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useDeleteEmployee } from "../hooks/use-employees";

export const DeleteEmployeeButton = ({ employeeId }: { employeeId: string }) => {

  const { mutate: deleteEmployee, isPending: isDeleting } = useDeleteEmployee();

  const handleEmployeeDelete = () => {
    if (globalThis.confirm("Tem certeza que deseja excluir este colaborador? Esta ação não pode ser desfeita.")) {
      deleteEmployee(employeeId)
    }
  }

  return (
    <Button
      type="button"
      onClick={handleEmployeeDelete}
      disabled={isDeleting}
      variant="danger"
      className="sm:mr-auto"
    >
      <Trash2 className="h-4 w-4" />
      {isDeleting ? "Excluindo..." : "Excluir colaborador"}
    </Button>
  )
}