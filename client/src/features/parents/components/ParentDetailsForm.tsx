import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";

type ParentDetailsFormProps = Readonly<{
  register: UseFormRegister<any>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors: FieldErrors<any>;
  parentId?: string;
  className?: string;
}>;

export function ParentDetailsForm({
  register,
  registerWithMask,
  errors,
  className,
}: ParentDetailsFormProps) {

  return (
    <SectionCard
      title="Dados do responsável"
      description="Preencha as informações de contato e identificação."
    >
      <div className={className}>
        <FormField
          label="Nome completo"
          htmlFor={`parent.name`}
          error={errors[`parent.name`]?.message as string}
        >
          <input
            className="app-input"
            id={`name`}
            placeholder="Ex: Maria Silva"
            {...register(`parent.name`)}
          />
        </FormField>

        <FormField
          label="Email"
          htmlFor={`parent.email`}
          error={errors[`parent.email`]?.message as string}
        >
          <input
            className="app-input"
            id={`email`}
            type="email"
            placeholder="exemplo@dominio.com"
            {...register(`parent.email`)}
          />
        </FormField>

        <FormField
          label="Contato"
          htmlFor={`parent.contact`}
          error={errors[`parent.contact`]?.message as string}
        >
          <input
            className="app-input"
            id={`contact`}
            placeholder="(11) 99999-9999"
            {...registerWithMask(`parent.contact`, [
              "(99) 9999-9999",
              "(99) 99999-9999",
            ])}
          />
        </FormField>

        <FormField
          label="CPF"
          htmlFor={`parent.cpf`}
          error={errors[`parent.cpf`]?.message as string}
        >
          <input
            className="app-input"
            id={`cpf`}
            placeholder="000.000.000-00"
            {...registerWithMask(`parent.cpf`, "999.999.999-99")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
