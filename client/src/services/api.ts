import {
  employeeApiSchema,
  employeeResponseSchema,
  employeeOptionSchema,
  parentResponseSchema,
  studentResponse,
  studentOptionSchema,
  eventResponse,
  eventInputSchema,
} from "@/lib/schemas"
import type {
  ParentFormInput,
  EmployeeFormInput,
  EventFormInput,
  StudentFormInput,
  EmployeeApiResponse,
  EmployeeResponse,
  EmployeeOption,
  EventResponse,
  ParentResponse,
  StudentResponse,
  StudentOption,
} from "@/lib/schemas"
import { pageResponseSchema, type PageResponse } from "@/lib/schemas/page-response"
import axios from "axios"
import { ZodError } from "zod"

type ApiErrorResponse = {
  status?: number
  error?: string
  code?: string
  message?: string
  path?: string
  timestamp?: string
}

function getApiErrorMessage(data: unknown) {
  if (!data || typeof data !== "object") {
    return null
  }

  const apiError = data as ApiErrorResponse
  return typeof apiError.message === "string" && apiError.message.trim() ? apiError.message : null
}

export function getFriendlyErrorMessage(error: unknown) {
  if (!error) return ""

  if (error instanceof ZodError) {
    console.error("Schema validation error:", error.issues)
    return "Resposta da API em formato inesperado. Atualize a página ou contate o suporte."
  }

  if (axios.isAxiosError(error)) {
    const status = error.response?.status
    const apiMessage = getApiErrorMessage(error.response?.data)

    if (!status) {
      return "Não foi possível conectar ao servidor. Verifique se a API está rodando e tente novamente."
    }

    if (status === 400) return apiMessage ?? "Dados inválidos. Revise as informações e tente novamente."
    if (status === 404) return apiMessage ?? "Não encontramos o recurso solicitado."
    if (status === 409) return apiMessage ?? "Conflito de dados. Verifique se já existe um registro com essas informações."
    if (status >= 500) return "Erro no servidor. Tente novamente em instantes."

    return "Não foi possível concluir a solicitação. Tente novamente."
  }

  return "Ocorreu um erro inesperado. Tente novamente."
}

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080"

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error.response?.data || error.message)
    return Promise.reject(error)
  }
)

export const studentsApi = {
  async list(page = 0, size = 20, sortBy = "name",): Promise<PageResponse<StudentResponse>> {
    const { data } = await api.get<PageResponse<StudentResponse>>(`/v1/students?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(studentResponse).parse(data);
  },
  async getOptions(): Promise<StudentOption[]> {
    const { data } = await api.get<StudentOption[]>("/v1/students/options")
    return studentOptionSchema.array().parse(data)
  },
  async listByParent(id: string): Promise<StudentResponse[]> {
    const { data } = await api.get<StudentResponse[]>(`/v1/students/parent/${id}`)
    return studentResponse.array().parse(data);
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

export const employeesApi = {
  async list(page = 0, size = 20, sortBy = "name",): Promise<PageResponse<EmployeeResponse>> {
    const { data } = await api.get<PageResponse<EmployeeResponse>>(`/v1/employees?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(employeeResponseSchema).parse(data);
  },
  async getOptions(): Promise<EmployeeOption[]> {
    const { data } = await api.get<EmployeeOption[]>("/v1/employees/options")
    return employeeOptionSchema.array().parse(data)
  },
  async getById(id: string): Promise<EmployeeResponse> {
    const { data } = await api.get<EmployeeResponse>(`/v1/employees/${id}`)
    return employeeResponseSchema.parse(data)
  },
  async getByIdForEdit(id: string): Promise<EmployeeApiResponse> {
    const { data } = await api.get<EmployeeApiResponse>(`/v1/employees/${id}`)
    return employeeApiSchema.parse(data)
  },
  async create(input: EmployeeFormInput): Promise<EmployeeResponse> {
    const { data } = await api.post<EmployeeResponse>("/v1/employees", input)
    return employeeResponseSchema.parse(data)
  },
  async update(id: string, input: EmployeeFormInput): Promise<EmployeeResponse> {
    const { data } = await api.patch<EmployeeResponse>(`/v1/employees/${id}`, input)
    return employeeResponseSchema.parse(data)
  },
  delete: (id: string) => api.delete(`/v1/employees/${id}`),
  archive: (id: string) => api.patch(`/v1/employees/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/employees/${id}/unarchive`),
}

export const parentsApi = {
  async list(): Promise<ParentResponse[]> {
    const { data } = await api.get<ParentResponse[]>(`/v1/parents/all`)
    return parentResponseSchema.array().parse(data);
  },
  async listPaginated(page = 0, size = 20, sortBy = "name",): Promise<PageResponse<ParentResponse>> {
    const { data } = await api.get<PageResponse<ParentResponse>>(`/v1/parents?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(parentResponseSchema).parse(data);
  },
  async getById(id: string): Promise<ParentResponse> {
    const { data } = await api.get<ParentResponse>(`/v1/parents/${id}`)
    return parentResponseSchema.parse(data)
  },
  async create(input: ParentFormInput): Promise<ParentResponse> {
    const { data } = await api.post<ParentResponse>("/v1/parents", input)
    return parentResponseSchema.parse(data)
  },
  async update(id: string, input: ParentFormInput): Promise<ParentResponse> {
    const { data } = await api.put<ParentResponse>(`/v1/parents/${id}`, input)
    return parentResponseSchema.parse(data)
  },
  archive: (id: string) => api.patch(`/v1/parents/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/parents/${id}/unarchive`),
  delete: (id: string) => api.delete(`/v1/parents/${id}`),
}

//TODO Implementar arquivamento e desarquivamento de eventos quando o backend expor esse suporte.
export const eventsApi = {
  async list(page = 0, size = 20, sortBy = "startDate",): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponse).parse(data);
  },

  async listByStudent(id: string, page = 0, size = 20, sortBy = "startDate"): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events/student/${id}?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponse).parse(data);
  },

  async listByEmployee(id: string, page = 0, size = 20, sortBy = "startDate"): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events/employee/${id}?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponse).parse(data);
  },
  async getById(id: string): Promise<EventResponse> {
    const { data } = await api.get<EventResponse>(`/v1/events/${id}`)
    return eventResponse.parse(data)
  },
  async create(input: EventFormInput): Promise<EventResponse> {
    const payload = eventInputSchema.parse(input)
    const { data } = await api.post<EventResponse>("/v1/events", payload)
    return eventResponse.parse(data)
  },
  async update(id: string, input: EventFormInput): Promise<EventResponse> {
    const payload = eventInputSchema.parse(input)
    const { data } = await api.put<EventResponse>(`/v1/events/${id}`, payload)
    return eventResponse.parse(data)
  },

  delete: (id: string) => api.delete(`/v1/events/${id}`),
  archive: (id: string) => api.patch(`/v1/events/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/events/${id}/unarchive`),
}
