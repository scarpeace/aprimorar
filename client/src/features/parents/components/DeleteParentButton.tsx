import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useDeleteParent } from "../query/useParentMutations";
import { studentsApi } from "@/features/students/api/studentsApi";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";

export const DeleteParentButton = ({ parentId }: { parentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const { mutate: deleteParent, isPending: isDeleting } = useDeleteParent();

  // Find linked students to warn the user, similarly to how events are checked for employees
  // Reused studentsByParent query concept to warn the user
  const { data: studentsData, isLoading: isStudentsLoading } = useQuery({
    queryKey: studentsQueryKeys.byParent(parentId),
    queryFn: () => studentsApi.listByParent(parentId),
    enabled: isOpen,
  });

  // To match the behavior exactly as the employee button, we show a DeleteConfirmationModal
  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteParent(parentId, {
      onSettled: () => {
        setIsOpen(false);
      },
    });
  };

  const studentsCount = studentsData?.length ?? 0;

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
        title="Restrição de Exclusão"
        isPending={isDeleting}
        isLoadingEvents={isStudentsLoading}
        eventsCount={studentsCount}
        itemName="responsável"
        isBlocker={studentsCount > 0}
        phantomWarning={
          studentsCount > 0 ? (
            <div className="bg-error/10 text-error-content p-4 rounded-md text-sm mt-3">
              Este responsável ainda possui{" "}
              <strong>{studentsCount} aluno(s) vinculado(s)</strong> no sistema.
              <br />
              <br />
              Você não pode excluí-lo sem antes acessar os alunos e excluí-los
              individualmente. Isso garante que o fluxo financeiro e histórico
              de eventos deles sejam tratados da forma correta.
            </div>
          ) : (
            <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm mt-3">
              Esta ação excluirá permanentemente o responsável e não pode ser
              desfeita.
            </div>
          )
        }
      />
    </>
  );
};
