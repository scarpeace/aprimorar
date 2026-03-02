import type { CreateEmployeeInput, CreateEventInput, CreateStudentInput, EmployeeResponse, EventResponse, StudentResponse } from "@/lib/schemas"
import axios, { type AxiosResponse } from "axios"

export function getFriendlyErrorMessage(error: unknown) {
  if (axios.isAxiosError(error)) {
    const status = error.response?.status

    if (!status) {
      return "Nao foi possivel conectar ao servidor. Verifique se a API esta rodando e tente novamente."
    }

    if (status === 400) return "Dados invalidos. Revise as informacoes e tente novamente."
    if (status === 404) return "Nao encontramos o recurso solicitado."
    if (status === 409) return "Conflito de dados. Verifique se ja existe um registro com essas informacoes."
    if (status >= 500) return "Erro no servidor. Tente novamente em instantes."

    return "Nao foi possivel concluir a solicitacao. Tente novamente."
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
  list: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<StudentResponse>>> =>
    api.get(`/v1/students?page=${page}&size=${size}&sortBy=${sortBy}`),
  listActive: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<StudentResponse>>> =>
    api.get(`/v1/students/active?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: string): Promise<AxiosResponse<StudentResponse>> =>
    api.get(`/v1/students/${id}`),
  create: (data: CreateStudentInput): Promise<AxiosResponse<StudentResponse>> => api.post("/v1/students", data),
  update: (id: string, data: CreateStudentInput): Promise<AxiosResponse<StudentResponse>> => api.patch(`/v1/students/${id}`, data),
  delete: (id: string) => api.delete(`/v1/students/${id}`),
}

export const employeesApi = {
  list: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<EmployeeResponse>>> =>
    api.get(`/v1/employees?page=${page}&size=${size}&sortBy=${sortBy}`),
  listActive: (page = 0, size = 20, sortBy = "name"): Promise<AxiosResponse<PageResponse<EmployeeResponse>>> =>
    api.get(`/v1/employees/active?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: string): Promise<AxiosResponse<EmployeeResponse>> =>
    api.get(`/v1/employees/${id}`),
  create: (data: CreateEmployeeInput): Promise<AxiosResponse<EmployeeResponse>> => api.post("/v1/employees", data),
  update: (id: string, data: CreateEmployeeInput): Promise<AxiosResponse<EmployeeResponse>> => api.patch(`/v1/employees/${id}`, data),
  delete: (id: string) => api.delete(`/v1/employees/${id}`),
}

export const eventsApi = {
  list: (page = 0, size = 20, sortBy = "startDateTime"): Promise<AxiosResponse<PageResponse<EventResponse>>> =>
    api.get(`/v1/events?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: number): Promise<AxiosResponse<EventResponse>> => api.get(`/v1/events/${id}`),
  create: (data: CreateEventInput): Promise<AxiosResponse<EventResponse>> => api.post("/v1/events", data),
  update: (id: number, data: CreateEventInput): Promise<AxiosResponse<EventResponse>> => api.patch(`/v1/events/${id}`, data),
  delete: (id: number) => api.delete(`/v1/events/${id}`),
}

export const parentsApi = {
  listActive: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/parents/active?page=${page}&size=${size}&sortBy=${sortBy}`),
}
