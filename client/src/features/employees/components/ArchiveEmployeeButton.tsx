import { Button } from "@/components/ui/button";
import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
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

  function handleConfirm() {
    const action = archived ? unarchiveEmployee : archiveEmployee;
    action({ employeeId });
    setShowConfirm(false);
  }

  if (isPending) {
    return (
      <Button type="button" disabled variant="outline">
        <Loader2Icon className="h-4 w-4 animate-spin" />
        Processando...
      </Button>
    );
  }

  if (showConfirm) {
    return (
      <InlineConfirmAlert
        variant={archived ? "info" : "warning"}
        message={`Deseja mesmo ${actionLabel.toLowerCase()} o colaborador?`}
        confirmText="Sim"
        cancelText="Cancelar"
        onConfirm={handleConfirm}
        onCancel={() => setShowConfirm(false)}
        className="p-2"
      />
    );
  }

  return (
    <Button
      type="button"
      onClick={() => setShowConfirm(true)}
      variant={variant}
    >
      <Icon className="h-4 w-4" />
      {actionLabel}
    </Button>
  );
};
