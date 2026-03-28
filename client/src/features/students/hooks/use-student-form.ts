import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type UseFormReturn } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentInputSchema, type StudentInputSchema, type StudentResponseSchema } from "./studentSchema";

const mockStudent1 = {
  name: "Felipe Barbosa",
  birthdate: "2012-09-20",
  cpf: "086.916.300-00",
  contact: "(61) 96242-8200",
  email: "felipe.barbosa@38estudante.com",
  school: "Escola A",
  address: {
    street: "Rua das Flores",
    number: "123",
    complement: "Casa 1",
    district: "Centro",
    city: "Brasilia",
    state: "DF",
    zip: "70000-001",
  },
  parent: {
    name: "Marcia Barbosa",
    contact: "(61) 99111-2201",
    email: "marcia.barbosa@735email.com",
    cpf: "123.456.789-01",
  },
};

type UseStudentFormReturn = UseFormReturn<StudentInputSchema> & {
  errors: UseFormReturn<StudentInputSchema>["formState"]["errors"];
  registerWithMask: ReturnType<typeof useHookFormMask>;
};


export function useStudentForm(student?: StudentResponseSchema) : UseStudentFormReturn {

  const form = useForm<StudentInputSchema>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? mockStudent1.name,
      birthdate: student?.birthdate ?? mockStudent1.birthdate,
      cpf: student?.cpf ?? mockStudent1.cpf,
      contact: student?.contact ?? mockStudent1.contact,
      email: student?.email ?? mockStudent1.email,
      school: student?.school ?? mockStudent1.school,
      address: {
        street: student?.address.street ?? mockStudent1.address.street,
        number: student?.address.number ?? mockStudent1.address.number,
        complement: student?.address.complement ?? mockStudent1.address.complement,
        district: student?.address.district ?? mockStudent1.address.district,
        city: student?.address.city ?? mockStudent1.address.city,
        state: student?.address.state ?? mockStudent1.address.state,
        zip: student?.address.zip ?? mockStudent1.address.zip,
      },
      parent: {
        name: student?.parent.name ?? mockStudent1.parent.name,
        contact: student?.parent.contact ?? mockStudent1.parent.contact,
        email: student?.parent.email ?? mockStudent1.parent.email,
        cpf: student?.parent.cpf ?? mockStudent1.parent.cpf,
      },
    },
  });

  const registerWithMask = useHookFormMask(form.register);

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
  };
}
