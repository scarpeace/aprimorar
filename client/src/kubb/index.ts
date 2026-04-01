export type { GetDashboardSummaryQueryKey } from "./hooks/dashboard-controller/useGetDashboardSummary.ts";
export type { ArchiveEmployeeMutationKey } from "./hooks/employee/useArchiveEmployee.ts";
export type { CreateEmployeeMutationKey } from "./hooks/employee/useCreateEmployee.ts";
export type { DeleteEmployeeMutationKey } from "./hooks/employee/useDeleteEmployee.ts";
export type { GetEmployeeByIdQueryKey } from "./hooks/employee/useGetEmployeeById.ts";
export type { GetEmployeeOptionsQueryKey } from "./hooks/employee/useGetEmployeeOptions.ts";
export type { GetEmployeesQueryKey } from "./hooks/employee/useGetEmployees.ts";
export type { UnarchiveEmployeeMutationKey } from "./hooks/employee/useUnarchiveEmployee.ts";
export type { UpdateEmployeeMutationKey } from "./hooks/employee/useUpdateEmployee.ts";
export type { CreateEventMutationKey } from "./hooks/events/useCreateEvent.ts";
export type { GetEventByIdQueryKey } from "./hooks/events/useGetEventById.ts";
export type { GetEventsQueryKey } from "./hooks/events/useGetEvents.ts";
export type { GetEventsByEmployeeQueryKey } from "./hooks/events/useGetEventsByEmployee.ts";
export type { GetEventsByStudentQueryKey } from "./hooks/events/useGetEventsByStudent.ts";
export type { UpdateEventMutationKey } from "./hooks/events/useUpdateEvent.ts";
export type { ArchiveParentMutationKey } from "./hooks/parent/useArchiveParent.ts";
export type { DeleteParentMutationKey } from "./hooks/parent/useDeleteParent.ts";
export type { GetParentByIdQueryKey } from "./hooks/parent/useGetParentById.ts";
export type { GetParentOptionsQueryKey } from "./hooks/parent/useGetParentOptions.ts";
export type { GetParentsQueryKey } from "./hooks/parent/useGetParents.ts";
export type { UnarchiveParentMutationKey } from "./hooks/parent/useUnarchiveParent.ts";
export type { ArchiveStudentMutationKey } from "./hooks/student/useArchiveStudent.ts";
export type { CreateStudentMutationKey } from "./hooks/student/useCreateStudent.ts";
export type { DeleteStudentMutationKey } from "./hooks/student/useDeleteStudent.ts";
export type { GetStudentByIdQueryKey } from "./hooks/student/useGetStudentById.ts";
export type { GetStudentsQueryKey } from "./hooks/student/useGetStudents.ts";
export type { GetStudentsByParentQueryKey } from "./hooks/student/useGetStudentsByParent.ts";
export type { GetStudentsOptionsQueryKey } from "./hooks/student/useGetStudentsOptions.ts";
export type { UnarchiveStudentMutationKey } from "./hooks/student/useUnarchiveStudent.ts";
export type { UpdateStudentMutationKey } from "./hooks/student/useUpdateStudent.ts";
export type {
  AddressRequestDTO,
  AddressRequestDTOStateEnumKey,
} from "./types/AddressRequestDTO.ts";
export type {
  AddressResponseDTO,
  AddressResponseDTOStateEnumKey,
} from "./types/AddressResponseDTO.ts";
export type { ClassesByContentDTO } from "./types/ClassesByContentDTO.ts";
export type { DashboardSummaryResponseDTO } from "./types/DashboardSummaryResponseDTO.ts";
export type {
  EmployeeCreateDTO,
  EmployeeCreateDTODutyEnumKey,
} from "./types/EmployeeCreateDTO.ts";
export type { EmployeeOptionsDTO } from "./types/EmployeeOptionsDTO.ts";
export type {
  EmployeeResponseDTO,
  EmployeeResponseDTODutyEnumKey,
} from "./types/EmployeeResponseDTO.ts";
export type {
  EmployeeUpdateDTO,
  EmployeeUpdateDTODutyEnumKey,
} from "./types/EmployeeUpdateDTO.ts";
export type {
  EventRequestDTO,
  EventRequestDTOContentEnumKey,
} from "./types/EventRequestDTO.ts";
export type { EventResponseDTO } from "./types/EventResponseDTO.ts";
export type { PageDTOEmployeeResponseDTO } from "./types/PageDTOEmployeeResponseDTO.ts";
export type { PageDTOEventResponseDTO } from "./types/PageDTOEventResponseDTO.ts";
export type { PageDTOParentResponseDTO } from "./types/PageDTOParentResponseDTO.ts";
export type { PageDTOStudentResponseDTO } from "./types/PageDTOStudentResponseDTO.ts";
export type { ParentCreateDTO } from "./types/ParentCreateDTO.ts";
export type { ParentOptionsDTO } from "./types/ParentOptionsDTO.ts";
export type { ParentResponseDTO } from "./types/ParentResponseDTO.ts";
export type { ParentUpdateDTO } from "./types/ParentUpdateDTO.ts";
export type { StudentCreateDTO } from "./types/StudentCreateDTO.ts";
export type { StudentOptionsDTO } from "./types/StudentOptionsDTO.ts";
export type { StudentResponseDTO } from "./types/StudentResponseDTO.ts";
export type { StudentUpdateDTO } from "./types/StudentUpdateDTO.ts";
export type {
  GetDashboardSummary200,
  GetDashboardSummaryQuery,
  GetDashboardSummaryQueryParams,
  GetDashboardSummaryQueryResponse,
} from "./types/dashboard-controller/GetDashboardSummary.ts";
export type {
  ArchiveEmployee204,
  ArchiveEmployeeMutation,
  ArchiveEmployeeMutationResponse,
  ArchiveEmployeePathParams,
} from "./types/employee/ArchiveEmployee.ts";
export type {
  CreateEmployee201,
  CreateEmployeeMutation,
  CreateEmployeeMutationRequest,
  CreateEmployeeMutationResponse,
} from "./types/employee/CreateEmployee.ts";
export type {
  DeleteEmployee204,
  DeleteEmployeeMutation,
  DeleteEmployeeMutationResponse,
  DeleteEmployeePathParams,
} from "./types/employee/DeleteEmployee.ts";
export type {
  GetEmployeeById200,
  GetEmployeeByIdPathParams,
  GetEmployeeByIdQuery,
  GetEmployeeByIdQueryResponse,
} from "./types/employee/GetEmployeeById.ts";
export type {
  GetEmployeeOptions200,
  GetEmployeeOptionsQuery,
  GetEmployeeOptionsQueryResponse,
} from "./types/employee/GetEmployeeOptions.ts";
export type {
  GetEmployees200,
  GetEmployeesQuery,
  GetEmployeesQueryParams,
  GetEmployeesQueryResponse,
} from "./types/employee/GetEmployees.ts";
export type {
  UnarchiveEmployee204,
  UnarchiveEmployeeMutation,
  UnarchiveEmployeeMutationResponse,
  UnarchiveEmployeePathParams,
} from "./types/employee/UnarchiveEmployee.ts";
export type {
  UpdateEmployee200,
  UpdateEmployeeMutation,
  UpdateEmployeeMutationRequest,
  UpdateEmployeeMutationResponse,
  UpdateEmployeePathParams,
} from "./types/employee/UpdateEmployee.ts";
export type {
  CreateEvent201,
  CreateEventMutation,
  CreateEventMutationRequest,
  CreateEventMutationResponse,
} from "./types/events/CreateEvent.ts";
export type {
  GetEventById200,
  GetEventByIdPathParams,
  GetEventByIdQuery,
  GetEventByIdQueryResponse,
} from "./types/events/GetEventById.ts";
export type {
  GetEvents200,
  GetEventsQuery,
  GetEventsQueryParams,
  GetEventsQueryResponse,
} from "./types/events/GetEvents.ts";
export type {
  GetEventsByEmployee200,
  GetEventsByEmployeePathParams,
  GetEventsByEmployeeQuery,
  GetEventsByEmployeeQueryParams,
  GetEventsByEmployeeQueryResponse,
} from "./types/events/GetEventsByEmployee.ts";
export type {
  GetEventsByStudent200,
  GetEventsByStudentPathParams,
  GetEventsByStudentQuery,
  GetEventsByStudentQueryParams,
  GetEventsByStudentQueryResponse,
} from "./types/events/GetEventsByStudent.ts";
export type {
  UpdateEvent200,
  UpdateEventMutation,
  UpdateEventMutationRequest,
  UpdateEventMutationResponse,
  UpdateEventPathParams,
} from "./types/events/UpdateEvent.ts";
export type {
  ArchiveParent204,
  ArchiveParentMutation,
  ArchiveParentMutationResponse,
  ArchiveParentPathParams,
} from "./types/parent/ArchiveParent.ts";
export type {
  DeleteParent204,
  DeleteParentMutation,
  DeleteParentMutationResponse,
  DeleteParentPathParams,
} from "./types/parent/DeleteParent.ts";
export type {
  GetParentById200,
  GetParentByIdPathParams,
  GetParentByIdQuery,
  GetParentByIdQueryResponse,
} from "./types/parent/GetParentById.ts";
export type {
  GetParentOptions200,
  GetParentOptionsQuery,
  GetParentOptionsQueryResponse,
} from "./types/parent/GetParentOptions.ts";
export type {
  GetParents200,
  GetParentsQuery,
  GetParentsQueryParams,
  GetParentsQueryResponse,
} from "./types/parent/GetParents.ts";
export type {
  UnarchiveParent204,
  UnarchiveParentMutation,
  UnarchiveParentMutationResponse,
  UnarchiveParentPathParams,
} from "./types/parent/UnarchiveParent.ts";
export type {
  ArchiveStudent204,
  ArchiveStudentMutation,
  ArchiveStudentMutationResponse,
  ArchiveStudentPathParams,
} from "./types/student/ArchiveStudent.ts";
export type {
  CreateStudent201,
  CreateStudentMutation,
  CreateStudentMutationRequest,
  CreateStudentMutationResponse,
} from "./types/student/CreateStudent.ts";
export type {
  DeleteStudent204,
  DeleteStudentMutation,
  DeleteStudentMutationResponse,
  DeleteStudentPathParams,
} from "./types/student/DeleteStudent.ts";
export type {
  GetStudentById200,
  GetStudentByIdPathParams,
  GetStudentByIdQuery,
  GetStudentByIdQueryResponse,
} from "./types/student/GetStudentById.ts";
export type {
  GetStudents200,
  GetStudentsQuery,
  GetStudentsQueryParams,
  GetStudentsQueryResponse,
} from "./types/student/GetStudents.ts";
export type {
  GetStudentsByParent200,
  GetStudentsByParentPathParams,
  GetStudentsByParentQuery,
  GetStudentsByParentQueryResponse,
} from "./types/student/GetStudentsByParent.ts";
export type {
  GetStudentsOptions200,
  GetStudentsOptionsQuery,
  GetStudentsOptionsQueryResponse,
} from "./types/student/GetStudentsOptions.ts";
export type {
  UnarchiveStudent204,
  UnarchiveStudentMutation,
  UnarchiveStudentMutationResponse,
  UnarchiveStudentPathParams,
} from "./types/student/UnarchiveStudent.ts";
export type {
  UpdateStudent200,
  UpdateStudentMutation,
  UpdateStudentMutationRequest,
  UpdateStudentMutationResponse,
  UpdateStudentPathParams,
} from "./types/student/UpdateStudent.ts";
export { getDashboardSummary } from "./hooks/dashboard-controller/useGetDashboardSummary.ts";
export { getDashboardSummaryQueryKey } from "./hooks/dashboard-controller/useGetDashboardSummary.ts";
export { getDashboardSummaryQueryOptions } from "./hooks/dashboard-controller/useGetDashboardSummary.ts";
export { useGetDashboardSummary } from "./hooks/dashboard-controller/useGetDashboardSummary.ts";
export { archiveEmployee } from "./hooks/employee/useArchiveEmployee.ts";
export { archiveEmployeeMutationKey } from "./hooks/employee/useArchiveEmployee.ts";
export { archiveEmployeeMutationOptions } from "./hooks/employee/useArchiveEmployee.ts";
export { useArchiveEmployee } from "./hooks/employee/useArchiveEmployee.ts";
export { createEmployee } from "./hooks/employee/useCreateEmployee.ts";
export { createEmployeeMutationKey } from "./hooks/employee/useCreateEmployee.ts";
export { createEmployeeMutationOptions } from "./hooks/employee/useCreateEmployee.ts";
export { useCreateEmployee } from "./hooks/employee/useCreateEmployee.ts";
export { deleteEmployee } from "./hooks/employee/useDeleteEmployee.ts";
export { deleteEmployeeMutationKey } from "./hooks/employee/useDeleteEmployee.ts";
export { deleteEmployeeMutationOptions } from "./hooks/employee/useDeleteEmployee.ts";
export { useDeleteEmployee } from "./hooks/employee/useDeleteEmployee.ts";
export { getEmployeeById } from "./hooks/employee/useGetEmployeeById.ts";
export { getEmployeeByIdQueryKey } from "./hooks/employee/useGetEmployeeById.ts";
export { getEmployeeByIdQueryOptions } from "./hooks/employee/useGetEmployeeById.ts";
export { useGetEmployeeById } from "./hooks/employee/useGetEmployeeById.ts";
export { getEmployeeOptions } from "./hooks/employee/useGetEmployeeOptions.ts";
export { getEmployeeOptionsQueryKey } from "./hooks/employee/useGetEmployeeOptions.ts";
export { getEmployeeOptionsQueryOptions } from "./hooks/employee/useGetEmployeeOptions.ts";
export { useGetEmployeeOptions } from "./hooks/employee/useGetEmployeeOptions.ts";
export { getEmployees } from "./hooks/employee/useGetEmployees.ts";
export { getEmployeesQueryKey } from "./hooks/employee/useGetEmployees.ts";
export { getEmployeesQueryOptions } from "./hooks/employee/useGetEmployees.ts";
export { useGetEmployees } from "./hooks/employee/useGetEmployees.ts";
export { unarchiveEmployee } from "./hooks/employee/useUnarchiveEmployee.ts";
export { unarchiveEmployeeMutationKey } from "./hooks/employee/useUnarchiveEmployee.ts";
export { unarchiveEmployeeMutationOptions } from "./hooks/employee/useUnarchiveEmployee.ts";
export { useUnarchiveEmployee } from "./hooks/employee/useUnarchiveEmployee.ts";
export { updateEmployee } from "./hooks/employee/useUpdateEmployee.ts";
export { updateEmployeeMutationKey } from "./hooks/employee/useUpdateEmployee.ts";
export { updateEmployeeMutationOptions } from "./hooks/employee/useUpdateEmployee.ts";
export { useUpdateEmployee } from "./hooks/employee/useUpdateEmployee.ts";
export { createEvent } from "./hooks/events/useCreateEvent.ts";
export { createEventMutationKey } from "./hooks/events/useCreateEvent.ts";
export { createEventMutationOptions } from "./hooks/events/useCreateEvent.ts";
export { useCreateEvent } from "./hooks/events/useCreateEvent.ts";
export { getEventById } from "./hooks/events/useGetEventById.ts";
export { getEventByIdQueryKey } from "./hooks/events/useGetEventById.ts";
export { getEventByIdQueryOptions } from "./hooks/events/useGetEventById.ts";
export { useGetEventById } from "./hooks/events/useGetEventById.ts";
export { getEvents } from "./hooks/events/useGetEvents.ts";
export { getEventsQueryKey } from "./hooks/events/useGetEvents.ts";
export { getEventsQueryOptions } from "./hooks/events/useGetEvents.ts";
export { useGetEvents } from "./hooks/events/useGetEvents.ts";
export { getEventsByEmployee } from "./hooks/events/useGetEventsByEmployee.ts";
export { getEventsByEmployeeQueryKey } from "./hooks/events/useGetEventsByEmployee.ts";
export { getEventsByEmployeeQueryOptions } from "./hooks/events/useGetEventsByEmployee.ts";
export { useGetEventsByEmployee } from "./hooks/events/useGetEventsByEmployee.ts";
export { getEventsByStudent } from "./hooks/events/useGetEventsByStudent.ts";
export { getEventsByStudentQueryKey } from "./hooks/events/useGetEventsByStudent.ts";
export { getEventsByStudentQueryOptions } from "./hooks/events/useGetEventsByStudent.ts";
export { useGetEventsByStudent } from "./hooks/events/useGetEventsByStudent.ts";
export { updateEvent } from "./hooks/events/useUpdateEvent.ts";
export { updateEventMutationKey } from "./hooks/events/useUpdateEvent.ts";
export { updateEventMutationOptions } from "./hooks/events/useUpdateEvent.ts";
export { useUpdateEvent } from "./hooks/events/useUpdateEvent.ts";
export { archiveParent } from "./hooks/parent/useArchiveParent.ts";
export { archiveParentMutationKey } from "./hooks/parent/useArchiveParent.ts";
export { archiveParentMutationOptions } from "./hooks/parent/useArchiveParent.ts";
export { useArchiveParent } from "./hooks/parent/useArchiveParent.ts";
export { deleteParent } from "./hooks/parent/useDeleteParent.ts";
export { deleteParentMutationKey } from "./hooks/parent/useDeleteParent.ts";
export { deleteParentMutationOptions } from "./hooks/parent/useDeleteParent.ts";
export { useDeleteParent } from "./hooks/parent/useDeleteParent.ts";
export { getParentById } from "./hooks/parent/useGetParentById.ts";
export { getParentByIdQueryKey } from "./hooks/parent/useGetParentById.ts";
export { getParentByIdQueryOptions } from "./hooks/parent/useGetParentById.ts";
export { useGetParentById } from "./hooks/parent/useGetParentById.ts";
export { getParentOptions } from "./hooks/parent/useGetParentOptions.ts";
export { getParentOptionsQueryKey } from "./hooks/parent/useGetParentOptions.ts";
export { getParentOptionsQueryOptions } from "./hooks/parent/useGetParentOptions.ts";
export { useGetParentOptions } from "./hooks/parent/useGetParentOptions.ts";
export { getParents } from "./hooks/parent/useGetParents.ts";
export { getParentsQueryKey } from "./hooks/parent/useGetParents.ts";
export { getParentsQueryOptions } from "./hooks/parent/useGetParents.ts";
export { useGetParents } from "./hooks/parent/useGetParents.ts";
export { unarchiveParent } from "./hooks/parent/useUnarchiveParent.ts";
export { unarchiveParentMutationKey } from "./hooks/parent/useUnarchiveParent.ts";
export { unarchiveParentMutationOptions } from "./hooks/parent/useUnarchiveParent.ts";
export { useUnarchiveParent } from "./hooks/parent/useUnarchiveParent.ts";
export { archiveStudent } from "./hooks/student/useArchiveStudent.ts";
export { archiveStudentMutationKey } from "./hooks/student/useArchiveStudent.ts";
export { archiveStudentMutationOptions } from "./hooks/student/useArchiveStudent.ts";
export { useArchiveStudent } from "./hooks/student/useArchiveStudent.ts";
export { createStudent } from "./hooks/student/useCreateStudent.ts";
export { createStudentMutationKey } from "./hooks/student/useCreateStudent.ts";
export { createStudentMutationOptions } from "./hooks/student/useCreateStudent.ts";
export { useCreateStudent } from "./hooks/student/useCreateStudent.ts";
export { deleteStudent } from "./hooks/student/useDeleteStudent.ts";
export { deleteStudentMutationKey } from "./hooks/student/useDeleteStudent.ts";
export { deleteStudentMutationOptions } from "./hooks/student/useDeleteStudent.ts";
export { useDeleteStudent } from "./hooks/student/useDeleteStudent.ts";
export { getStudentById } from "./hooks/student/useGetStudentById.ts";
export { getStudentByIdQueryKey } from "./hooks/student/useGetStudentById.ts";
export { getStudentByIdQueryOptions } from "./hooks/student/useGetStudentById.ts";
export { useGetStudentById } from "./hooks/student/useGetStudentById.ts";
export { getStudents } from "./hooks/student/useGetStudents.ts";
export { getStudentsQueryKey } from "./hooks/student/useGetStudents.ts";
export { getStudentsQueryOptions } from "./hooks/student/useGetStudents.ts";
export { useGetStudents } from "./hooks/student/useGetStudents.ts";
export { getStudentsByParent } from "./hooks/student/useGetStudentsByParent.ts";
export { getStudentsByParentQueryKey } from "./hooks/student/useGetStudentsByParent.ts";
export { getStudentsByParentQueryOptions } from "./hooks/student/useGetStudentsByParent.ts";
export { useGetStudentsByParent } from "./hooks/student/useGetStudentsByParent.ts";
export { getStudentsOptions } from "./hooks/student/useGetStudentsOptions.ts";
export { getStudentsOptionsQueryKey } from "./hooks/student/useGetStudentsOptions.ts";
export { getStudentsOptionsQueryOptions } from "./hooks/student/useGetStudentsOptions.ts";
export { useGetStudentsOptions } from "./hooks/student/useGetStudentsOptions.ts";
export { unarchiveStudent } from "./hooks/student/useUnarchiveStudent.ts";
export { unarchiveStudentMutationKey } from "./hooks/student/useUnarchiveStudent.ts";
export { unarchiveStudentMutationOptions } from "./hooks/student/useUnarchiveStudent.ts";
export { useUnarchiveStudent } from "./hooks/student/useUnarchiveStudent.ts";
export { updateStudent } from "./hooks/student/useUpdateStudent.ts";
export { updateStudentMutationKey } from "./hooks/student/useUpdateStudent.ts";
export { updateStudentMutationOptions } from "./hooks/student/useUpdateStudent.ts";
export { useUpdateStudent } from "./hooks/student/useUpdateStudent.ts";
export { addressRequestDTOStateEnum } from "./types/AddressRequestDTO.ts";
export { addressResponseDTOStateEnum } from "./types/AddressResponseDTO.ts";
export { employeeCreateDTODutyEnum } from "./types/EmployeeCreateDTO.ts";
export { employeeResponseDTODutyEnum } from "./types/EmployeeResponseDTO.ts";
export { employeeUpdateDTODutyEnum } from "./types/EmployeeUpdateDTO.ts";
export { eventRequestDTOContentEnum } from "./types/EventRequestDTO.ts";
