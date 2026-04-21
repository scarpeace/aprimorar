import { Button, ButtonLink } from "@/components/ui/button";
import { SectionCard } from "@/components/ui/section-card";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import { TriangleAlert } from "lucide-react";
import type { PropsWithChildren, SubmitEventHandler } from "react";
import type { FieldErrors, UseFormRegister } from "react-hook-form";

import { ContentSelectDropdown } from "../components/ContentSelectDropdown";
import { StatusSelectDropdown } from "../components/StatusSelectDropdown";
import type { EventFormSchema } from "./eventFormSchema";

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
  register: UseFormRegister<EventFormSchema>;
  errors: FieldErrors<EventFormSchema>;
};

export function Fields({ errors, register }: FieldsProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
      <EmployeeSelectDropdown
        registration={register("employeeId")}
        error={errors?.employeeId?.message}
        label="Colaborador"
      />

      <StudentSelectDropdown
        registration={register("studentId")}
        error={errors?.studentId?.message}
        label={"Aluno"}
      />

      <ContentSelectDropdown
        label="Atendimento"
        registration={register("content")}
        error={errors.content?.message}
      />

      <StatusSelectDropdown
        label="Status"
        registration={register("status")}
        error={errors.status?.message}
      />

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Data de Início</legend>
        <input
          type="datetime-local"
          className="input"
          placeholder="Data de Início"
          {...register("startDate")}
        />
        {errors?.startDate && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.startDate.message}
          </p>
        )}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Fim</legend>
        <input
          type="datetime-local"
          className="input"
          placeholder="Data de Fim"
          {...register("endDate")}
        />
        {errors?.endDate && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.endDate.message}
          </p>
        )}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Preço (receita)</legend>
        <input
          type="number"
          className="input"
          placeholder="Preço (receita)"
          {...register("price", {
          valueAsNumber: true,
        })}
        />
        {errors?.price && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.price.message}
          </p>
        )}
      </fieldset>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Pagamento (custo)</legend>
        <input
          type="number"
          className="input"
          placeholder="Pagamento (custo)"
          {...register("payment", {
          valueAsNumber: true,
        })}
        />
        {errors?.payment && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.payment.message}
          </p>
        )}
      </fieldset>

      <fieldset className="fieldset md:col-span-3">
        <legend className="fieldset-legend">Descrição (opcional)</legend>
        <textarea
          className="textarea textarea-bordered w-full"
          placeholder="Observações do atendimento"
          {...register("description")}
        />
        {errors?.description && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.description.message}
          </p>
        )}
      </fieldset>
    </div>
  );
}

type ActionsProps = {
  isSubmitting: boolean;
  cancelTo: string;
  submitLabel?: string;
  submittingLabel?: string;
};

export function Actions({
  isSubmitting,
  cancelTo,
  submitLabel = "Salvar",
  submittingLabel = "Salvando...",
}: ActionsProps) {
  return (
    <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
      <ButtonLink to={cancelTo} variant="outline">
        Cancelar
      </ButtonLink>
      <Button type="submit" disabled={isSubmitting} variant="primary">
        {isSubmitting ? submittingLabel : submitLabel}
      </Button>
    </div>
  );
}
