import { api } from "@/lib/shared/api";
import type { EmployeeSummaryDTO, StudentSummaryDTO } from "@/kubb";
import { useQuery } from "@tanstack/react-query";

interface SummaryParams {
  startDate?: string;
  endDate?: string;
}

export function useFinanceStudentSummary(studentId: string, params: SummaryParams) {
  return useQuery({
    queryKey: ["finance", "student-summary", studentId, params],
    queryFn: async () => {
      const response = await api.get<StudentSummaryDTO>(`/v1/finance/students/${studentId}/summary`, { params });
      return response.data;
    },
    enabled: !!studentId,
  });
}

export function useFinanceEmployeeSummary(employeeId: string, params: SummaryParams) {
  return useQuery({
    queryKey: ["finance", "employee-summary", employeeId, params],
    queryFn: async () => {
      const response = await api.get<EmployeeSummaryDTO>(`/v1/finance/employees/${employeeId}/summary`, { params });
      return response.data;
    },
    enabled: !!employeeId,
  });
}
