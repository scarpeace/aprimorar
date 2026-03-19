import { api } from "@/lib/shared/api";
import {
  pageResponseSchema,
  type PageResponse,
} from "@/lib/shared/page-response";
import {
  parentResponseSchema,
  type ParentFormInput,
  type ParentResponse,
} from "@/features/parents/schemas/parent";

export const parentsApi = {
  async list(): Promise<ParentResponse[]> {
    const { data } = await api.get<ParentResponse[]>("/v1/parents/all");
    return parentResponseSchema.array().parse(data);
  },

  async listPaginated(
    page = 0,
    size = 20,
    sortBy = "name",
    search?: string,
  ): Promise<PageResponse<ParentResponse>> {
    const searchParam = search ? `&search=${encodeURIComponent(search)}` : "";
    const { data } = await api.get<PageResponse<ParentResponse>>(
      `/v1/parents?page=${page}&size=${size}&sort=${sortBy}${searchParam}`,
    );
    return pageResponseSchema(parentResponseSchema).parse(data);
  },

  async getById(id: string): Promise<ParentResponse> {
    const { data } = await api.get<ParentResponse>(`/v1/parents/${id}`);
    return parentResponseSchema.parse(data);
  },

  async create(input: ParentFormInput): Promise<ParentResponse> {
    const { data } = await api.post<ParentResponse>("/v1/parents", input);
    return parentResponseSchema.parse(data);
  },

  async update(id: string, input: ParentFormInput): Promise<ParentResponse> {
    const { data } = await api.put<ParentResponse>(`/v1/parents/${id}`, input);
    return parentResponseSchema.parse(data);
  },

  archive: (id: string) => api.patch(`/v1/parents/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/parents/${id}/unarchive`),
  delete: (id: string) => api.delete(`/v1/parents/${id}`),
};
