# Aprimorar - Planning (Epics, User Stories, Gaps)

Version: 1.0

Original docs date: 2026-02-26
This consolidated doc generated: 2026-02-27
Repository: https://github.com/anomalyco/aprimorar

## 1. Post-MVP Workflow

- New or changed requirements go into `docs/PROJECT.md`.
- After requirements are updated, we add/adjust epics and user stories in this document.

## 2. Epics and User Stories

### 2.1 Epic 1: Core Data Management

Description: Manage core entities (Students, Employees, Parents, Events)

#### User Story 1.1: Student Management

Priority: High | Story Points: 8

As an administrator,
I want to manage student records,
So that I can enroll, update, and track students in the system.

| Task | Description | Status |
|---|---|---|
| T-1.1.2 | Add student search/filter by name | Pending |
| T-1.1.3 | Add student search/filter by activity type | Pending |
| T-1.1.4 | Add bulk import/export (CSV) | Pending |
| T-1.1.5 | Student age validation (min/max) | Pending |

#### User Story 1.2: Employee Management

Priority: High | Story Points: 5

As an administrator,
I want to manage employee records,
So that I can track teachers and staff.

All tasks completed.

#### User Story 1.3: Event/Class Management

Priority: High | Story Points: 8

As an employee,
I want to schedule and manage classes/events,
So that students can book sessions.

| Task | Description | Status |
|---|---|---|
| T-1.3.3 | Fix N+1 query problem | Pending |
| T-1.3.4 | Add event filtering by date range | Pending |
| T-1.3.5 | Add event filtering by student | Pending |
| T-1.3.6 | Add event filtering by employee | Pending |

#### User Story 1.4: Parent Management

Priority: Medium | Story Points: 3

As an administrator,
I want to manage parent/guardian records when creating or editing students,
So that I can maintain contact with student guardians.

All tasks completed.

Business rules

- A parent can have multiple students
- Parents are created inline when creating a new student, OR an existing parent can be assigned via dropdown
- Parent list shows only active parents (id + name for dropdown)
- Students can change their assigned parent during edit
- Parents are soft-deleted (have `active` field)

#### User Story 1.5: Event Session Types (Mentorship and Psychological Consulting)

Priority: High | Story Points: 5

As an employee,
I want to classify events by session type,
So that I can schedule and report on classes, mentorship, and consulting consistently.

| Task | Description | Status |
|---|---|---|
| T-1.5.1 | Add `sessionType` enum (CLASS_1ON1, CLASS_GROUP, MENTORSHIP_1ON1, PSYCHOLOGICAL_CONSULTING) | Pending |
| T-1.5.2 | Add `sessionType` to Event entity/DTOs and validate it is required | Pending |
| T-1.5.3 | Add DB migration for `session_type` column on events | Pending |
| T-1.5.4 | Add `sessionType` filtering on GET `/v1/events` | Pending |
| T-1.5.5 | (Frontend) Add session type dropdown + filters | Pending |

### 2.2 Epic 2: Authentication and Authorization

Description: Implement secure access control for the system

#### User Story 2.1: User Authentication

Priority: High | Story Points: 13

As a user,
I want to log in securely,
So that I can access the system.

| Task | Description | Status |
|---|---|---|
| T-2.1.1 | Implement JWT token generation | Pending |
| T-2.1.2 | Implement login endpoint | Pending |
| T-2.1.3 | Implement password hashing (BCrypt) | Pending |
| T-2.1.4 | Configure HTTPS/TLS | Pending |
| T-2.1.5 | Configure CORS policy | Pending |
| T-2.1.6 | Implement refresh token mechanism | Pending |
| T-2.1.7 | Implement logout functionality | Pending |

#### User Story 2.2: Role-Based Access Control

Priority: High | Story Points: 8

As an administrator,
I want to control access based on roles,
So that users can only perform authorized actions.

| Task | Description | Status |
|---|---|---|
| T-2.2.1 | Define permission matrix | Pending |
| T-2.2.2 | Implement `@PreAuthorize` annotations | Pending |
| T-2.2.3 | Create admin-only endpoints | Pending |
| T-2.2.4 | Implement employee self-service | Pending |
| T-2.2.5 | Implement parent read-only portal | Pending |

#### User Story 2.3: Security Hardening

Priority: Medium | Story Points: 5

As a security engineer,
I want to harden the application,
So that it meets security best practices.

| Task | Description | Status |
|---|---|---|
| T-2.3.1 | Implement rate limiting | Pending |
| T-2.3.2 | Add CSRF protection | Pending |
| T-2.3.3 | Implement audit logging | Pending |
| T-2.3.4 | Add security headers | Pending |

