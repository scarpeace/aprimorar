import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { getActiveLinkedStudentsCount } from "@/features/parents/lib/getActiveLinkedStudentsCount";
import { useListarAlunosPorResponsavel } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useResponsavelMutations } from "../hooks/use-responsavel-mutations";

export const DeletarResponsavelButton = ({ responsavelId }: { responsavelId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const {
    deleteParent: {
      mutate: deleteParent,
      isPending: isDeleting,
    },
  } = useResponsavelMutations();

  const {
    data: linkedStudents,
    isLoading: isLinkedStudentsLoading,
  } = useListarAlunosPorResponsavel(responsavelId);

  const linkedStudentsCount = linkedStudents?.length ?? 0;
  const activeLinkedStudentsCount = getActiveLinkedStudentsCount(linkedStudents);
  const archivedLinkedStudentsCount = Math.max(
    linkedStudentsCount - activeLinkedStudentsCount,
    0,
  );
  const hasActiveLinkedStudents = activeLinkedStudentsCount > 0;
  const isParentStudentsLoading = isLinkedStudentsLoading;

  const linkedStudentsWarning = hasActiveLinkedStudents ? (
    <div className="rounded-md bg-warning/10 p-4 text-sm text-warning-content">
      Não é possível excluir este responsável enquanto houver {activeLinkedStudentsCount} aluno(s) ativo(s) vinculado(s). Atualize ou arquive esses cadastros antes de tentar novamente.
    </div>
  ) : archivedLinkedStudentsCount > 0 ? (
    <div className="rounded-md bg-base-200 p-4 text-sm text-base-content/80">
      Este responsável mantém {archivedLinkedStudentsCount} vínculo(s) apenas no histórico arquivado. Esse histórico continuará visível para consulta e não bloqueia a exclusão.
    </div>
  ) : null;

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!isDeleting) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteParent(
      { responsavelId: responsavelId },
      {
        onSuccess: () => {
          setIsOpen(false);
        },
      },
    );
  };

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
        title="Excluir responsável"
        isItemPending={isDeleting}
        isItemLoading={isParentStudentsLoading}
        itemDeleteCount={hasActiveLinkedStudents ? activeLinkedStudentsCount : archivedLinkedStudentsCount}
        itemName="responsável"
        linkedItemsLabel="aluno(s)"
        isBlocker={hasActiveLinkedStudents}
        phantomWarning={linkedStudentsWarning}
        confirmText="Sim, excluir responsável"
      />
    </>
  );
};
