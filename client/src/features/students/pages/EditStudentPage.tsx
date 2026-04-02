import { Alert } from "@/components/ui/alert";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { AddressFormFields } from "@/features/address/AddressFormFields";
import { ParentFormFields } from "@/features/parents/components/ParentFormFields";
import { GraduationCap } from "lucide-react";
import { useParams } from "react-router-dom";
import { StudentForm } from "../components/StudentForm";
import { StudentFormFields } from "../components/StudentFormFields";
import type { StudentUpdateSchema } from "../hooks/studentSchema";
import { useStudentForm } from "../hooks/use-student-form";
import { useUpdateStudentMutation } from "../hooks/use-student-mutation";
import { useStudentById } from "../hooks/use-students-query";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";

  const {
    data: student,
    isError: isStudentError,
    isPending: isStudentPending,
    error: studentError,
  } = useStudentById({ studentId });

  const {
    mutate: updateStudent,
    isPending: isUpdateStudentPending,
    isError: isUpdateStudentError,
    error: updateStudentError,
  } = useUpdateStudentMutation();

  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm(student);

  if (isStudentError) {
    return (
      <ErrorCard
        title="Ocorreu um erro ao carregar o aluno."
        error={studentError}
      />
    );
  }

  if (isStudentPending) {
    return <PageLoading message="Carregando aluno..." />;
  }
  const onSubmit = handleSubmit((data: StudentUpdateSchema) => {
    updateStudent({ studentId, data });
  });

  return (
    <>
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
      />

      <StudentForm onSubmit={onSubmit}>

        {isUpdateStudentError && (
          <Alert error={updateStudentError} variant="error" />
        )}

        <StudentFormFields
          isUpdate={true}
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-3 gap-4"
        />

        <ParentFormFields
          isUpdate={ true}
          register={register}
          registerWithMask={registerWithMask}
          prefix="parent"
          errors={errors.parent}
          className="grid grid-cols-2 gap-4"
        />

        <AddressFormFields
          register={register}
          registerWithMask={registerWithMask}
          errors={errors.address}
          prefix="address"
          className="grid grid-cols-3 gap-4"
        />

        <div className="flex flex-wrap justify-end gap-3">
          <Button
            type="submit"
            variant="success"
            disabled={isUpdateStudentPending}
          >
            <Button type="submit" variant="success"disabled={isUpdateStudentPending}>
              {isUpdateStudentPending ? <LoadingSpinner text={"Salvando"} /> : "Salvar alterações"}
            </Button>
          </Button>

          <ButtonLink to={`/students/`} variant="outline">
            Cancelar
          </ButtonLink>
        </div>
      </StudentForm>
    </>
  );
}
