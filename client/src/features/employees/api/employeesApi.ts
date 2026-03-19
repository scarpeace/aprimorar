import { api } from "@/lib/shared/api";
import {
  pageResponseSchema,
  type PageResponse,
} from "@/lib/shared/page-response";
import {
  employeeApiSchema,
  employeeOptionSchema,
  employeeResponseSchema,
  type EmployeeApiResponse,
  type EmployeeFormInput,
  type EmployeeOption,
  type EmployeeResponse,
} from "@/features/employees/schemas/employee";

export const employeesApi = {
  async list(
    page = 0,
    size = 50,
    sortBy = "name",
    search?: string,
  ): Promise<PageResponse<EmployeeResponse>> {
    const searchParam = search ? `&search=${encodeURIComponent(search)}` : "";
    const { data } = await api.get<PageResponse<EmployeeResponse>>(
      `/v1/employees?page=${page}&size=${size}&sort=${sortBy}${searchParam}`,
    );
    return pageResponseSchema(employeeResponseSchema).parse(data);
  },

  async getOptions(): Promise<EmployeeOption[]> {
    const { data } = await api.get<EmployeeOption[]>("/v1/employees/options");
    return employeeOptionSchema.array().parse(data);
  },

  async getById(id: string): Promise<EmployeeResponse> {
    const { data } = await api.get<EmployeeResponse>(`/v1/employees/${id}`);
    return employeeResponseSchema.parse(data);
  },

  async getByIdForEdit(id: string): Promise<EmployeeApiResponse> {
    const { data } = await api.get<EmployeeApiResponse>(`/v1/employees/${id}`);
    return employeeApiSchema.parse(data);
  },

  async create(input: EmployeeFormInput): Promise<EmployeeResponse> {
    const { data } = await api.post<EmployeeResponse>("/v1/employees", input);
    return employeeResponseSchema.parse(data);
  },

  async update(
    id: string,
    input: EmployeeFormInput,
  ): Promise<EmployeeResponse> {
    const { data } = await api.patch<EmployeeResponse>(
      `/v1/employees/${id}`,
      input,
    );
    return employeeResponseSchema.parse(data);
  },

  delete: (id: string) => api.delete(`/v1/employees/${id}`),
  archive: (id: string) => api.patch(`/v1/employees/${id}/archive`),
  unarchive: (id: string) => api.patch(`/v1/employees/${id}/unarchive`),
};
