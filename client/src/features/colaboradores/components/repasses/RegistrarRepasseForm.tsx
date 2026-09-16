"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { useAtendimentoMutations } from "@/features/atendimentos/hooks/use-atendimento-mutations";
import { brl } from "@/lib/utils/formatter";
import {
  registrarRepasseFormSchema,
  type RegistrarRepasseFormData,
} from "@/features/colaboradores/schemas/registrar-repasse-form-schema";

const formaPagamentoOptions = [
  { value: "PIX", label: "PIX" },
  { value: "DINHEIRO", label: "Dinheiro" },
  { value: "CARTAO_CREDITO", label: "Cartão de crédito" },
  { value: "CARTAO_DEBITO", label: "Cartão de débito" },
  { value: "BOLETO", label: "Boleto" },
  { value: "TRANSFERENCIA", label: "Transferência" },
];

type RegistrarRepasseFormProps = {
  repasseIds: number[];
  selectedTotal: number;
  onSuccess: () => void;
  onCancel: () => void;
};

export function RegistrarRepasseForm({
  repasseIds,
  selectedTotal,
  onSuccess,
  onCancel,
}: Readonly<RegistrarRepasseFormProps>) {
  const methods = useForm<RegistrarRepasseFormData>({
    resolver: zodResolver(registrarRepasseFormSchema),
    defaultValues: {
      repasseIds,
      formaPagamento: "PIX",
    },
  });
  const { registerCollaboratorPayment } = useAtendimentoMutations();

  const onSubmit = methods.handleSubmit((data) => {
    registerCollaboratorPayment.mutate(
      { data },
      {
        onSuccess,
      },
    );
  });

  return (
    <FormProvider {...methods}>
      <form className="space-y-6" onSubmit={onSubmit}>
        <div className="flex items-center justify-between rounded-box bg-base-200 p-4">
          <p className="text-sm font-medium">{repasseIds.length} repasse(s) selecionado(s)</p>
          <p className="font-bold text-base-content">{brl.format(selectedTotal)}</p>
        </div>

        <SelectInput name="formaPagamento" label="Forma de pagamento" options={formaPagamentoOptions} />

        <label className="form-control">
          <span className="label-text mb-2 text-sm font-medium text-base-content/70">Comprovante</span>
          <input type="file" className="file-input file-input-bordered w-full" disabled />
          <span className="mt-1 text-xs text-base-content/60">
            O envio de comprovantes será disponibilizado futuramente.
          </span>
        </label>

        <div className="flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onCancel} disabled={registerCollaboratorPayment.isPending}>
            Voltar
          </Button>
          <Button type="submit" disabled={registerCollaboratorPayment.isPending}>
            {registerCollaboratorPayment.isPending ? (
              <>
                <LoadingSpinner />
                Registrando...
              </>
            ) : (
              "Registrar repasse"
            )}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
