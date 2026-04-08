import { PageHeader } from "@/components/ui/page-header";
import { zodResolver } from "@hookform/resolvers/zod";
import { GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";
import * as StudentForm from "../forms/StudentForm";
import {
  type StudentFormSchema,
  studentFormSchema,
} from "../forms/studentFormSchema";
import { useStudentMutations } from "../hooks/student-mutations";

export function StudentCreatePage() {
  const form = useForm<StudentFormSchema>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
  });

  const { createStudent } = useStudentMutations();

  // const onSubmit = form.handleSubmit((data: StudentFormSchema) => {
  //   console.log("DISPAROU");
  //   createStudent.mutate({ data });
  // });

  const onSubmit = form.handleSubmit(
  (data) => console.log("valid", data),
  (errors) => console.log("invalid", errors)
);

  return (
    <>
      <PageHeader
        title="Novo Aluno"
        description="Preencha os dados do aluno no formulário abaixo."
        Icon={GraduationCap}
        backLink="/students"
      />

      <StudentForm.Root
        title="Criar aluno"
        description="Informe os dados do aluno e do selecione um responsável."
        onSubmit={onSubmit}
      >
        <StudentForm.Fields
          register={form.register}
          errors={form.formState.errors}
        />

        <StudentForm.Actions
          isSubmitting={createStudent.isPending}
          cancelTo="/students"
        />
      </StudentForm.Root>
    </>
  );
}
