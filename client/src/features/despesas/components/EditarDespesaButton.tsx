"use client";

import { PencilLine } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { DespesaForm } from "@/features/despesas/components/DespesaForm";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";

type EditarDespesaButtonProps = {
  despesa: DespesaResponse;
};

export function EditarDespesaButton({ despesa }: Readonly<EditarDespesaButtonProps>) {
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
        aria-label="Editar despesa"
        title="Editar despesa"
        onClick={() => setIsOpen(true)}
      >
        <PencilLine size={18} />
        Editar
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Editar despesa"
        description="Atualize os dados da despesa sem sair da tela de detalhe."
        size="lg"
      >
        <DespesaForm initialData={despesa} onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
