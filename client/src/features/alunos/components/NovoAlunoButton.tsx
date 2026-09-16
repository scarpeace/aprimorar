"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { AlunoForm } from "@/features/alunos/components/AlunoForm";

export function NovoAlunoButton() {
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
        aria-label="Novo aluno"
        title="Novo aluno"
        onClick={() => setIsOpen(true)}
      >
        Novo aluno
        <Plus size={18} />
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Cadastrar aluno"
        description="Preencha os dados para criar um novo aluno."
        size="lg"
      >
        <AlunoForm onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
