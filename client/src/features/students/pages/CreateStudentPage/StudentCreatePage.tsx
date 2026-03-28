import { Alert } from "@/components/ui/alert";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import {
  getStudentsQueryKey,
  updateStudentMutationKey,
  useCreateStudent,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { useStudentForm } from "../../hooks/use-student-form";
import type { StudentFormInput } from "../../schemas/studentFormSchema";
import { StudentDetailsForm } from "../EditStudentPage/components/StudentDetailsForm";
import { StudentEditForm } from "../EditStudentPage/components/StudentEditForm";


export function StudentCreatePage() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const {
    mutate: createStudent,
    error: createStudentError,
    isPending: isStudentCreating,
  } = useCreateStudent({
    mutation: {
      onSuccess: (createdStudent) => {
        console.log(createdStudent);
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({ queryKey: updateStudentMutationKey() });
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        navigate(`/students/${createdStudent.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm();

  const onSubmit = handleSubmit((data: StudentFormInput) => {
    createStudent({ data });
  });

  return (
    <div className="container">
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
      />

      {/*<StudentEditPageState
        studentId={studentId}
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
      >*/}

      <StudentEditForm onSubmit={onSubmit}>
        {/*<ParentSelectDropdown
            selectedParentId={formSelectedParentId}
            register={register}
            setValue={setValue}
            errors={errors}
            className="grid grid-cols-1 gap-4"
          />*/}

        <StudentDetailsForm
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-3 gap-4"
        />

        <ParentDetailsForm
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-2 gap-4"
        />

        <AddressDetailsForm
          register={register}
          className="grid grid-cols-3 gap-4"
          errors={errors}
        />

        {/*<StudentCreateActions
            studentId={studentId}
            isSubmitting={isStudentCreating}
          />*/}

        {createStudentError ? (
          <Alert variant="error" message={createStudentError.message} />
        ) : null}
      </StudentEditForm>
      {/*</StudentEditPageState>*/}
    </div>
  );
}
