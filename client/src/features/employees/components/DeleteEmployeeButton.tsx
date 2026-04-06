import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { useGetEventsByEmployee } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";



export const DeleteEmployeeButton = ({employeeId}: {employeeId: string}) => {
  const [isOpen, setIsOpen] = useState(false);

  const {
    deleteEmployee: { mutate: deleteEmployee, isPending: isDeleting },
  } = useEmployeeMutations();

  const { data: eventsData, isLoading: isEventsLoading } =
    useGetEventsByEmployee(employeeId);

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteEmployee(
      { employeeId },
      {
        onSettled: () => {
          setIsOpen(false);
        },
      },
    );
  };

  const eventsCount = eventsData?.totalElements ?? 0;

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
        isItemPending={isDeleting}
        isItemLoading={isEventsLoading}
        itemDeleteCount={eventsCount}
        itemName="colaborador"
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
            <strong>
              todos os seus eventos e atendimentos serão transferidos
              automaticamente para um perfil de "Colaborador Removido"
            </strong>{" "}
            para manter a consistência financeira e o histórico.
          </div>
        }
      />
    </>
  );
};
