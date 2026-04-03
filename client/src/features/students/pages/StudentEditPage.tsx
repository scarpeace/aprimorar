import { Alert } from "@/components/ui/alert";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button, ButtonLink } from "@/components/ui/button";
import { ComponentState } from "@/components/ui/component-state";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { AddressFormFields } from "@/features/address/AddressFormFields";
import { studentRequestDTOSchema } from "@/kubb";
import { GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { useHookFormMask } from "use-mask-input";
import { StudentForm } from "../components/StudentForm";
import { StudentFormFields } from "../components/StudentFormFields";
import {
  studentFormInputSchema,
  type StudentFormInputSchema
} from "../forms/studentFormSchema";
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
    register,
    formState: { errors },
    handleSubmit,
  } = useForm<StudentFormInputSchema>({
    resolver: zodResolver(studentFormInputSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? "",
      cpf: student?.cpf ?? "",
      birthdate: student?.birthdate ?? "",
      contact: student?.contact ?? "",
      email: student?.email ?? "",
      school: student?.school ?? "",
      parentId: student?.parentId ?? "",
      address: {
        street: student?.address.street ?? "",
        number: student?.address.number ?? "",
        complement: student?.address.complement ?? "N/A",
        district: student?.address.district ?? "",
        city: student?.address.city ?? "",
        state: student?.address.state ?? "DF",
        zip: student?.address.zip ?? "",
      },
    },
  });
  const registerWithMask = useHookFormMask(register);


  const onSubmit = handleSubmit((data : StudentFormInputSchema) => {
    updateStudent({ studentId, data });
  });

  if (isStudentError || isStudentPending) {
    return <ComponentState error={studentError} isPending={isStudentPending} />;
  }

  return (
    <>
      <PageHeader
        title="Criar aluno"
        description="Preencha os dados do aluno e do responsável."
        Icon={GraduationCap}
        backLink={`/students/${studentId}`}
      />

      <StudentForm onSubmit={onSubmit}>

        {isUpdateStudentError && (<Alert error={updateStudentError} variant="error" />)}

        <StudentFormFields
          isUpdate={true}
          register={register}
          registerWithMask={registerWithMask}
          errors={errors}
          className="grid grid-cols-3 gap-4"
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
              {isUpdateStudentPending ? (
                <LoadingSpinner text={"Salvando"} />
              ) : (
                "Salvar alterações"
              )}
            </Button>


          <ButtonLink to={`/students/`} variant="outline">
            Cancelar
          </ButtonLink>
        </div>
      </StudentForm>
    </>
  );
}
