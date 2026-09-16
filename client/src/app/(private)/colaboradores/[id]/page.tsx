"use client";

import { useParams } from "next/navigation";
import { ColaboradorAtendimentos } from "@/features/colaboradores/components/atendimentos/ColaboradorAtendimentos";
import { ColaboradorCalendar } from "@/features/colaboradores/components/atendimentos/ColaboradorCalendar";
import { ColaboradorProfile } from "@/features/colaboradores/components/ColaboradorProfile";
import { ColaboradorRepassesHistory } from "@/features/colaboradores/components/repasses/ColaboradorRepassesHistory";

export default function ColaboradorPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-6 lg:flex-row lg:items-stretch">
        <div className="min-w-0 lg:flex-2">
          <ColaboradorProfile colaboradorId={id} />
        </div>

        <div className="min-w-0 lg:flex-1">
          <ColaboradorRepassesHistory colaboradorId={id} />
        </div>
      </div>

      <ColaboradorCalendar colaboradorId={id} />
      <ColaboradorAtendimentos colaboradorId={id} />
    </section>
  );
}
