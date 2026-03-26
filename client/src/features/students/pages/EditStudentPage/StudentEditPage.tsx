import { Alert } from "@/components/ui/alert";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { useParams } from "react-router-dom";
import { useStudentById } from "../../hooks/studentQueries";
import { StudentEditPageState } from "./components/StudentEditPageState";
import { StudentInfoSection } from "./components/StudentDetailsForm";
import { AddressInfoSection } from "../../../address/AddressEditForm";
import { ParentDetailsForm } from "./components/ParentDetailsForm";
import { useEffect } from "react";
import { StudentEditActions } from "./components/StudentEditActions";
import { useStudentEditForm } from "../../hooks/studentEditForm";
import type { StudentFormInput } from "../../schemas/studentFormSchema";
import { useUpdateStudentMutation } from "../../hooks/studentMutations";
import { StudentEditForm } from "./components/StudentEditForm";

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
    mutate: updateStudent,
    error: updateStudentError,
    isPending: isStudentUpdating,
  } = useUpdateStudentMutation();

  const {
    formState: { errors },
    selectedParentId: formSelectedParentId,
    setValue,
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentEditForm(student);

  //TODO: isso aqui pode virar um watch do form?
  useEffect(() => {
    if (!student) return;
    // setSelectedParentId(formSelectedParentId);
    setValue("parentId", student.parent.parentId);
  }, [student, setValue]);

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

      <StudentEditPageState
        studentId={studentId}
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onRetry={refetchStudent}
      >
        <StudentEditForm onSubmit={onSubmit}>
          <ParentDetailsForm
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

          <AddressInfoSection
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
