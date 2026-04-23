import { Button } from "@/components/ui/button";
import { ConfirmModal } from "@/components/ui/confirm-modal";
import { ArchiveIcon, ArchiveRestoreIcon } from "lucide-react";
import { useState } from "react";
import { useEmployeeMutations } from "../hooks/emlpoyee-mutations";

type ArchiveEmployeeButtonProps = {
  employeeId: string;
  isArchived: boolean | null;
};

export const ArchiveEmployeeButton = ({
  employeeId,
  isArchived,
}: ArchiveEmployeeButtonProps) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const {
    archiveEmployee: { mutate: archiveEmployee, isPending: isArchiving },
    unarchiveEmployee: { mutate: unarchiveEmployee, isPending: isUnarchiving },
  } = useEmployeeMutations();

  const archived = !!isArchived;
  const isPending = isArchiving || isUnarchiving;
  const actionLabel = archived ? "Desarquivar" : "Arquivar";
  const Icon = archived ? ArchiveRestoreIcon : ArchiveIcon;
  const variant = archived ? "outline" : "warning";
  const modalVariant = archived ? "info" : "warning";

  function handleConfirm() {
    const action = archived ? unarchiveEmployee : archiveEmployee;
    action(
      { employeeId },
      {
        onSettled: () => setShowConfirm(false),
      }
    );
  }

  return (
    <>
      <Button
        type="button"
        onClick={() => setShowConfirm(true)}
        variant={variant}
        disabled={isPending}
      >
        <Icon className="h-4 w-4" />
        {isPending ? "Processando..." : actionLabel}
      </Button>

      <ConfirmModal
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={handleConfirm}
        title={`${actionLabel} Colaborador`}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o colaborador?`}
        isPending={isPending}
        confirmText="Sim"
        cancelText="Cancelar"
        variant={modalVariant}
      />
    </>
  );
};
