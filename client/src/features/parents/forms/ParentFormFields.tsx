import { FormField } from "@/components/ui/form-field";
import { SectionCard } from "@/components/ui/section-card";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { useHookFormMask } from "use-mask-input";
import type { ParentFormSchema } from "./parentFormSchema";

type ParentFormFieldsProps = Readonly<{
  register: UseFormRegister<any>;
  registerWithMask: ReturnType<typeof useHookFormMask>;
  errors?: FieldErrors<ParentFormSchema>;
  className?: string;
  isUpdate?: boolean;
}>;

export function ParentFormFields({
  register,
  registerWithMask,
  errors,
  className,
  isUpdate
}: ParentFormFieldsProps) {


  return (
    <SectionCard
      title="Dados do responsável"
      description="Preencha as informações de contato e identificação."
    >
      <div className={className}>
        <FormField
          label="Nome completo"
          htmlFor="name"
          error={errors?.name?.message}
        >
          <input
            className="app-input"
            id={`name`}
            placeholder="Ex: Maria Silva"
            {...register("name")}
          />
        </FormField>

        <FormField
          label="Email"
          htmlFor="email"
          error={errors?.email?.message}
        >
          <input
            className="app-input"
            id={`email`}
            type="email"
            placeholder="exemplo@dominio.com"
            {...register("email")}
          />
        </FormField>

        <FormField
          label="Contato"
          htmlFor="contact"
          error={errors?.contact?.message}
        >
          <input
            className="app-input"
            id={`contact`}
            placeholder="(11) 99999-9999"
            {...registerWithMask("contact", [
              "(99) 9999-9999",
              "(99) 99999-9999",
            ])}
          />
        </FormField>

        <FormField
          label="CPF"
          htmlFor="cpf"
          error={errors?.cpf?.message}
        >
          <input
            className="app-input"
            disabled={isUpdate}
            id={`cpf`}
            placeholder="000.000.000-00"
            {...registerWithMask("cpf", "999.999.999-99")}
          />
        </FormField>
      </div>
    </SectionCard>
  );
}
