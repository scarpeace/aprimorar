import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import { Button, ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { FormField } from "@/components/ui/form-field";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import styles from "@/features/events/EventCreatePage.module.css";

import { getFriendlyErrorMessage } from "@/lib/shared/api";
import {
  eventRequestDTOContentEnum,
  updateEventMutationRequestSchema,
  useGetEventById,
  useGetStudentOptions,
  useUpdateEvent,
  type UpdateEventMutationRequestSchema,
} from "@/kubb";
import { useEmployeeOptionsQuery } from "@/features/employees/query/useEmployeeQueries";
import { formatDateTimeLocal } from "@/lib/utils/formatter";
import { DeleteEventButton } from "./components/DeleteEventButton";
import { Alert } from "@/components/ui/alert";

export function EventEditPage() {
  const { id: eventId } = useParams<{ id: string }>();

  const {
    data: eventData,
    isLoading: isEventLoading,
    error: eventError,
    refetch: refetchEvent,
  } = useGetEventById(eventId ?? "", {
    query: { enabled: !!eventId },
  });

  const {
    mutate: updateEvent,
    isPending: isUpdateEventPending,
    error: updateEventError,
  } = useUpdateEvent();

  const {
    data: studentOptions,
    isLoading: isStudentOptionsLoading,
    refetch: refetchStudentOptions,
    error: studentOptionsError,
  } = useGetStudentOptions();
  const {
    data: employeeeOptions,
    isLoading: isEmployeeeOptionsLoading,
    refetch: refetchEmployeeOptions,
    error: employeeeOptionsError,
  } = useEmployeeOptionsQuery();

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<UpdateEventMutationRequestSchema>({
    resolver: zodResolver(updateEventMutationRequestSchema),
    mode: "onBlur",
    values: {
      title: eventData?.title ?? "",
      description: eventData?.description ?? "",
      content:
        (eventData?.content as UpdateEventMutationRequestSchema["content"]) ??
        "",
      startDate: eventData?.startDate
        ? formatDateTimeLocal(eventData.startDate)
        : "",
      endDate: eventData?.endDate ? formatDateTimeLocal(eventData.endDate) : "",
      price: eventData?.price ?? 0,
      payment: eventData?.payment ?? 0,
      studentId: eventData?.studentId ?? "",
      employeeId: eventData?.employeeId ?? "",
    },
  });
  const studentIdField = register("studentId");
  const employeeIdField = register("employeeId");

  if (!eventId) {
    return <ErrorCard description={"Evento não encontrado"} />;
  }

  const errorMessage =
    eventError || studentOptionsError || employeeeOptionsError;
  if (eventError || studentOptionsError || employeeeOptionsError) {
    return (
      <div className={styles.page}>
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

  const onSubmit = (data: UpdateEventMutationRequestSchema) => {
    updateEvent({ eventId, data });
  };

  return (
    <div className={styles.page}>
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
        <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
          <div className={styles.formGrid}>
            <FormField
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={styles.field}
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
              className={`${styles.field} ${styles.span2}`}
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

          <div className={styles.actions}>
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
