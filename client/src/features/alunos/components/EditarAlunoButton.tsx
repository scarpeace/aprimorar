"use client";

import { PencilLine } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { AlunoForm } from "@/features/alunos/components/AlunoForm";
import type { AlunoDetailResponseDTO } from "@/lib/api/generated/types/AlunoDetailResponseDTO";

type EditarAlunoButtonProps = {
  aluno: AlunoDetailResponseDTO;
};

export function EditarAlunoButton({ aluno }: Readonly<EditarAlunoButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);

  function close() {
    setIsOpen(false);
  }

  return (
    <>
      <Button
        type="button"
        size="sm"
        variant="primary"
        aria-label="Editar aluno"
        title="Editar aluno"
        onClick={() => setIsOpen(true)}
      >
        <PencilLine size={18} />
        Editar
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Editar aluno"
        description="Atualize os dados do aluno sem sair da tela de detalhe."
        size="lg"
      >
        <AlunoForm initialData={aluno} onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
