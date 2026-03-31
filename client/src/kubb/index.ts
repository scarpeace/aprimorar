export type { GetDashboardSummaryQueryKey } from "./hooks/dashboard/useGetDashboardSummary.ts";
export type { ArchiveEmployeeMutationKey } from "./hooks/employee/useArchiveEmployee.ts";
export type { CreateEmployeeMutationKey } from "./hooks/employee/useCreateEmployee.ts";
export type { DeleteEmployeeMutationKey } from "./hooks/employee/useDeleteEmployee.ts";
export type { GetEmployeeByIdQueryKey } from "./hooks/employee/useGetEmployeeById.ts";
export type { GetEmployeeSummaryQueryKey } from "./hooks/employee/useGetEmployeeSummary.ts";
export type { GetEmployeesQueryKey } from "./hooks/employee/useGetEmployees.ts";
export type { UnarchiveEmployeeMutationKey } from "./hooks/employee/useUnarchiveEmployee.ts";
export type { UpdateEmployeeMutationKey } from "./hooks/employee/useUpdateEmployee.ts";
export type { CreateEventMutationKey } from "./hooks/event/useCreateEvent.ts";
export type { DeleteEventMutationKey } from "./hooks/event/useDeleteEvent.ts";
export type { GetEventByIdQueryKey } from "./hooks/event/useGetEventById.ts";
export type { GetEventsQueryKey } from "./hooks/event/useGetEvents.ts";
export type { GetEventsByEmployeeQueryKey } from "./hooks/event/useGetEventsByEmployee.ts";
export type { GetEventsByStudentQueryKey } from "./hooks/event/useGetEventsByStudent.ts";
export type { UpdateEventMutationKey } from "./hooks/event/useUpdateEvent.ts";
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
  EmployeeRequestDTO,
  EmployeeRequestDTODutyEnumKey,
} from "./types/EmployeeRequestDTO.ts";
export type {
  EmployeeResponseDTO,
  EmployeeResponseDTODutyEnumKey,
} from "./types/EmployeeResponseDTO.ts";
export type { EmployeeSummaryDTO } from "./types/EmployeeSummaryDTO.ts";
export type {
  EventRequestDTO,
  EventRequestDTOContentEnumKey,
} from "./types/EventRequestDTO.ts";
export type { EventResponseDTO } from "./types/EventResponseDTO.ts";
export type { PageDTOStudentResponseDTO } from "./types/PageDTOStudentResponseDTO.ts";
export type { PageMetadata } from "./types/PageMetadata.ts";
export type { PagedModelEmployeeResponseDTO } from "./types/PagedModelEmployeeResponseDTO.ts";
export type { PagedModelEventResponseDTO } from "./types/PagedModelEventResponseDTO.ts";
export type { PagedModelParentResponseDTO } from "./types/PagedModelParentResponseDTO.ts";
export type { ParentOptionsDTO } from "./types/ParentOptionsDTO.ts";
export type { ParentRequestDTO } from "./types/ParentRequestDTO.ts";
export type { ParentResponseDTO } from "./types/ParentResponseDTO.ts";
export type {
  ProblemResponseDTO,
  ProblemResponseDTOErrorCodeEnumKey,
  ProblemResponseDTOStatusEnumKey,
} from "./types/ProblemResponseDTO.ts";
export type { StudentOptionsDTO } from "./types/StudentOptionsDTO.ts";
export type { StudentRequestDTO } from "./types/StudentRequestDTO.ts";
export type { StudentResponseDTO } from "./types/StudentResponseDTO.ts";
export type {
  GetDashboardSummary200,
  GetDashboardSummary400,
  GetDashboardSummaryQuery,
  GetDashboardSummaryQueryParams,
  GetDashboardSummaryQueryResponse,
} from "./types/dashboard/GetDashboardSummary.ts";
export type {
  ArchiveEmployee204,
  ArchiveEmployee400,
  ArchiveEmployeeMutation,
  ArchiveEmployeeMutationResponse,
  ArchiveEmployeePathParams,
} from "./types/employee/ArchiveEmployee.ts";
export type {
  CreateEmployee200,
  CreateEmployee400,
  CreateEmployeeMutation,
  CreateEmployeeMutationRequest,
  CreateEmployeeMutationResponse,
} from "./types/employee/CreateEmployee.ts";
export type {
  DeleteEmployee204,
  DeleteEmployee400,
  DeleteEmployeeMutation,
  DeleteEmployeeMutationResponse,
  DeleteEmployeePathParams,
} from "./types/employee/DeleteEmployee.ts";
export type {
  GetEmployeeById200,
  GetEmployeeById400,
  GetEmployeeByIdPathParams,
  GetEmployeeByIdQuery,
  GetEmployeeByIdQueryResponse,
} from "./types/employee/GetEmployeeById.ts";
export type {
  GetEmployeeSummary200,
  GetEmployeeSummary400,
  GetEmployeeSummaryQuery,
  GetEmployeeSummaryQueryResponse,
} from "./types/employee/GetEmployeeSummary.ts";
export type {
  GetEmployees200,
  GetEmployees400,
  GetEmployeesQuery,
  GetEmployeesQueryParams,
  GetEmployeesQueryResponse,
} from "./types/employee/GetEmployees.ts";
export type {
  UnarchiveEmployee204,
  UnarchiveEmployee400,
  UnarchiveEmployeeMutation,
  UnarchiveEmployeeMutationResponse,
  UnarchiveEmployeePathParams,
} from "./types/employee/UnarchiveEmployee.ts";
export type {
  UpdateEmployee200,
  UpdateEmployee400,
  UpdateEmployeeMutation,
  UpdateEmployeeMutationRequest,
  UpdateEmployeeMutationResponse,
  UpdateEmployeePathParams,
} from "./types/employee/UpdateEmployee.ts";
export type {
  CreateEvent200,
  CreateEvent400,
  CreateEventMutation,
  CreateEventMutationRequest,
  CreateEventMutationResponse,
} from "./types/event/CreateEvent.ts";
export type {
  DeleteEvent204,
  DeleteEvent400,
  DeleteEventMutation,
  DeleteEventMutationResponse,
  DeleteEventPathParams,
} from "./types/event/DeleteEvent.ts";
export type {
  GetEventById200,
  GetEventById400,
  GetEventByIdPathParams,
  GetEventByIdQuery,
  GetEventByIdQueryResponse,
} from "./types/event/GetEventById.ts";
export type {
  GetEvents200,
  GetEvents400,
  GetEventsQuery,
  GetEventsQueryParams,
  GetEventsQueryResponse,
} from "./types/event/GetEvents.ts";
export type {
  GetEventsByEmployee200,
  GetEventsByEmployee400,
  GetEventsByEmployeePathParams,
  GetEventsByEmployeeQuery,
  GetEventsByEmployeeQueryParams,
  GetEventsByEmployeeQueryResponse,
} from "./types/event/GetEventsByEmployee.ts";
export type {
  GetEventsByStudent200,
  GetEventsByStudent400,
  GetEventsByStudentPathParams,
  GetEventsByStudentQuery,
  GetEventsByStudentQueryParams,
  GetEventsByStudentQueryResponse,
} from "./types/event/GetEventsByStudent.ts";
export type {
  UpdateEvent200,
  UpdateEvent400,
  UpdateEventMutation,
  UpdateEventMutationRequest,
  UpdateEventMutationResponse,
  UpdateEventPathParams,
} from "./types/event/UpdateEvent.ts";
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
export { getDashboardSummary } from "./hooks/dashboard/useGetDashboardSummary.ts";
export { getDashboardSummaryQueryKey } from "./hooks/dashboard/useGetDashboardSummary.ts";
export { getDashboardSummaryQueryOptions } from "./hooks/dashboard/useGetDashboardSummary.ts";
export { useGetDashboardSummary } from "./hooks/dashboard/useGetDashboardSummary.ts";
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
export { getEmployeeSummary } from "./hooks/employee/useGetEmployeeSummary.ts";
export { getEmployeeSummaryQueryKey } from "./hooks/employee/useGetEmployeeSummary.ts";
export { getEmployeeSummaryQueryOptions } from "./hooks/employee/useGetEmployeeSummary.ts";
export { useGetEmployeeSummary } from "./hooks/employee/useGetEmployeeSummary.ts";
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
export { createEvent } from "./hooks/event/useCreateEvent.ts";
export { createEventMutationKey } from "./hooks/event/useCreateEvent.ts";
export { createEventMutationOptions } from "./hooks/event/useCreateEvent.ts";
export { useCreateEvent } from "./hooks/event/useCreateEvent.ts";
export { deleteEvent } from "./hooks/event/useDeleteEvent.ts";
export { deleteEventMutationKey } from "./hooks/event/useDeleteEvent.ts";
export { deleteEventMutationOptions } from "./hooks/event/useDeleteEvent.ts";
export { useDeleteEvent } from "./hooks/event/useDeleteEvent.ts";
export { getEventById } from "./hooks/event/useGetEventById.ts";
export { getEventByIdQueryKey } from "./hooks/event/useGetEventById.ts";
export { getEventByIdQueryOptions } from "./hooks/event/useGetEventById.ts";
export { useGetEventById } from "./hooks/event/useGetEventById.ts";
export { getEvents } from "./hooks/event/useGetEvents.ts";
export { getEventsQueryKey } from "./hooks/event/useGetEvents.ts";
export { getEventsQueryOptions } from "./hooks/event/useGetEvents.ts";
export { useGetEvents } from "./hooks/event/useGetEvents.ts";
export { getEventsByEmployee } from "./hooks/event/useGetEventsByEmployee.ts";
export { getEventsByEmployeeQueryKey } from "./hooks/event/useGetEventsByEmployee.ts";
export { getEventsByEmployeeQueryOptions } from "./hooks/event/useGetEventsByEmployee.ts";
export { useGetEventsByEmployee } from "./hooks/event/useGetEventsByEmployee.ts";
export { getEventsByStudent } from "./hooks/event/useGetEventsByStudent.ts";
export { getEventsByStudentQueryKey } from "./hooks/event/useGetEventsByStudent.ts";
export { getEventsByStudentQueryOptions } from "./hooks/event/useGetEventsByStudent.ts";
export { useGetEventsByStudent } from "./hooks/event/useGetEventsByStudent.ts";
export { updateEvent } from "./hooks/event/useUpdateEvent.ts";
export { updateEventMutationKey } from "./hooks/event/useUpdateEvent.ts";
export { updateEventMutationOptions } from "./hooks/event/useUpdateEvent.ts";
export { useUpdateEvent } from "./hooks/event/useUpdateEvent.ts";
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
export { employeeRequestDTODutyEnum } from "./types/EmployeeRequestDTO.ts";
export { employeeResponseDTODutyEnum } from "./types/EmployeeResponseDTO.ts";
export { eventRequestDTOContentEnum } from "./types/EventRequestDTO.ts";
export { problemResponseDTOErrorCodeEnum } from "./types/ProblemResponseDTO.ts";
export { problemResponseDTOStatusEnum } from "./types/ProblemResponseDTO.ts";
