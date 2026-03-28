import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import { getStudentsQueryKey, updateStudentMutationKey, useGetStudentById, useUpdateStudent } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { useStudentForm } from "../../hooks/use-student-form";
import type { StudentFormInput } from "../../schemas/studentFormSchema";
import { StudentEditActions } from "./components/StudentEditActions";
import { StudentFormFields } from "./components/StudentFormFields";
import { StudentPageState } from "./components/StudentPageState";
import { StudentForm } from "./components/StudentForm";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  // const [selectedParentId, setSelectedParentId] = useState("");
  const studentId = id ?? "";
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
    refetch: refetchStudent,
  } = useGetStudentById(studentId);

  const {
    mutate: updateStudent,
    error: updateStudentError,
    isPending: isStudentUpdating,
  } = useUpdateStudent({
    mutation: {
      onSuccess: (updatedStudent) => {
        console.log(updatedStudent)
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: updateStudentMutationKey() });
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        navigate(`/students/${updatedStudent.id}`);
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
  } = useStudentForm(student);

  const onSubmit = handleSubmit((data: StudentFormInput) => {
    updateStudent({ studentId, data });
  });

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

      <StudentPageState
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
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

          <StudentEditActions
            studentId={studentId}
            isSubmitting={isStudentUpdating}
          />

          {updateStudentError ? (
            <Alert variant="error" message={updateStudentError.message} />
          ) : null}
        </StudentForm>
      </StudentPageState>
    </div>
  );
}
