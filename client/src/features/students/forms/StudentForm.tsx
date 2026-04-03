import type { ReactNode } from "react";

type StudentEditFormProps = {
  onSubmit: React.SubmitEventHandler<HTMLFormElement>;
  children: ReactNode;
};

export function StudentForm({
  onSubmit,
  children,
}: Readonly<StudentEditFormProps>) {
  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit}>
      {children}
    </form>
  );
}
