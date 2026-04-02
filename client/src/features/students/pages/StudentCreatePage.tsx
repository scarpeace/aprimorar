import { Button, ButtonLink } from "@/components/ui/button";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { AddressFormFields } from "@/features/address/AddressFormFields";
import { ParentFormFields } from "@/features/parents/components/ParentFormFields";
import { GraduationCap } from "lucide-react";
import { StudentForm } from "../components/StudentForm";
import { StudentFormFields } from "../components/StudentFormFields";
import { useStudentForm } from "../hooks/use-student-form";
import { useCreateStudentMutation } from "../hooks/use-student-mutation";
import { Alert } from "@/components/ui/alert";

export function StudentCreatePage() {
  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm();

  const {
    mutate: createStudent,
    isPending: isCreateStudentPending,
    isError: isCreateStudentError,
    error: createStudentError,
  } = useCreateStudentMutation();

  const onSubmit = handleSubmit((data) => {
    createStudent({ data });
  });

  return (
    <>
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
      />

      <div className="container animate-[fade-up_300ms_ease-out_both]">
        <StudentForm onSubmit={onSubmit}>

          <StudentFormFields
            register={register}
            registerWithMask={registerWithMask}
            errors={errors}
            className="grid grid-cols-3 gap-4"
          />

          <ParentFormFields
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

           {isCreateStudentError && (
            <Alert error={createStudentError} variant="error" />
          )}

          <div className="flex justify-end flex-wrap gap-3">
            <Button type="submit" variant="success"disabled={isCreateStudentPending}>
              {isCreateStudentPending ? <LoadingSpinner text={"Salvando"} /> : "Salvar alterações"}
            </Button>

            <ButtonLink to={`/students/`} variant="outline">
              Cancelar
            </ButtonLink>
          </div>
        </StudentForm>
      </div>
    </>
  );
}
