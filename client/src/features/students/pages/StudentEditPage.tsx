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
import { StudentPageState } from "../components/StudentPageState";
import type { StudentInputSchema } from "../hooks/studentSchema";
import { useStudentForm } from "../hooks/use-student-form";
import { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { PageError } from "@/components/ui/page-error";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentFetchError,
    refetch: refetchStudent,
  } = useGetStudentById(studentId);

  const {
    mutate: updateStudent,
    error: studantUpdateError,
    isPending: isStudentUpdating,
  } = useUpdateStudent();

  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm(student);

  const onSubmit = handleSubmit((data: StudentInputSchema) => {
    updateStudent({ studentId, data });
  });

  if (isStudentLoading) {
    return <PageLoading message="Carregando aluno..." />;
  }

  //TODO: revisitar esse comportamento de erro, ele vai servir como base
// Atualmente a página renderiza por inteiro, as vezes é melhor colocar o erro embaixo do formulário
  if (studentFetchError) {
    return <PageError message="Ocorreu um erro ao carregar o aluno." error={studentFetchError} />;
  }

  return (
    <div className="container">
      <PageHeader
        title="Editar aluno"
        description="Atualize os dados do aluno e do responsável."
        action={
          <ButtonLink to={`/students/${studentId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <StudentForm onSubmit={onSubmit}>
        <StudentFormFields
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-3 gap-4"
        />

        <ParentDetailsForm
          prefix="parent"
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-2 gap-4"
        />

        <AddressDetailsForm
          prefix="address"
          register={register}
          errors={errors}
          className="grid grid-cols-3 gap-4"
        />

        {studantUpdateError ? (
          <Alert variant="error" error={studantUpdateError?.message} />
        ) : null}

        <StudentEditActions
          studentId={studentId}
          isSubmitting={isStudentUpdating}
        />
      </StudentForm>
    </div>
  );
}
