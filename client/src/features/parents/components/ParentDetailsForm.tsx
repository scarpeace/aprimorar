import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";
import z from "zod";
import type { ParentInputSchema } from "../hooks/parentSchema";

type ParentDetailsFormProps = Readonly<{
  register: UseFormRegister<any>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  prefix?: string;
  errors?: FieldErrors<ParentInputSchema>;
  className?: string;
}>;

export function ParentDetailsForm({
  register,
  registerWithMask,
  prefix,
  errors,
  className,
}: ParentDetailsFormProps) {

  const withPrefix = (field: string) => prefix ? `${prefix}.${field}` : field;

  return (
    <SectionCard
      title="Dados do responsável"
      description="Preencha as informações de contato e identificação."
    >
      <div className={className}>
        <FormField
          label="Nome completo"
          htmlFor={withPrefix(`name`)}
          error={errors?.name?.message}
        >
          <input
            className="app-input"
            id={`name`}
            placeholder="Ex: Maria Silva"
            {...register(withPrefix(`name`))}
          />
        </FormField>

        <FormField
          label="Email"
          htmlFor={withPrefix(`email`)}
          error={errors?.email?.message}
        >
          <input
            className="app-input"
            id={`email`}
            type="email"
            placeholder="exemplo@dominio.com"
            {...register(withPrefix(`email`))}
          />
        </FormField>

        <FormField
          label="Contato"
          htmlFor={withPrefix(`contact`)}
          error={errors?.contact?.message}
        >
          <input
            className="app-input"
            id={`contact`}
            placeholder="(11) 99999-9999"
            {...registerWithMask(withPrefix(`contact`), [
              "(99) 9999-9999",
              "(99) 99999-9999",
            ])}
          />
        </FormField>

        <FormField
          label="CPF"
          htmlFor={withPrefix(`cpf`)}
          error={errors?.cpf?.message}
        >
          <input
            className="app-input"
            id={`cpf`}
            placeholder="000.000.000-00"
            {...registerWithMask(withPrefix(`cpf`), "999.999.999-99")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
