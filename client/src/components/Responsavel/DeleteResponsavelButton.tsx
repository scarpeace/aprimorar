import { Button } from "@/components/button.tsx";
import { ConfirmationModal } from "@/components/confirmation-modal.tsx";
import { useGetAlunosByResponsavel } from "@/kubb";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { useResponsavelMutations } from "../../services/use-responsavel-mutations.ts";

export const DeleteResponsavelButton = ({ responsavelId }: { responsavelId: string }) => {
  const [isOpen, setIsOpen] = useState(false);

  const { deleteResponsavel } = useResponsavelMutations();

  const {
    data: alunosVinculados,
    isLoading: isAlunosVinculadosLoading,
  } = useGetAlunosByResponsavel(responsavelId);

  const alunosVinculadosCount = alunosVinculados?.length ?? 0;

  const alunosVinculadosWarning = alunosVinculadosCount > 0 ? (
    <div className="rounded-md bg-warning/10 p-4 text-sm text-warning-content">
      Atenção: este responsável possui {alunosVinculadosCount} aluno(s) vinculado(s).
      Ao confirmar, todos os alunos vinculados também serão removidos do sistema.
    </div>
  ) : null;

  const handleOpenClick = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    if (!deleteResponsavel.isPending) {
      setIsOpen(false);
    }
  };

  const handleConfirmDelete = () => {
    deleteResponsavel.mutate({ responsavelId, params: { cascade: true } }, {
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
        disabled={deleteResponsavel.isPending}
        variant="danger"
      >
        <Trash2 className="h-4 w-4" />
        {deleteResponsavel.isPending ? "Excluindo..." : "Excluir"}
      </Button>
      <ConfirmationModal
        isOpen={isOpen}
        onClose={handleClose}
        onConfirm={handleConfirmDelete}
        title="Atenção: exclusão definitiva"
        description="Atenção, todos os registros serão removidos do sistema. Tem certeza que deseja continuar com essa exclusão?"
        variant="danger"
        isPending={deleteResponsavel.isPending}
        disableCloseOnPending
        confirmText="Excluir responsável e alunos"
      >
        {isAlunosVinculadosLoading ? (
          <p className="text-sm text-base-content/70">Verificando dados vinculados...</p>
        ) : (
          alunosVinculadosWarning
        )}
      </ConfirmationModal>
    </>
  );
};
