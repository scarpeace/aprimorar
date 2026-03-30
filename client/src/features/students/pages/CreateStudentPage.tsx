import { Alert } from "@/components/ui/alert";
import { PageHeader } from "@/components/ui/page-header";
import { AddressDetailsForm } from "@/features/address/AddressDetailsForm";
import { ParentDetailsForm } from "@/features/parents/components/ParentDetailsForm";
import { StudentForm } from "../components/StudentForm";
import { StudentFormActions } from "../components/StudentFormActions";
import { StudentFormFields } from "../components/StudentFormFields";
import { useStudentForm } from "../hooks/use-student-form";
import { useCreateStudentMutation } from "../hooks/use-student-mutation";
import { GraduationCap } from "lucide-react";

export function StudentCreatePage() {
  const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
  } = useStudentForm();

  const createStudentMutation = useCreateStudentMutation();

  const onSubmit = handleSubmit((data) => {
    console.log(data)
    createStudentMutation.mutate({ data });
  });

  return (
    <div className="container">
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
      />

      <StudentForm onSubmit={onSubmit}>
        {createStudentMutation.isError && <Alert error={createStudentMutation.error} variant="error" />}

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
          registerWithMask={registerWithMask}
          errors={errors.address}
          prefix="address"
          className="grid grid-cols-3 gap-4"
        />

        <StudentFormActions isSubmitting={createStudentMutation.isPending} />
      </StudentForm>
    </div>
  );
}
