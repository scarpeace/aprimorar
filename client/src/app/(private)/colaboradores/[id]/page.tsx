"use client";

import { useParams } from "next/navigation";
import { ColaboradorAtendimentosTable } from "@/features/colaboradores/components/ColaboradorAtendimentosTable";
import { ColaboradorProfile } from "@/features/colaboradores/components/ColaboradorProfile";

export default function ColaboradorPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <section className="space-y-6">
      <ColaboradorProfile colaboradorId={id} />
      <ColaboradorAtendimentosTable colaboradorId={id} />
    </section>
  );
}
