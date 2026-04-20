import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { getActiveLinkedStudentsCount } from "@/features/parents/utils/getActiveLinkedStudentsCount";
import { useGetStudentsByParent } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useParentMutations } from "../hooks/parent-mutations";

export const DeleteParentButton = ({ parentId }: { parentId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const {
    deleteParent: {
      mutate: deleteParent,
      isPending: isDeleting,
    },
  } = useParentMutations();

  const {
    data: linkedStudentsMeta,
    isLoading: isLinkedStudentsMetaLoading,
  } = useGetStudentsByParent(parentId, {
    page: 0,
    size: 1,
  });

  const linkedStudentsCount = linkedStudentsMeta?.totalElements ?? 0;

  const {
    data: linkedStudentsSummary,
    isLoading: isLinkedStudentsSummaryLoading,
  } = useGetStudentsByParent(
    parentId,
    {
      page: 0,
      size: linkedStudentsCount,
    },
    {
      query: {
        enabled: !!parentId && linkedStudentsCount > 0,
      },
    },
  );

  const activeLinkedStudentsCount = getActiveLinkedStudentsCount(linkedStudentsSummary);
  const archivedLinkedStudentsCount = Math.max(
    linkedStudentsCount - activeLinkedStudentsCount,
    0,
  );
  const hasActiveLinkedStudents = activeLinkedStudentsCount > 0;
  const isParentStudentsLoading =
    isLinkedStudentsMetaLoading || isLinkedStudentsSummaryLoading;

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
      { parentId },
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
        className="sm:mr-auto"
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
