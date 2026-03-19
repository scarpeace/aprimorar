import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";
import { useDeleteEvent } from "../query/useEventMutations";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";

export const DeleteEventButton = ({ eventId }: { eventId: string }) => {
  const [isOpen, setIsOpen] = useState(false);
  const { mutate: deleteEvent, isPending: isDeleting } = useDeleteEvent();

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteEvent(eventId, {
      onSettled: () => {
        setIsOpen(false);
      },
    });
  };

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
        title="Excluir Evento"
        isPending={isDeleting}
        itemName="evento"
      />
    </>
  );
};
