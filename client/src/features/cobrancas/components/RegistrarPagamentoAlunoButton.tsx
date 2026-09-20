"use client";

import { Banknote } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { RegistrarPagamentoAlunoModal } from "@/features/cobrancas/components/RegistrarPagamentoAlunoModal";

type RegistrarPagamentoAlunoButtonProps = {
  alunoId: string;
};

export function RegistrarPagamentoAlunoButton({ alunoId }: Readonly<RegistrarPagamentoAlunoButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <Button type="button" variant="success" size="sm" onClick={() => setIsOpen(true)}>
        <Banknote size={16} />
        Registrar pagamento
      </Button>

      <RegistrarPagamentoAlunoModal alunoId={alunoId} isOpen={isOpen} onClose={() => setIsOpen(false)} />
    </>
  );
}
