import type { ReactNode } from "react";

type EmployeeFormProps = {
  onSubmit: React.SubmitEventHandler<HTMLFormElement>;
  children: ReactNode;
};

export function EmployeeForm({
  onSubmit,
  children,
}: Readonly<EmployeeFormProps>) {
  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit}>
      {children}
    </form>
  );
}
