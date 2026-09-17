"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { LoadingSpinner } from "@/components/ui/LoadingSpinner";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { useCobrancaMutations } from "@/features/alunos/hooks/use-cobranca-mutations";
import {
  registrarCobrancaFormSchema,
  type RegistrarCobrancaFormData,
} from "@/features/alunos/schemas/registrar-cobranca-form-schema";
import { formaPagamentoLabels } from "@/lib/constants/pagamento-constants";
import { brl } from "@/lib/utils/formatter";

const formaPagamentoOptions = [
  { value: "PIX", label: formaPagamentoLabels.PIX },
  { value: "DINHEIRO", label: formaPagamentoLabels.DINHEIRO },
  { value: "CARTAO_CREDITO", label: formaPagamentoLabels.CARTAO_CREDITO },
  { value: "CARTAO_DEBITO", label: formaPagamentoLabels.CARTAO_DEBITO },
  { value: "BOLETO", label: formaPagamentoLabels.BOLETO },
  { value: "TRANSFERENCIA", label: formaPagamentoLabels.TRANSFERENCIA },
];

type RegistrarCobrancaFormProps = {
  cobrancaIds: number[];
  selectedTotal: number;
  onSuccess: () => void;
  onCancel: () => void;
};

export function RegistrarCobrancaForm({
  cobrancaIds,
  selectedTotal,
  onSuccess,
  onCancel,
}: Readonly<RegistrarCobrancaFormProps>) {
  const methods = useForm<RegistrarCobrancaFormData>({
    resolver: zodResolver(registrarCobrancaFormSchema),
    defaultValues: {
      cobrancaIds,
      formaPagamento: "PIX",
    },
  });
  const { registerStudentPayment } = useCobrancaMutations();

  useEffect(() => {
    methods.setValue("cobrancaIds", cobrancaIds, { shouldValidate: true });
  }, [cobrancaIds, methods]);

  const onSubmit = methods.handleSubmit((data) => {
    registerStudentPayment.mutate(
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
          <p className="text-sm font-medium">{cobrancaIds.length} cobrança(s) selecionada(s)</p>
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
          <Button type="button" variant="ghost" onClick={onCancel} disabled={registerStudentPayment.isPending}>
            Cancelar
          </Button>
          <Button type="submit" disabled={cobrancaIds.length === 0 || registerStudentPayment.isPending}>
            {registerStudentPayment.isPending ? (
              <>
                <LoadingSpinner />
                Registrando...
              </>
            ) : (
              "Registrar cobrança"
            )}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
