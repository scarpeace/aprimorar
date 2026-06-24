import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useHookFormMask } from "use-mask-input";
import { Button } from "@/components/Ui/Button.tsx";
import { TriangleAlert } from "lucide-react";
import type { ResponsavelResponseDTO } from "@/kubb";
import { responsavelFormSchema, type ResponsavelFormSchema } from "../../utils/zod/responsavel-form-schema.ts";
import { useResponsavelMutations } from "../../services/use-responsavel-mutations.ts";
import { formatDateForInput } from "@/utils/date-utils.ts";
import type { ReactNode } from "react";
import { TextInput } from "@/components/Forms/TextInput.tsx";
import { MaskedInput } from "@/components/Forms/MaskedInput.tsx";

interface ResponsavelFormProps {
  initialData?: ResponsavelResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function ResponsavelForm({ initialData, onSuccess, onCancel }: ResponsavelFormProps) {
  const { createResponsavel, updateResponsavel } = useResponsavelMutations();
  const isEditMode = !!initialData;

  const methods = useForm<ResponsavelFormSchema>({
    resolver: zodResolver(responsavelFormSchema),
    mode: "onBlur",
    defaultValues: {
      nome: initialData?.nome,
      email: initialData?.email,
      telefone: initialData?.telefone,
      dataNascimento: formatDateForInput(initialData?.dataNascimento),
      cpf: initialData?.cpf,
    },
  });


  const onSubmit = methods.handleSubmit((data: ResponsavelFormSchema) => {
    if (isEditMode && initialData.id) {
      updateResponsavel.mutate({ responsavelId: initialData.id, data },
        { onSuccess }
      );
    } else {
      createResponsavel.mutate({ data },
        { onSuccess }
      );
    }
  });

  const isPending = createResponsavel.isPending || updateResponsavel.isPending;

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-x-3 md:grid-cols-3">

          <TextInput name="nome" label="Nome" placeholder="Nome completo" />
          <TextInput name="email" label="Email" type="email" placeholder="Email"/>
          <MaskedInput name="dataNascimento" label="Data de Nascimento" mask="00/00/0000" placeholder="Ex: 11/12/2003"/>
          <MaskedInput name="telefone" label="Telefone" mask="(00) 00000-0000" placeholder="Ex: (11) 99999-9999"/>
          <MaskedInput name="cpf" label="CPF" mask="000.000.000-00" placeholder="Ex: 123.456.789-00"/>
        </div>

        <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="ghost" onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isPending} variant="primary">
            {isPending ? "Salvando..." : (isEditMode ? "Atualizar Responsável" : "Salvar Responsável")}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
