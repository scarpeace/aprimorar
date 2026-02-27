import axios from "axios"

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
  list: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/students?page=${page}&size=${size}&sortBy=${sortBy}`),
  listActive: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/students/active?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: string) => api.get(`/v1/students/${id}`),
  create: (data: unknown) => api.post("/v1/students", data),
  update: (id: string, data: unknown) => api.patch(`/v1/students/${id}`, data),
  delete: (id: string) => api.delete(`/v1/students/${id}`),
}

export const employeesApi = {
  list: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/employees?page=${page}&size=${size}&sortBy=${sortBy}`),
  listActive: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/employees/active?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: string) => api.get(`/v1/employees/${id}`),
  create: (data: unknown) => api.post("/v1/employees", data),
  update: (id: string, data: unknown) => api.patch(`/v1/employees/${id}`, data),
  delete: (id: string) => api.delete(`/v1/employees/${id}`),
}

export const eventsApi = {
  list: (page = 0, size = 20, sortBy = "startDateTime") =>
    api.get(`/v1/events?page=${page}&size=${size}&sortBy=${sortBy}`),
  getById: (id: number) => api.get(`/v1/events/${id}`),
  create: (data: unknown) => api.post("/v1/events", data),
  update: (id: number, data: unknown) => api.patch(`/v1/events/${id}`, data),
  delete: (id: number) => api.delete(`/v1/events/${id}`),
}

export const parentsApi = {
  listActive: (page = 0, size = 20, sortBy = "name") =>
    api.get(`/v1/parents/active?page=${page}&size=${size}&sortBy=${sortBy}`),
}
