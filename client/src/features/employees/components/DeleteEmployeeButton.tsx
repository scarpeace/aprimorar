import { useState } from "react"
import { useQuery } from "@tanstack/react-query"
import { Button } from "@/components/ui/button"
import { Trash2 } from "lucide-react"
import { useDeleteEmployee } from "../hooks/use-employees"
import { eventsApi } from "@/services/api"
import { queryKeys } from "@/lib/query/queryKeys"
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal"

export const DeleteEmployeeButton = ({ employeeId }: { employeeId: string }) => {
  const [isOpen, setIsOpen] = useState(false)
  const { mutate: deleteEmployee, isPending: isDeleting } = useDeleteEmployee()

  const { data: eventsData, isLoading: isEventsLoading } = useQuery({
    queryKey: [...queryKeys.events.byEmployee(employeeId), 0, 1],
    queryFn: () => eventsApi.listByEmployee(employeeId, 0, 1),
    enabled: isOpen, // Only fetch when the user clicks to delete
  })

  const handleOpenClick = () => {
    setIsOpen(true)
  }

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false)
    }
  }

  const handleConfirmDelete = () => {
    deleteEmployee(employeeId, {
      onSettled: () => {
        setIsOpen(false)
      }
    })
  }

  const eventsCount = eventsData?.page.totalElements ?? 0

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={isDeleting}
        variant="danger"
        className="sm:mr-auto"
      >
        <Trash2 className="h-4 w-4" />
        {isDeleting ? "Excluindo..." : "Excluir"}
      </Button>

      <DeleteConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Excluir Colaborador"
        isPending={isDeleting}
        isLoadingEvents={isEventsLoading}
        eventsCount={eventsCount}
        itemName="colaborador"
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Ao excluí-lo, seu histórico pessoal será apagado, mas <strong>todos os seus eventos e atendimentos serão transferidos automaticamente para um perfil de "Colaborador Removido"</strong> para manter a consistência financeira e o histórico.
          </div>
        }
      />
    </>
  )
}