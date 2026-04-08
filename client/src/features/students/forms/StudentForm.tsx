import { Button, ButtonLink } from "@/components/ui/button";
import { FieldsetInput } from "@/components/ui/fieldset-input";
import { SectionCard } from "@/components/ui/section-card";
import type { PropsWithChildren, SubmitEventHandler } from "react";
import type { FieldErrors } from "react-hook-form";
import type { StudentFormSchema } from "./studentFormSchema";
import { useHookFormMask } from "use-mask-input";
import { ParentSelectDropdown } from "@/features/parents/components/ParentSelectDropdown";
import { AddressFormFields } from "@/features/address/forms/AddressFormFields";

type FormProps = PropsWithChildren<{
  title: string;
  description: string;
  onSubmit: SubmitEventHandler<HTMLFormElement>;
}>;

export function Root({ title, description, onSubmit, children }: FormProps) {
  return (
    <SectionCard title={title} description={description}>
      <form className="flex flex-col gap-3" onSubmit={onSubmit}>
        {children}
      </form>
    </SectionCard>
  );
}

type FieldsProps = {
  register: any;
  errors: FieldErrors<StudentFormSchema>;
};

export function Fields({ errors, register }: FieldsProps) {
  const registerWithMask = useHookFormMask(register);
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5  ">
      <ParentSelectDropdown
        className="col-span-3"
        registration={register("parentId")}
        error={errors.parentId?.message}
        label="Responsável"
      />

      <FieldsetInput
        label="Nome Completo"
        placeholder="Nome Completo"
        type="text"
        registration={register("name")}
        error={errors?.name?.message}
      />

      <FieldsetInput
        label="CPF"
        placeholder="CPF"
        type="text"
        registration={registerWithMask("cpf", "999.999.999-99")}
        error={errors?.cpf?.message}
      />

      <FieldsetInput
        label="Contato"
        placeholder="Contato"
        type="text"
        registration={registerWithMask("contact", [
          "(99) 9999-9999",
          "(99) 99999-9999",
        ])}
        error={errors?.contact?.message}
      />

      <FieldsetInput
        label="Email"
        placeholder="Email"
        type="email"
        registration={register("email")}
        error={errors?.email?.message}
      />

      <FieldsetInput
        label="Data de nascimento"
        placeholder="Data de nascimento"
        type="date"
        registration={register("birthdate")}
        error={errors?.birthdate?.message}
      />

      <FieldsetInput
        label="Escola"
        placeholder="Escola"
        type="text"
        registration={register("school")}
        error={errors?.school?.message}
      />

      <div className="divider col-span-3 m-0" />
      <div className="col-span-3">
        <h1>Endereço</h1>
        <AddressFormFields
          register={register}
          prefix="address"
          errors={errors.address}
          registerWithMask={registerWithMask}
        />
      </div>
    </div>
  );
}

type ActionsProps = {
  isSubmitting: boolean;
  cancelTo: string;
};

export function Actions({ isSubmitting, cancelTo }: ActionsProps) {
  return (
    <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
      <ButtonLink to={cancelTo} variant="outline">
        Cancelar
      </ButtonLink>
      <Button type="submit" disabled={isSubmitting} variant="primary">
        {isSubmitting ? "Salvando..." : "Salvar"}
      </Button>
    </div>
  );
}