#### User Story 2.4: Privacy Hardening for Confidential Session Types (Follow-up)

Priority: Medium | Story Points: 8

As an administrator,
I want confidential session types to have stricter access controls and auditing,
So that mentorship and psychological consulting data is protected.

Applies to session types: MENTORSHIP_1ON1, PSYCHOLOGICAL_CONSULTING

| Task | Description | Status |
|---|---|---|
| T-2.4.1 | Define permission matrix for confidential event access | Pending |
| T-2.4.2 | Restrict list/details access to ADMIN and assigned employee | Pending |
| T-2.4.3 | Add audit logging for reads of confidential events | Pending |
| T-2.4.4 | Consider redacting title/description in list views for non-privileged roles | Pending |

### 2.3 Epic 3: Frontend Development

Description: Build the user interface for the application

> **TypeScript Policy:** All frontend code (components, hooks, services, stores) must be TypeScript (.tsx/.ts). No vanilla JavaScript permitted.

#### User Story 3.1: Dashboard and Navigation

Priority: High | Story Points: 5

As a user,
I want to navigate the application,
So that I can access different features easily.

| Task | Description | Status |
|---|---|---|

#### User Story 3.2: Student Management UI

Priority: High | Story Points: 8

As an administrator,
I want to manage students through the UI,
So that I don't need to use API directly.

| Task | Description | Status |
|---|---|---|
| T-3.2.1 | Add Delete Buttons to Employees, Students and Events list pages (Confirm before deleting) | Complete |
| T-3.2.2 | Refactor list-page delete flow into shared hook/component to reduce drift and prevent delete races | Pending |
| T-3.2.3 | Add pagination controls to all listing pages | Pending |
| T-3.2.4 | Add search and filter functionality to all listing pages| Pending |
| T-3.2.6 | Add form validation feedback (Is this really necessary?) | Pending |

#### User Story 3.3: Event Management UI

Priority: High | Story Points: 8

As an employee,
I want to manage events through the UI,
So that scheduling is intuitive.

| Task | Description | Status |
|---|---|---|
| T-3.3.1 | Create event calendar view | Deferred (Post-MVP) |
| T-3.3.2 | Create event form component | Pending |
| T-3.3.3 | Add date/time picker | Pending |
| T-3.3.4 | Add student/employee dropdowns | Pending |
| T-3.3.5 | Display pricing information | Pending |
| T-3.3.6 | Add payment tracking display | Pending |

### 2.4 Epic 4: Calendar and Scheduling

Description: Integrate with Google Calendar for event scheduling

#### User Story 4.1: Google Calendar Integration

Priority: Medium | Story Points: 8

As an employee,
I want to sync events with Google Calendar,
So that I can manage schedules externally.

| Task | Description | Status |
|---|---|---|
| T-4.1.1 | Set up Google Calendar API credentials | Pending |
| T-4.1.2 | Implement calendar service | Pending |
| T-4.1.3 | Create events in Google Calendar | Pending |
| T-4.1.4 | Update calendar events on change | Pending |
| T-4.1.5 | Delete calendar events on cancellation | Pending |
| T-4.1.6 | Handle calendar sync conflicts | Pending |

#### User Story 4.2: Calendar Views

Priority: Medium | Story Points: 5

As a user,
I want to view events in calendar format,
So that scheduling is visual.

| Task | Description | Status |
|---|---|---|
| T-4.2.1 | Implement monthly calendar view | Pending |
| T-4.2.2 | Implement weekly calendar view | Pending |
| T-4.2.3 | Implement daily schedule view | Pending |

### 2.5 Epic 5: Payments and Billing

Description: Handle payment tracking and financial management

#### User Story 5.1: Payment Tracking

Priority: Medium | Story Points: 5

As an administrator,
I want to track payments for events,
So that I can manage billing.

| Task | Description | Status |
|---|---|---|
| T-5.1.1 | Enhance payment fields | Pending |
| T-5.1.2 | Calculate outstanding balance | Pending |
| T-5.1.3 | Generate payment reports | Pending |
| T-5.1.4 | Add payment history | Pending |

#### User Story 5.2: Payment Gateway Integration

Priority: Low | Story Points: 13

As a student/parent,
I want to pay online,
So that I don't need to pay in person.

| Task | Description | Status |
|---|---|---|
| T-5.2.1 | Evaluate payment gateway options | Pending |
| T-5.2.2 | Implement payment processing | Pending |
| T-5.2.3 | Handle payment webhooks | Pending |
| T-5.2.4 | Implement refund process | Pending |
| T-5.2.5 | Generate invoices/receipts | Pending |

## 3. Development Roadmap

