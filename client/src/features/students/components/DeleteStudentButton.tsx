import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useDeleteStudent } from "@/features/students/query/useStudentMutations";
import { eventsApi } from "@/features/events/api/eventsApi";
import { eventsQueryKeys } from "@/features/events/query/eventsQueryKeys";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";

export const DeleteStudentButton = ({ studentId }: { studentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const { mutate: deleteStudent, isPending: isDeleting } = useDeleteStudent();

  const { data: eventsData, isLoading: isEventsLoading } = useQuery({
    queryKey: [...eventsQueryKeys.byStudent(studentId), { page: 0, size: 1 }],
    queryFn: () => eventsApi.listByStudent(studentId, 0, 1),
    enabled: isOpen,
  });

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteStudent(studentId, {
      onSettled: () => {
        setIsOpen(false);
      },
    });
  };

  const eventsCount = eventsData?.page.totalElements ?? 0;

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
        title="Excluir Aluno"
        isPending={isDeleting}
        isLoadingEvents={isEventsLoading}
        eventsCount={eventsCount}
        itemName="aluno"
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
            <strong>
              todos os seus eventos e atendimentos serão transferidos
              automaticamente para um perfil de "Aluno Removido"
            </strong>{" "}
            para manter a consistência financeira e o histórico da clínica.
          </div>
        }
      />
    </>
  );
};
