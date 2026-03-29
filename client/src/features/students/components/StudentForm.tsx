import type { StudentInputSchema } from "@/features/students/hooks/studentSchema";
import type { ReactNode } from "react";
import type { FormSubmitHandler } from "react-hook-form";

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
