"use client";

import { Banknote } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { RegistrarCobrancaModal } from "@/features/alunos/components/cobrancas/RegistrarCobrancaModal";

type RegistrarCobrancaButtonProps = {
  alunoId: string;
};

export function RegistrarCobrancaButton({ alunoId }: Readonly<RegistrarCobrancaButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <Button type="button" variant="success" size="sm" onClick={() => setIsOpen(true)}>
        <Banknote size={16} />
        Registrar cobrança
      </Button>

      <RegistrarCobrancaModal alunoId={alunoId} isOpen={isOpen} onClose={() => setIsOpen(false)} />
    </>
  );
}
