"use client";

import { Plus } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { UserForm } from "@/features/usuarios/components/UserForm";

export function NovoUsuarioButton() {
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
        aria-label="Novo usuário"
        title="Novo usuário"
        onClick={() => setIsOpen(true)}
      >
        Novo usuário
        <Plus size={18} />
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={close}
        title="Cadastrar usuário"
        description="Crie um novo acesso para a secretaria."
        size="md"
      >
        <UserForm onSuccess={close} onCancel={close} />
      </Modal>
    </>
  );
}
