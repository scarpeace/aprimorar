import { Alert } from "@/components/ui/alert";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import { StudentForm } from "../components/StudentForm";
import { StudentFormActions } from "../components/StudentFormActions";
import { StudentFormFields } from "../components/StudentFormFields";
import { StudentPageState } from "../components/StudentPageState";
import { useStudentForm } from "../hooks/use-student-form";
import { useCreateStudentMutation } from "../hooks/use-student-mutation";
import { PageLoading } from "@/components/ui/page-loading";
import { ErrorCard } from "@/components/ui/error-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

export function StudentCreatePage() {
  const {
    mutate: createStudent,
    error: createStudentError,
    isPending: isCreatingStudent,
  } = useCreateStudentMutation();

  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm();

  const onSubmit = handleSubmit((data, event) => {
    event?.preventDefault();
    createStudent({ data });
  });
  return (
    <div className="container">
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
      />

      {createStudentError ? (
        <ErrorCard
          description={getFriendlyErrorMessage(createStudentError)}
          actionLabel="Tentar novamente"
        />
      ) : null}

        <StudentForm onSubmit={onSubmit}>

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
            errors={errors.address}
            prefix="address"
            className="grid grid-cols-3 gap-4"
          />

          <StudentFormActions isSubmitting={isCreatingStudent} />

          {createStudentError ? (
            <Alert variant="error" error={createStudentError.message} />
          ) : null}
      </StudentForm>
    </div>
  );
}
