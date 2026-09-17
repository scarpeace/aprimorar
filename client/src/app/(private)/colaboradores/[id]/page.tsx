"use client";

import { useParams } from "next/navigation";
import { ColaboradorAtendimentosTable } from "@/features/colaboradores/components/atendimentos/ColaboradorAtendimentosTable";
import { ColaboradorCalendar } from "@/features/colaboradores/components/atendimentos/ColaboradorCalendar";
import { ColaboradorProfile } from "@/features/colaboradores/components/ColaboradorProfile";
import { ColaboradorRepassesHistory } from "@/features/colaboradores/components/repasses/ColaboradorRepassesHistory";

export default function ColaboradorPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-6 lg:flex-row lg:items-stretch">
        <div className="min-w-0 lg:flex-3">
          <ColaboradorProfile colaboradorId={id} />
        </div>

        <div className="min-w-0 lg:flex-3">
          <ColaboradorCalendar colaboradorId={id} />
        </div>
      </div>

      <div className="flex flex-col gap-6 lg:flex-row lg:items-stretch">
        <div className="min-w-0 lg:flex-3">
          <ColaboradorAtendimentosTable colaboradorId={id} />
        </div>

        <div className="min-w-0 lg:flex-[1.5]">
          <ColaboradorRepassesHistory colaboradorId={id} />
        </div>
      </div>
    </section>
  );
}
