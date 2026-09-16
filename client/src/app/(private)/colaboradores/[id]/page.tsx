"use client";

import { useParams } from "next/navigation";
import { ColaboradorAtendimentos } from "@/features/colaboradores/components/ColaboradorAtendimentos";
import { ColaboradorProfile } from "@/features/colaboradores/components/ColaboradorProfile";

export default function ColaboradorPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <section className="space-y-6">
      <ColaboradorProfile colaboradorId={id} />
      <ColaboradorAtendimentos colaboradorId={id} />
    </section>
  );
}
