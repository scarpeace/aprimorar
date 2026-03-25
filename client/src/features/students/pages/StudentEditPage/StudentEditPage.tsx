import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { useParams } from "react-router-dom";
import { useStudentById } from "../../hooks/studentQueries";
import { useUpdateStudentMutation } from "../../hooks/studentMutations";
import type { UpdateStudentMutationRequestSchema } from "@/kubb";
import { StudentEditState } from "../../components/StudentEdit/StudentEditState";
import { StudentEditForm } from "../../components/StudentEdit/StudentEditForm";
import { StudentInfoSection } from "../../components/StudentEdit/StudentInfoSection";
import { AddressInfoSection } from "../../components/StudentEdit/AddressInfoSection";
import { StudentResponsibleSection } from "../../components/StudentEdit/StudentResponsibleSection";
import { useEffect } from "react";
import { StudentEditActions } from "../../components/StudentEdit/StudentEditActions";
import { useStudentEditForm } from "../../hooks/studentEditForm";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  // const [selectedParentId, setSelectedParentId] = useState("");
  const studentId = id ?? "";

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
    refetch: refetchStudent,
  } = useStudentById(studentId);

  const {
    formState: { errors },
    selectedParentId: formSelectedParentId,
    setValue,
    handleSubmit,
    register,
    registerWithMask,
    watch,
  } = useStudentEditForm(student);

  useEffect(() => {
    if (!student) return;
    // setSelectedParentId(formSelectedParentId);
    setValue("parentId", student.parent.parentId)
  }, [student, setValue]);


  const {
    mutate: updateStudent,
    isPending: isUpdatingStudent,
    error: updateStudentError,
  } = useUpdateStudentMutation();

  const onSubmit = handleSubmit((data: UpdateStudentMutationRequestSchema) => {
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

      <StudentEditState
        studentId={studentId}
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
      >
        <StudentEditForm onSubmit={onSubmit}>
          <StudentResponsibleSection
            selectedParentId={formSelectedParentId}
            register={register}
            setValue={setValue}
            errors={errors}
            className="grid grid-cols-1 gap-4"
          />

          <StudentInfoSection
            register={register}
            registerWithMask={registerWithMask}
            errors={errors}
            className="grid grid-cols-3 gap-4"
          />

          <AddressInfoSection register={register} errors={errors} />

          <StudentEditActions studentId={studentId} isSubmitting={isUpdatingStudent} />

          {updateStudentError ? (
            <Alert
              variant="error"
              message={updateStudentError.message}
            />
          ) : null}
        </StudentEditForm>
      </StudentEditState>
    </div>
  );
}
