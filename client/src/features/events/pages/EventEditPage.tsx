import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";

import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { eventContentLabels } from "@/features/events/hooks/eventContentLabels";
import { formatDateTimeLocal } from "@/lib/utils/formatter";
import { DeleteEventButton } from "../components/DeleteEventButton";
import { Alert } from "@/components/ui/alert";
import { useEventById } from "../hooks/use-event-queries";
import { useUpdateEventMutation } from "../hooks/use-event-mutations";
import { useStudentsOption } from "@/features/students/hooks/use-students-query";
import { useEmployeesOptions } from "@/features/employees/hooks/employeeQueries";

export function EventEditPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  //TODO: como observado abaixo o eventId não vai entre chaves. Padronizar isso em todos os componentes.
  const {
    data: event,
    isLoading: isEventLoading,
    error: eventError,
    refetch: refetchEvent,
  } = useEventById(eventId);

  const {
    mutate: updateEvent,
    isPending: isUpdateEventPending,
    error: updateEventError,
  } = useUpdateEventMutation();

  const {
    data: studentOptions,
    isLoading: isStudentOptionsLoading,
    refetch: refetchStudentOptions,
    error: studentOptionsError,
  } = useStudentsOption();

  const {
    data: employeeeOptions,
    isLoading: isEmployeeeOptionsLoading,
    refetch: refetchEmployeeOptions,
    error: employeeeOptionsError,
  } = useEmployeesOptions();


    const {
    formState: { errors },
    handleSubmit,
    register,
    registerWithMask,
    } = useEventForm(event);

  const studentIdField = register("studentId");
  const employeeIdField = register("employeeId");

  if (!eventId) {
    return <ErrorCard description={"Evento não encontrado"} />;
  }

  const errorMessage =
    eventError || studentOptionsError || employeeeOptionsError;
  if (eventError || studentOptionsError || employeeeOptionsError) {
    return (
      <div className="flex flex-col gap-7">
        <ErrorCard
          description={getFriendlyErrorMessage(errorMessage)}
          onAction={() =>
            Promise.all([
              refetchEvent(),
              refetchEmployeeOptions(),
              refetchStudentOptions(),
            ])
          }
        />
      </div>
    );
  }

  if (isEventLoading || isStudentOptionsLoading || isEmployeeeOptionsLoading) {
    return <PageLoading message="Carregando evento para edição..." />;
  }

  const onSubmit = (data: any) => {
    updateEvent({ eventId, data });
  };

  return (
    <div className="flex flex-col gap-7">
      <PageHeader
        title="Editar evento"
        description="Atualize os dados do atendimento."
        action={
          <ButtonLink to={`/events/${eventId}`} variant="outline">
            Voltar para detalhes
          </ButtonLink>
        }
      />

      <SectionCard
        title="Dados do evento"
        description="Atualize data, valores e participantes do atendimento."
      >
        <form className="flex flex-col gap-5" onSubmit={handleSubmit(onSubmit)}>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-5">
            <FormField
              className="flex flex-col gap-2"
              label="Título"
              htmlFor="title"
              error={errors.title?.message}
            >
              <input
                className="app-input"
                id="title"
                placeholder="Ex: Aula de matemática"
                {...register("title")}
              />
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Aluno"
              htmlFor="studentId"
              error={errors.studentId?.message}
            >
              <select
                id="studentId"
                className="app-select"
                {...studentIdField}
                onChange={(event) => {
                  studentIdField.onChange(event);
                  setValue("studentId", event.target.value, {
                    shouldValidate: true,
                  });
                }}
                defaultValue=""
              >
                <option value="" disabled>
                  Selecione um aluno
                </option>
                {studentOptions?.map((student) => (
                  <option key={student.id} value={student.id}>
                    {student.name}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Colaborador"
              htmlFor="employeeId"
              error={errors.employeeId?.message}
            >
              <select
                id="employeeId"
                className="app-select"
                {...employeeIdField}
                onChange={(event) => {
                  employeeIdField.onChange(event);
                  setValue("employeeId", event.target.value, {
                    shouldValidate: true,
                  });
                }}
                defaultValue=""
              >
                <option value="" disabled>
                  Selecione um colaborador
                </option>
                {employeeeOptions?.map((employee) => (
                  <option key={employee.id} value={employee.id}>
                    {employee.name}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Conteúdo"
              htmlFor="content"
              error={errors.content?.message}
            >
              <select
                id="content"
                className="app-select"
                {...register("content")}
                defaultValue=""
              >
                <option value="" disabled>
                  Selecione um conteúdo
                </option>
                {eventRequestDTOContentEnum.map((content) => (
                  <option key={content} value={content}>
                    {eventContentLabels[content]}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Início"
              htmlFor="startDate"
              error={errors.startDate?.message}
            >
              <input
                className="app-input"
                id="startDate"
                type="datetime-local"
                {...register("startDate")}
              />
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Fim"
              htmlFor="endDate"
              error={errors.endDate?.message}
            >
              <input
                className="app-input"
                id="endDate"
                type="datetime-local"
                {...register("endDate")}
              />
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Preço (receita)"
              htmlFor="price"
              error={errors.price?.message}
            >
              <input
                className="app-input"
                id="price"
                type="number"
                step="0.01"
                min="0"
                {...register("price", {
                  valueAsNumber: true,
                  required: "Preço é obrigatório",
                })}
              />
            </FormField>

            <FormField
              className="flex flex-col gap-2"
              label="Pagamento (custo)"
              htmlFor="payment"
              error={errors.payment?.message}
            >
              <input
                className="app-input"
                id="payment"
                type="number"
                step="0.01"
                min="0"
                {...register("payment", {
                  valueAsNumber: true,
                  required: "Pagamento é obrigatório",
                })}
              />
            </FormField>

            <FormField
              className="flex flex-col gap-2 md:col-span-2"
              label="Descrição (opcional)"
              htmlFor="description"
              error={errors.description?.message}
            >
              <textarea
                className="app-textarea"
                id="description"
                placeholder="Observações do atendimento"
                {...register("description")}
              />
            </FormField>
          </div>

          <div className="flex flex-col-reverse sm:flex-row sm:justify-end gap-3 mt-1">
            <DeleteEventButton eventId={eventId} />
            <ButtonLink to={`/events/${eventId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={isUpdateEventPending} variant="primary">
              {isUpdateEventPending ? "Salvando..." : "Salvar alterações"}
            </Button>
          </div>
        </form>
        <Alert variant="error" message={getFriendlyErrorMessage(updateEventError)} />
      </SectionCard>
    </div>
  );
}
