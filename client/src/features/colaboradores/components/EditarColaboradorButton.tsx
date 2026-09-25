"use client";

import { PencilLine } from "lucide-react";
import { useState } from "react";
import type { ColaboradorResponse } from "@/lib/api/generated/types/ColaboradorResponse";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { ColaboradorForm } from "@/features/colaboradores/components/ColaboradorForm";

type EditarColaboradorButtonProps = {
  colaborador: ColaboradorResponse;
};

export function EditarColaboradorButton({ colaborador }: Readonly<EditarColaboradorButtonProps>) {
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
        aria-label="Editar colaborador"
        title="Editar colaborador"
        onClick={() => setIsOpen(true)}
      >
        <PencilLine size={18} />
        Editar
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Editar colaborador"
        description="Atualize os dados do colaborador sem sair da tela de detalhe."
        size="lg"
      >
        <ColaboradorForm initialData={colaborador} onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
