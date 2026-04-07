import { Button, ButtonLink } from "@/components/ui/button";
import { FieldsetInput } from "@/components/ui/fieldset-input";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import {
  type LucideIcon
} from "lucide-react";
import type {
  PropsWithChildren,
  SubmitEventHandler
} from "react";
import type { Control, FieldErrors } from "react-hook-form";
import { ContentSelectDropdown } from "../components/ContentSelectDropdown";
import type { EventFormSchema } from "./eventFormSchema";
import { DevTool } from "@hookform/devtools";

type FormProps = PropsWithChildren<{
  title: string;
  description: string;
  onSubmit: SubmitEventHandler<HTMLFormElement>;
}>;

type HeaderProps = {
  title: string;
  description: string;
  icon: LucideIcon;
};

type FieldsProps = {
  register: any;
  errors: FieldErrors<EventFormSchema>;
};

type ActionsProps = {
  isSubmitting: boolean;
  cancelTo: string;
  submitLabel: string;
  submittingLabel: string;
};

export function Root({ title, description, onSubmit, children }: FormProps) {
  return (
    <SectionCard title={title} description={description}>
      <form className="flex flex-col gap-3" onSubmit={onSubmit}>
        {children}
      </form>
    </SectionCard>
  );
}

export function Header({ title, description, icon }: HeaderProps) {
  return <PageHeader title={title} description={description} Icon={icon} />;
}

export function Fields({ errors, register }: FieldsProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5  ">

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

      <FieldsetInput
        label="Data de Início"
        placeholder="Data de Início"
        type="datetime-local"
        registration={register("startDate")}
        error={errors?.startDate?.message}
      />

      <FieldsetInput
        label="Fim"
        placeholder="Data de Fim"
        type="datetime-local"
        registration={register("endDate")}
        error={errors?.endDate?.message}
      />

      <FieldsetInput
        label="Título"
        placeholder="Ex: Aula de matemática TRUE"
        type="text"
        registration={register("title")}
        error={errors?.title?.message}
      />

      <FieldsetInput
        label="Preço (receita)"
        placeholder="Preço (receita)"
        type="number"
        registration={register("price", {
          valueAsNumber: true,
        })}
        error={errors?.price?.message}
      />

      <FieldsetInput
        label="Pagamento (custo)"
        placeholder="Pagamento (custo)"
        type="number"
        registration={register("payment", {
          valueAsNumber: true,
        })}
        error={errors?.payment?.message}
      />

      <FieldsetInput
        label="Descrição (opcional)"
        placeholder="Observações do atendimento"
        registration={register("description")}
        error={errors?.description?.message}
        type="text"
        className="textarea"
      />
    </div>
  );
}

export function Actions({
  isSubmitting,
  cancelTo,
  submitLabel,
  submittingLabel,
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
