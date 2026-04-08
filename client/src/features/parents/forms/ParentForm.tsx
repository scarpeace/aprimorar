import type { ReactNode } from "react";

type ParentFormProps = {
  onSubmit: React.SubmitEventHandler<HTMLFormElement>;
  children: ReactNode;
};

export function ParentForm({
  onSubmit,
  children,
}: Readonly<ParentFormProps>) {
  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit}>
      {children}
    </form>
  );
}
