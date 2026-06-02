import { Button } from "@/components/ui/button";
import { ConfirmationModal } from "@/components/ui/confirmation-modal";
import { useGetAlunosByResponsavel } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useResponsavelMutations } from "../hooks/use-responsavel-mutations";

export const DeleteResponsavelButton = ({ responsavelId }: { responsavelId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const {deleteParent} = useResponsavelMutations();

  const {
    data: linkedStudents,
    isLoading: isLinkedStudentsLoading,
  } = useGetAlunosByResponsavel(responsavelId);

  const linkedStudentsCount = linkedStudents?.length ?? 0;
  const isParentStudentsLoading = isLinkedStudentsLoading;

  const linkedStudentsWarning = linkedStudentsCount > 0 ? (
    <div className="rounded-md bg-warning/10 p-4 text-sm text-warning-content">
      Atenção: este responsável possui {linkedStudentsCount} aluno(s) vinculado(s).
      Ao confirmar, todos os alunos vinculados também serão removidos do sistema.
    </div>
  ) : null;

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!deleteParent.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteParent.mutate({ responsavelId: responsavelId, params: { cascade: true } }, {
      onSuccess: () => {
        setIsOpen(false);
      },
    });
  };

  return (
    <>
      <Button
        type="button"
        onClick={handleOpenClick}
        disabled={deleteParent.isPending}
        variant="danger"
      >
        <Trash2 className="h-4 w-4" />
        {deleteParent.isPending ? "Excluindo..." : "Excluir"}
      </Button>
      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Atenção: exclusão definitiva"
        description="Atenção, todos os registros serão removidos do sistema. Tem certeza que deseja continuar com essa exclusão?"
        variant="danger"
        isPending={deleteParent.isPending}
        disableCloseOnPending
        confirmText="Excluir responsável e alunos"
      >
        {isParentStudentsLoading ? (
          <p className="text-sm text-base-content/70">Verificando dados vinculados...</p>
        ) : (
          linkedStudentsWarning
        )}
      </ConfirmationModal>
    </>
  );
};