Phase 1: Foundation (Weeks 1-4)
- [ ] Complete Employee CRUD API
- [ ] Complete Parent CRUD API
- [ ] Add `sessionType` to events
- [ ] Fix N+1 query problems
- [ ] Implement search and filter capabilities
- [ ] Add dashboard summary API (month KPIs + upcoming events)

Phase 2: Security (Weeks 5-8)
- [ ] Implement JWT authentication
- [ ] Implement RBAC
- [ ] Configure HTTPS
- [ ] Add rate limiting
- [ ] Security audit

Phase 3: Frontend (Weeks 9-14)
- [x] Build dashboard and navigation
- [ ] Add dashboard KPIs + upcoming list + quick actions (no calendar view in MVP)
- [ ] Build student management UI
- [ ] Build employee management UI
- [ ] Build event management UI
- [ ] Build parent management UI
- [ ] Testing and polish

Phase 4: Integrations (Weeks 15-18)
- [ ] Google Calendar integration
- [ ] Calendar views in UI
- [ ] Payment tracking enhancements

Phase 5: Payments (Weeks 19-22)
- [ ] Payment gateway integration
- [ ] Invoice generation
- [ ] Financial reports

## 4. Gap Analysis

### 4.1 Critical Gaps (High Priority)

| ID | Gap | Impact | Effort |
|---|---|---|---|
| GAP-001 | Authentication system | No login/secure access | High |
| GAP-002 | Authorization/RBAC | No role-based access control | High |
| GAP-003 | Frontend UI | UI incomplete | High |
| GAP-004 | Employee CRUD | Docs drifted; verify and align | Medium |
| GAP-005 | Parent CRUD | No dedicated endpoints beyond active list | Medium |

### 4.2 Important Gaps (Medium Priority)

| ID | Gap | Impact | Effort |
|---|---|---|---|
| GAP-006 | Google Calendar integration | Scheduling automation missing | Medium |
| GAP-007 | Payment processing | No payment gateway integration | Medium |
| GAP-009 | Student age validation | Min/max age validation missing | Low |
| GAP-010 | Query optimization | N+1 problem in events | Low |

### 4.3 Security Gaps

| ID | Gap | Description |
|---|---|---|
| SEC-001 | No JWT implementation | Token-based auth missing |
| SEC-002 | No password hashing | BCrypt or similar not implemented |
| SEC-003 | No HTTPS/TLS | SSL not configured |
| SEC-004 | No CORS policy | Cross-origin requests not restricted |
| SEC-005 | No rate limiting | API vulnerable to abuse |
| SEC-006 | No CSRF protection | Anti-CSRF tokens not implemented |

## 5. Known Code Issues (Trackable)

| ID | Issue | Notes |
|---|---|---|
| ISSUE-001 | N+1 query problem | Event listing code has a TODO; confirm with profiling and fix with fetch/join strategy |
| ISSUE-003 | No age validation | Student entity/DTO has an open TODO; define min/max and enforce |

## 6. Testing Gaps

Current tests
- Mapper tests (StudentMapper, EmployeeMapper, ParentMapper, EventMapper, AddressMapper)
- Service tests (StudentService, EventService)
- DTO validation tests

Required tests
- Controller integration tests
- Security tests
- E2E tests for critical flows
- Performance/load tests

## 7. Acceptance Criteria

Core functionality
- [ ] CRUD operations work for Students, Employees, Parents, Events
- [ ] Pagination and sorting work correctly
- [ ] Data validation prevents invalid data entry
- [ ] Error messages are clear and helpful

Security
- [ ] Authentication required for all endpoints except public ones
- [ ] Role-based access controls enforced
- [ ] Passwords securely hashed
- [ ] API protected against common attacks

User interface
- [ ] Users can manage all entities through UI
- [ ] Navigation is intuitive
- [ ] Forms provide immediate validation feedback
- [ ] Calendar views display events correctly

Performance
- [ ] API response times < 200ms
- [ ] Pagination works efficiently with large datasets
- [ ] No N+1 query problems

## 8. User Story Template

```markdown
#### [User Story Title]
Priority: [High/Medium/Low] | Story Points: [Number]

As a [role],
I want to [action],
So that [benefit].

| Task | Description | Status |
|---|---|---|
| T-?.?.1 | [Task description] | [Pending/In Progress/Complete] |
| T-?.?.2 | [Task description] | [Pending/In Progress/Complete] |
| T-?.?.3 | [Task description] | [Pending/In Progress/Complete] |

Acceptance criteria
- [ ] [Criterion 1]
- [ ] [Criterion 2]
- [ ] [Criterion 3]

Technical notes
- [Any technical considerations, dependencies, or constraints]
```
