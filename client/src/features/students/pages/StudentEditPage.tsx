import { zodResolver } from "@hookform/resolvers/zod";

import { PageLayout } from "@/components/layout/PageLayout";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetStudentById } from "@/kubb";
import { GraduationCap, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";

import { SectionCard } from "@/components/ui/section-card";
import { ParentSelectDropdown } from "@/features/parents/components/ParentSelectDropdown";
import { brazilianStates } from "@/lib/utils/brazilianStates";
import { useHookFormMask } from "use-mask-input";
import {
  type StudentFormSchema,
  studentFormSchema,
} from "../forms/studentFormSchema";
import { useStudentMutations } from "../hooks/student-mutations";

export function StudentEditPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";

  const studentQuery = useGetStudentById(studentId);
  const { updateStudent } = useStudentMutations();

  const { register, control, handleSubmit, formState: { errors } } = useForm<StudentFormSchema>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
    values: {
      name: studentQuery.data?.name ?? "",
      cpf: studentQuery.data?.cpf ?? "",
      birthdate: studentQuery.data?.birthdate ?? "",
      contact: studentQuery.data?.contact ?? "",
      email: studentQuery.data?.email ?? "",
      school: studentQuery.data?.school ?? "",
      parentId: studentQuery.data?.parentId ?? "",
      address: {
        street: studentQuery.data?.address.street ?? "",
        number: studentQuery.data?.address.number ?? "",
        complement: studentQuery.data?.address.complement ?? "N/A",
        district: studentQuery.data?.address.district ?? "",
        city: studentQuery.data?.address.city ?? "",
        state: studentQuery.data?.address.state ?? "DF",
        zip: studentQuery.data?.address.zip ?? "",
      },
    },
  });
  const registerWithMask = useHookFormMask(register);

  const onSubmit = handleSubmit((data: StudentFormSchema) => {
    updateStudent.mutate({studentId, data });
  });

  const headerProps = {
    title: "Editar aluno",
    description: "Preencha os dados do aluno e do responsável.",
    Icon: GraduationCap,
    backLink: `/students/${studentId}`,
  };

  if (studentQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do aluno"
          error={studentQuery.error}
        />
      </PageLayout>
    );
  }

  if (studentQuery.isPending || !studentQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do aluno" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <SectionCard title={"Cadastre um novo aluno"} description={"Informe os dados do aluno e do selecione um responsável."}>

      <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
            <ParentSelectDropdown className="col-span-3" label="Responsável" control={control} error={errors.parentId?.message} />

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Nome</legend>
            <input type="text" className="input" {...register("name")} placeholder="Nome Completo"/>
            {errors?.name && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.name.message}</p>)}
          </fieldset>

           <fieldset className="fieldset">
            <legend className="fieldset-legend">Data de Nascimento</legend>
            <input type="date" className="input" {...register("birthdate")} placeholder="Ex: 01/01/1990"/>
            {errors?.birthdate && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.birthdate.message}</p>)}
          </fieldset>

           <fieldset className="fieldset">
            <legend className="fieldset-legend">CPF</legend>
              <input type="text" className="input"
                placeholder="Ex: 123.456.789-00"
                {...registerWithMask("cpf", ["###.###.###-##"])}
              />
            {errors?.cpf && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.cpf.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Contato</legend>
              <input type="text" className="input"
                placeholder="Ex: (61) 99633-2332"
                {...registerWithMask("contact", ["(##) #####-####", "(##) ####-####"])}
              />
            {errors?.contact && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.contact.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Email</legend>
            <input type="text" className="input" {...register("email")} placeholder="email@email.com"/>
            {errors?.email && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.email.message}</p>)}
          </fieldset>

          <fieldset className="fieldset">
            <legend className="fieldset-legend">Escola</legend>
            <input type="text" className="input" {...register("school")} placeholder="Ex: Leonardo da Vinci"/>
            {errors?.school && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.school.message}</p>)}
          </fieldset>

            <div className="divider col-span-3 m-0" />

            <fieldset className="fieldset">
            <legend className="fieldset-legend">Rua</legend>
            <input type="text" className="input" {...register("address.street")} placeholder="Ex: SQS 406, Bloco C"/>
            {errors?.address?.street && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.street.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
            <legend className="fieldset-legend">Bairro</legend>
            <input type="text" className="input" {...register("address.district")} placeholder="Ex: Asa sul"/>
            {errors?.address?.district && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.district.message}</p>)}
            </fieldset>

             <fieldset className="fieldset">
            <legend className="fieldset-legend">Complemento</legend>
            <input type="text" className="input" {...register("address.complement")} placeholder="Ex: Apto 101"/>
            {errors?.address?.complement && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.complement.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
            <legend className="fieldset-legend">Cidade</legend>
            <input type="text" className="input" {...register("address.city")} placeholder="Ex: Brasília"/>
            {errors?.address?.city && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.city.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
            <legend className="fieldset-legend">Estado</legend>
             <select className="select select-bordered w-full" {...register("address.state")}>
                {Object.values(brazilianStates).map((content) => (
                    <option key={content} value={content}>
                        {content}
                    </option>
                ))}
            </select>
            {errors?.address?.state && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.state.message}</p>)}
            </fieldset>

            <fieldset className="fieldset">
            <legend className="fieldset-legend">CEP</legend>
            <input type="text" className="input" {...register("address.zip")} placeholder="Ex: 70254-010"/>
            {errors?.address?.zip && (<p className="label text-error"><TriangleAlert className="w-3 h-3" />{errors.address.zip.message}</p>)}
            </fieldset>
        </div>

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to={`/students/${studentId}`} variant="outline">Cancelar</ButtonLink>
            <Button type="submit" disabled={updateStudent.isPending} variant="primary">
              {updateStudent.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
