import type { FormEventHandler, PropsWithChildren } from "react";
import { Button, ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import type { LucideIcon } from "lucide-react";

type HeaderProps = {
  title: string;
  description: string;
  icon: LucideIcon;
};

type FormProps = PropsWithChildren<{
  title: string;
  description: string;
  onSubmit: FormEventHandler<HTMLFormElement>;
}>;

type ActionsProps = {
  isSubmitting: boolean;
  cancelTo: string;
  submitLabel: string;
  submittingLabel: string;
};

export function Header({ title, description, icon }: HeaderProps) {
  return <PageHeader title={title} description={description} Icon={icon} />;
}

export function Form({ title, description, onSubmit, children }: FormProps) {
  return (
    <SectionCard title={title} description={description}>
      <form className="flex flex-col gap-5" onSubmit={onSubmit}>
        {children}
      </form>
    </SectionCard>
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
