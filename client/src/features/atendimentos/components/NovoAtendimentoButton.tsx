"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { AtendimentoForm } from "@/features/atendimentos/components/AtendimentoForm";

export function NovoAtendimentoButton() {
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
        aria-label="Novo atendimento"
        title="Novo atendimento"
        onClick={() => setIsOpen(true)}
      >
        Novo atendimento
        <Plus size={18} />
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Cadastrar atendimento"
        description="Defina participante, horário e valores para criar um novo atendimento."
        size="lg"
      >
        <AtendimentoForm onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
