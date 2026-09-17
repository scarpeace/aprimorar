"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { DespesaForm } from "@/features/despesas/components/DespesaForm";

export function NovaDespesaButton() {
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
        aria-label="Nova despesa"
        title="Nova despesa"
        onClick={() => setIsOpen(true)}
      >
        Nova despesa
        <Plus size={18} />
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Cadastrar despesa"
        description="Preencha os dados para registrar uma despesa operacional."
        size="lg"
      >
        <DespesaForm onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
