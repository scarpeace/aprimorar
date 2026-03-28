import { Alert } from "@/components/ui/alert";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import { StudentFormActions } from "../../components/StudentFormActions";
import { useCreateStudentMutation } from "../../hooks/studentMutations";
import type { StudentInputSchema } from "../../hooks/studentSchema";
import {
  useStudentForm,
} from "../../hooks/use-student-form";
import { StudentForm } from "../EditStudentPage/components/StudentForm";
import { StudentFormFields } from "../EditStudentPage/components/StudentFormFields";
import { StudentPageState } from "../EditStudentPage/components/StudentPageState";

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

  const onSubmit = handleSubmit((data) => {
    createStudent({ data });
  });

  return (
    <div className="container">
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
      />

      <StudentPageState
        isLoading={isCreatingStudent}
        error={createStudentError}
      >
        <StudentForm onSubmit={onSubmit}>
          {/*<ParentSelectDropdown
            selectedParentId={formSelectedParentId}
            register={register}
            setValue={setValue}
            errors={errors}
            className="grid grid-cols-1 gap-4"
          />*/}

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
            <Alert variant="error" error={createStudentError} />
          ) : null}
        </StudentForm>
      </StudentPageState>
    </div>
  );
}
