import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentFormSchema, type StudentFormInput } from "../schemas/studentFormSchema";
import type { StudentResponseDTOSchema } from "@/kubb";

const mock = {
  name: "Felipe Barbosa",
  birthdate: "2012-09-20",
  cpf: "990.714.990-06",
  school: "Escola A",
  contact: "(61) 96242-8200",
  email: "felipe.barbosa@38estudante.com",
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
    email: "marcia.barbosa@735email.com",
    contact: "(61) 99111-2201",
    cpf: "123.456.789-01",
  },
};
export function useStudentForm(student?: StudentResponseDTOSchema) {

  const form = useForm<StudentFormInput>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? "",
      birthdate: student?.birthdate ?? "",
      cpf: student?.cpf ?? "",
      contact: student?.contact ?? "",
      email: student?.email ?? "",
      school: student?.school ?? "",
      address: {
        street: student?.address?.street ?? "",
        number: student?.address?.number ?? "",
        complement: student?.address?.complement ?? "",
        district: student?.address?.district ?? "",
        city: student?.address?.city ?? "",
        state: student?.address?.state ?? "DF",
        zip: student?.address?.zip ?? "",
      },
      parent: {
        name: student?.parent?.name ?? "",
        email: student?.parent?.email ?? "",
        contact: student?.parent?.contact ?? "",
        cpf: student?.parent?.cpf ?? "",
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
