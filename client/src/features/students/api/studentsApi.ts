import { api } from "@/services/api"
import { pageResponseSchema, type PageResponse } from "@/lib/schemas/page-response"
import {
  studentResponse,
  studentOptionSchema,
  type StudentFormInput,
  type StudentOption,
  type StudentResponse,
} from "@/lib/schemas/student"

export const studentsApi = {
  async list(
    page = 0,
    size = 50,
    sortBy = "name",
    search?: string
  ): Promise<PageResponse<StudentResponse>> {
    const searchParam = search ? `&search=${encodeURIComponent(search)}` : ""
    const { data } = await api.get<PageResponse<StudentResponse>>(
      `/v1/students?page=${page}&size=${size}&sort=${sortBy}${searchParam}`
    )
    return pageResponseSchema(studentResponse).parse(data)
  },

  async getOptions(): Promise<StudentOption[]> {
    const { data } = await api.get<StudentOption[]>("/v1/students/options")
    return studentOptionSchema.array().parse(data)
  },

  async listByParent(id: string): Promise<StudentResponse[]> {
    const { data } = await api.get<StudentResponse[]>(`/v1/students/parent/${id}`)
    return studentResponse.array().parse(data)
  },

  async getById(id: string): Promise<StudentResponse> {
    const { data } = await api.get<StudentResponse>(`/v1/students/${id}`)
    return studentResponse.parse(data)
  },

  async create(input: StudentFormInput): Promise<StudentResponse> {
    const { data } = await api.post<StudentResponse>("/v1/students", input)
    return studentResponse.parse(data)
  },

  async update(id: string, input: StudentFormInput): Promise<StudentResponse> {
    const { data } = await api.put<StudentResponse>(`/v1/students/${id}`, input)
    return studentResponse.parse(data)
  },

  delete: (id: string) => api.delete(`/v1/students/${id}`),
  archive: (id: string) => api.patch(`/v1/students/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/students/${id}/unarchive`),
}
