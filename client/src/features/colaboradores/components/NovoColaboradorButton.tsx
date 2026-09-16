"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { ColaboradorForm } from "@/features/colaboradores/components/ColaboradorForm";

export function NovoColaboradorButton() {
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
        aria-label="Novo colaborador"
        title="Novo colaborador"
        onClick={() => setIsOpen(true)}
      >
        Novo colaborador
        <Plus size={18} />
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Cadastrar colaborador"
        description="Preencha os dados para criar um novo colaborador."
        size="lg"
      >
        <ColaboradorForm onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
