import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type UseFormReturn } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { studentInputSchema, type StudentInputSchema } from "./studentSchema";
import type { StudentResponseDTO } from "@/kubb";
import { formatCpf } from "@/lib/utils/formatter";

//TODO: testar o fluxo completo de criação de aluno depois que arrumar o parent
//TODO: em tempo, isso é necessario \/?
type UseStudentFormReturn = UseFormReturn<StudentInputSchema> & {
  errors: UseFormReturn<StudentInputSchema>["formState"]["errors"];
  registerWithMask: ReturnType<typeof useHookFormMask>;
};

export const MOCK_STUDENT_DATA = {
  name: "João Silva Oliveira",
  birthdate: "2015-05-15",
  cpf: "123.456.789-01",
  contact: "(61) 98888-7777",
  email: "joao.silva@escola.com",
  school: "Colégio Marista de Brasília",
  address: {
    street: "SQN 205 Bloco G",
    number: "402",
    complement: "Apartamento",
    district: "Asa Norte",
    city: "Brasília",
    state: "DF",
    zip: "70735-070",
  },
  parent: {
    name: "Mariana Silva Oliveira",
    contact: "(61) 99999-8888",
    email: "mariana.oliveira@email.com",
    cpf: "987.654.321-00",
  },
};


export function useStudentForm(student?: StudentResponseDTO) : UseStudentFormReturn {

  const form = useForm<StudentInputSchema>({
    resolver: zodResolver(studentInputSchema),
    mode: "onBlur",
    values: {
      name: student?.name ?? MOCK_STUDENT_DATA.name,
      birthdate: student?.birthdate ?? MOCK_STUDENT_DATA.birthdate,
      cpf: student?.cpf ?? MOCK_STUDENT_DATA.cpf,
      contact: student?.contact ?? MOCK_STUDENT_DATA.contact,
      email: student?.email ?? MOCK_STUDENT_DATA.email,
      school: student?.school ?? MOCK_STUDENT_DATA.school,
      address: {
        street: student?.address?.street ?? MOCK_STUDENT_DATA.address.street,
        number: student?.address?.number ?? MOCK_STUDENT_DATA.address.number,
        complement: student?.address?.complement ?? MOCK_STUDENT_DATA.address.complement,
        district: student?.address?.district ?? MOCK_STUDENT_DATA.address.district,
        city: student?.address?.city ?? MOCK_STUDENT_DATA.address.city,
        state: student?.address?.state ?? MOCK_STUDENT_DATA.address.state ,
        zip: student?.address?.zip ?? MOCK_STUDENT_DATA.address.zip,
      },
      parent: {
        name: student?.parent?.name ?? MOCK_STUDENT_DATA.parent.name,
        contact: student?.parent?.contact ?? MOCK_STUDENT_DATA.parent.contact,
        email: student?.parent?.email ?? MOCK_STUDENT_DATA.parent.email,
        cpf: student?.parent?.cpf ?? MOCK_STUDENT_DATA.parent.cpf,
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
