import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { useNavigate, useParams } from "react-router-dom";
import { StudentEditPageState } from "./components/StudentEditPageState";
import { StudentDetailsForm } from "./components/StudentDetailsForm";
import { StudentEditActions } from "./components/StudentEditActions";
import { useStudentForm } from "../../hooks/use-student-form";
import { StudentEditForm } from "./components/StudentEditForm";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { getStudentsQueryKey, getStudentSummaryQueryKey, updateStudentMutationKey, useGetStudentById, useUpdateStudent } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import type { StudentFormInput } from "../../schemas/studentFormSchema";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";

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
    console.log(data);
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

      <StudentEditPageState
        studentId={studentId}
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
      >
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
            className="grid grid-cols-3 gap-4"
            register={register}
            errors={errors}
          />

          <StudentEditActions
            studentId={studentId}
            isSubmitting={isStudentUpdating}
          />

          {updateStudentError ? (
            <Alert variant="error" message={updateStudentError.message} />
          ) : null}
        </StudentEditForm>
      </StudentEditPageState>
    </div>
  );
}
