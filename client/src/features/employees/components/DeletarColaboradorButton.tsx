import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useColaboradorMutations } from "../hooks/use-colaborador-mutations";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useGetAtendimentosByColaborador } from "@/kubb";

export const DeletarColaboradorButton = ({ colaboradorId }: { colaboradorId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const {
    deleteEmployee: { mutate: deleteEmployee, isPending: isDeleting },
  } = useColaboradorMutations();

  const { data: eventsData, isLoading: isEventsLoading } =
    useGetAtendimentosByColaborador(colaboradorId);

  const handleOpenClick = () => {
    setErrorMessage(null);
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setErrorMessage(null);
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    setErrorMessage(null);
    deleteEmployee(
      { colaboradorId: colaboradorId },
      {
        onSuccess: () => {
          setIsOpen(false);
        },
        onError: (error) => {
          setErrorMessage(getFriendlyErrorMessage(error));
        },
      },
    );
  };

  const eventsCount = eventsData?.atendimentos?.totalElements ?? 0;

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={isDeleting}
        variant="danger"
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
        errorMessage={errorMessage}
        phantomWarning={
          <div className="bg-warning/10 text-warning-content p-4 rounded-md text-sm">
            Ao excluí-lo, seu histórico pessoal será apagado, mas{" "}
            <strong>
              todos os seus atendimentoos e atendimentos serão transferidos
              automaticamente para um perfil de "Colaborador Removido"
            </strong>{" "}
            para manter a consistência financeira e o histórico.
          </div>
        }
      />
    </>
  );
};
