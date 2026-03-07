import type {
  CreateEmployeeInput,
  CreateEventInput,
  CreateStudentInput,
  EmployeeResponse,
  EventResponse,
  ParentSummary,
  StudentResponse,
} from "@/lib/schemas"
import axios, { type AxiosResponse } from "axios"

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

export type PageResponse<T> = {
  content: T[]
  number: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

function buildQueryString(params: Record<string, string | number | boolean | undefined>) {
  const searchParams = new URLSearchParams()

  for (const [key, value] of Object.entries(params)) {
    if (value === undefined) {
      continue
    }

    searchParams.set(key, String(value))
  }

  return searchParams.toString()
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
  list: (
    page = 0,
    size = 20,
    sortBy = "name",
    includeArchived?: boolean,
    name?: string
  ): Promise<AxiosResponse<PageResponse<StudentResponse>>> => {
    const normalizedName = name?.trim()
    const queryString = buildQueryString({
      page: String(page),
      size: String(size),
      sortBy,
      includeArchived: includeArchived ? true : undefined,
      name: normalizedName || undefined,
    })

    return api.get(`/v1/students?${queryString}`)
  },
  getById: (id: string): Promise<AxiosResponse<StudentResponse>> =>
    api.get(`/v1/students/${id}`),
  create: (data: CreateStudentInput): Promise<AxiosResponse<StudentResponse>> => api.post("/v1/students", data),
  update: (id: string, data: CreateStudentInput): Promise<AxiosResponse<StudentResponse>> => api.patch(`/v1/students/${id}`, data),
  archive: (id: string) => api.delete(`/v1/students/${id}`),
  unarchive: (id: string) => api.patch(`/v1/students/${id}/unarchive`),
}

export const employeesApi = {
  list: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<EmployeeResponse>>> =>
    api.get(`/v1/employees?${buildQueryString({ page, size, sortBy })}`),
  listActive: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<EmployeeResponse>>> =>
    api.get(`/v1/employees/active?${buildQueryString({ page, size, sortBy })}`),
  getById: (id: string): Promise<AxiosResponse<EmployeeResponse>> =>
    api.get(`/v1/employees/${id}`),
  create: (data: CreateEmployeeInput): Promise<AxiosResponse<EmployeeResponse>> => api.post("/v1/employees", data),
  update: (id: string, data: CreateEmployeeInput): Promise<AxiosResponse<EmployeeResponse>> => api.patch(`/v1/employees/${id}`, data),
  delete: (id: string) => api.delete(`/v1/employees/${id}`),
}

export const eventsApi = {
  list: (page = 0, size = 20, sortBy = "startDateTime"): Promise<AxiosResponse<PageResponse<EventResponse>>> =>
    api.get(`/v1/events?${buildQueryString({ page, size, sortBy })}`),
  getById: (id: number): Promise<AxiosResponse<EventResponse>> => api.get(`/v1/events/${id}`),
  create: (data: CreateEventInput): Promise<AxiosResponse<EventResponse>> => api.post("/v1/events", data),
  update: (id: number, data: CreateEventInput): Promise<AxiosResponse<EventResponse>> => api.patch(`/v1/events/${id}`, data),
  delete: (id: number) => api.delete(`/v1/events/${id}`),
}

export const parentsApi = {
  listActive: (
    page = 0,
    size = 20,
    sortBy = "name"
  ): Promise<AxiosResponse<PageResponse<ParentSummary>>> =>
    api.get(`/v1/parents/active?${buildQueryString({ page, size, sortBy })}`),
}
