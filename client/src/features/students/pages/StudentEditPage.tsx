import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import { useGetStudentById, useUpdateStudent } from "@/kubb";
import { useParams } from "react-router-dom";
import { StudentEditActions } from "../components/StudentEditActions";
import { StudentForm } from "../components/StudentForm";
import { StudentFormFields } from "../components/StudentFormFields";
import type { StudentInputSchema } from "../hooks/studentSchema";
import { useStudentForm } from "../hooks/use-student-form";
import { PageLoading } from "@/components/ui/page-loading";
import { PageError } from "@/components/ui/page-error";
import { ErrorCard } from "@/components/ui/error-card";
import { GraduationCap } from "lucide-react";
import { useUpdateStudentMutation } from "../hooks/use-student-mutation";
import { StudentFormActions } from "../components/StudentFormActions";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";

  const studentQuery = useGetStudentById(studentId);
  const updateStudentMutation = useUpdateStudentMutation();

  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm(studentQuery.data);

  const onSubmit = handleSubmit((data: StudentInputSchema) => {
    updateStudentMutation.mutate({ studentId, data });
  });

  if (studentQuery.isPending) {
    return <PageLoading message="Carregando aluno..." />;
  }

if (studentQuery.isError) {
  return <ErrorCard title="Ocorreu um erro ao carregar o aluno." error={studentQuery.error} />
}

  return (
    <div className="container">
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
      />

      <StudentForm onSubmit={onSubmit}>
          {updateStudentMutation.isError && <Alert error={updateStudentMutation.error} variant="error" />}

        <StudentFormFields
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-3 gap-4"
        />

        <ParentDetailsForm
          register={register}
          registerWithMask={registerWithMask}
          prefix="parent"
          errors={errors.parent}
          className="grid grid-cols-2 gap-4"
        />

        <AddressDetailsForm
          register={register}
          registerWithMask={registerWithMask}
          errors={errors.address}
          prefix="address"
          className="grid grid-cols-3 gap-4"
        />

        <StudentFormActions isSubmitting={updateStudentMutation.isPending} />
      </StudentForm>
    </div>
  );
}
