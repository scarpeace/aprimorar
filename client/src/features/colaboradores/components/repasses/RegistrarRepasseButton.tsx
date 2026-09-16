"use client";

import { Banknote } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/Button";
import { RegistrarRepasseModal } from "@/features/colaboradores/components/repasses/RegistrarRepasseModal";

type RegistrarRepasseButtonProps = {
  colaboradorId: string;
};

export function RegistrarRepasseButton({ colaboradorId }: Readonly<RegistrarRepasseButtonProps>) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <Button type="button" variant="success" size="sm" onClick={() => setIsOpen(true)}>
        <Banknote size={16} />
        Registrar repasse
      </Button>

      <RegistrarRepasseModal colaboradorId={colaboradorId} isOpen={isOpen} onClose={() => setIsOpen(false)} />
    </>
  );
}
