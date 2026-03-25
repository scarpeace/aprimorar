import type { ReactNode } from "react";

type StudentEditFormProps = {
  onSubmit: (event: React.FormEvent<HTMLFormElement>) => void;
  children: ReactNode;
};

export function StudentEditForm({
  onSubmit,
  children,
}: Readonly<StudentEditFormProps>) {
  return (
    <form className={""} onSubmit={onSubmit}>
      {children}
    </form>
  );
}
