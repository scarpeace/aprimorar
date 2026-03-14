import {
  employeeResponseSchema,
  eventResponseSchema,
  parentResponseSchema,
  studentResponseSchema,
} from "@/lib/schemas"
import type {
  CreateEmployeeInput,
  CreateEventInput,
  CreateStudentInput,
  EmployeeResponse,
  EventResponse,
  ParentResponse,
  StudentResponse,
} from "@/lib/schemas"
import { pageResponseSchema, type PageResponse } from "@/lib/schemas/page-response"
import axios from "axios"

export function getFriendlyErrorMessage(error: unknown) {
  if (axios.isAxiosError(error)) {
    const status = error.response?.status

    if (!status) {
      return "Não foi possível conectar ao servidor. Verifique se a API está rodando e tente novamente."
    }

    if (status === 400) return "Dados inválidos. Revise as informações e tente novamente."
    if (status === 404) return "Não encontramos o recurso solicitado."
    if (status === 409) return "Conflito de dados. Verifique se já existe um registro com essas informações."
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
    return pageResponseSchema(studentResponseSchema).parse(data);
  },
  async getById(id: string): Promise<StudentResponse> {
    const { data } = await api.get<StudentResponse>(`/v1/students/${id}`)
    return studentResponseSchema.parse(data)
  },
  async create(input: CreateStudentInput): Promise<StudentResponse> {
    const { data } = await api.post<StudentResponse>("/v1/students", input)
    return studentResponseSchema.parse(data)
  },
  async update(id: string, input: CreateStudentInput): Promise<StudentResponse> {
    const { data } = await api.put<StudentResponse>(`/v1/students/${id}`, input)
    return studentResponseSchema.parse(data)
  },
  archive: (id: string) => api.patch(`/v1/students/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/students/${id}/unarchive`),
}

export const employeesApi = {
  async list(page = 0, size = 20, sortBy = "name",): Promise<PageResponse<EmployeeResponse>> {
    const { data } = await api.get<PageResponse<EmployeeResponse>>(`/v1/employees?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(employeeResponseSchema).parse(data);
  },
  async getById(id: string): Promise<EmployeeResponse> {
    const { data } = await api.get<EmployeeResponse>(`/v1/employees/${id}`)
    return employeeResponseSchema.parse(data)
  },
  async create(input: CreateEmployeeInput): Promise<EmployeeResponse> {
    const { data } = await api.post<EmployeeResponse>("/v1/employees", input)
    return employeeResponseSchema.parse(data)
  },
  async update(id: string, input: CreateEmployeeInput): Promise<EmployeeResponse> {
    const { data } = await api.put<EmployeeResponse>(`/v1/employees/${id}`, input)
    return employeeResponseSchema.parse(data)
  },
  archive: (id: string) => api.patch(`/v1/employees/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/employees/${id}/unarchive`),
}

export const parentsApi = {
  async list(page = 0, size = 20, sortBy = "name",): Promise<PageResponse<ParentResponse>> {
    const { data } = await api.get<PageResponse<ParentResponse>>(`/v1/parents?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(parentResponseSchema).parse(data);
  },
  async getById(id: string): Promise<ParentResponse> {
    const { data } = await api.get<ParentResponse>(`/v1/parents/${id}`)
    return parentResponseSchema.parse(data)
  },
  async create(input: CreateEmployeeInput): Promise<ParentResponse> {
    const { data } = await api.post<ParentResponse>("/v1/parents", input)
    return parentResponseSchema.parse(data)
  },
  async update(id: string, input: CreateEmployeeInput): Promise<ParentResponse> {
    const { data } = await api.put<ParentResponse>(`/v1/parents/${id}`, input)
    return data
  },
  archive: (id: string) => api.patch(`/v1/parents/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/parents/${id}/unarchive`),
}

export const eventsApi = {
  async list(page = 0, size = 20, sortBy = "startDate",): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponseSchema).parse(data);
  },

  async listByStudent(id: string, page = 0, size = 20, sortBy = "startDate"): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events/student/${id}?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponseSchema).parse(data);
  },

  async listByEmployee(id: string, page = 0, size = 20, sortBy = "startDate"): Promise<PageResponse<EventResponse>> {
    const { data } = await api.get<PageResponse<EventResponse>>(`/v1/events/employee/${id}?page=${page}&size=${size}&sort=${sortBy}`)
    return pageResponseSchema(eventResponseSchema).parse(data);
  },
  async getById(id: string): Promise<EventResponse> {
    const { data } = await api.get<EventResponse>(`/v1/events/${id}`)
    return eventResponseSchema.parse(data)
  },
  async create(input: CreateEventInput): Promise<EventResponse> {
    const { data } = await api.post<EventResponse>("/v1/events", input)
    return eventResponseSchema.parse(data)
  },

  async update(id: string, input: CreateEventInput): Promise<EventResponse> {
    const { data } = await api.put<EventResponse>(`/v1/events/${id}`, input)
    return eventResponseSchema.parse(data)
  },

  archive: (id: string) => api.patch(`/v1/events/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/events/${id}/unarchive`),
}
