import { api } from "@/lib/shared/api";
import {
  pageResponseSchema,
  type PageResponse,
} from "@/lib/shared/page-response";
import {
  eventInputSchema,
  eventResponse,
  type EventFormInput,
  type EventResponse,
} from "@/features/events/schemas/event";

export const eventsApi = {
  async list(
    page = 0,
    size = 20,
    sortBy = "startDate",
    search?: string,
  ): Promise<PageResponse<EventResponse>> {
    const searchParam = search ? `&search=${encodeURIComponent(search)}` : "";
    const { data } = await api.get<PageResponse<EventResponse>>(
      `/v1/events?page=${page}&size=${size}&sort=${sortBy}${searchParam}`,
    );
    return pageResponseSchema(eventResponse).parse(data);
  },

  async listByStudent(
    id: string,
    page = 0,
    size = 20,
    sortBy = "startDate",
  ): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(
      `/v1/events/student/${id}?page=${page}&size=${size}&sort=${sortBy}`,
    );
    return pageResponseSchema(eventResponse).parse(data);
  },

  async listByEmployee(
    id: string,
    page = 0,
    size = 20,
    sortBy = "startDate",
  ): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(
      `/v1/events/employee/${id}?page=${page}&size=${size}&sort=${sortBy}`,
    );
    return pageResponseSchema(eventResponse).parse(data);
  },

  async getById(id: string): Promise<EventResponse> {
    const { data } = await api.get<EventResponse>(`/v1/events/${id}`);
    return eventResponse.parse(data);
  },

  async create(input: EventFormInput): Promise<EventResponse> {
    const payload = eventInputSchema.parse(input);
    const { data } = await api.post<EventResponse>("/v1/events", payload);
    return eventResponse.parse(data);
  },

  async update(id: string, input: EventFormInput): Promise<EventResponse> {
    const payload = eventInputSchema.parse(input);
    const { data } = await api.put<EventResponse>(`/v1/events/${id}`, payload);
    return eventResponse.parse(data);
  },

  delete: (id: string) => api.delete(`/v1/events/${id}`),
  archive: (id: string) => api.patch(`/v1/events/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/events/${id}/unarchive`),
};
