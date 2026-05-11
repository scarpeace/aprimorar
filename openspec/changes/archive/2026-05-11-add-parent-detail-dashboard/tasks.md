## 1. Parent Detail Composition

- [x] 1.1 Refactor `ParentDetailPage` to match the `StudentDetailsPage` composition pattern with page header, info section, linked-students table, pagination state, and edit modal state.
- [x] 1.2 Ensure the edit modal uses `ParentForm` with current parent data from `useGetParentById` and closes on success or cancel.

## 2. Parent Information Section

- [x] 2.1 Create `ParentInfoSection` that accepts `parentId` and `onEdit`.
- [x] 2.2 Load parent data with `useGetParentById` and render loading and error states.
- [x] 2.3 Display parent name, CPF, email, contact, creation date, and active/inactive status using current `ParentResponseDTO` fields.
- [x] 2.4 Render edit, archive/unarchive, and delete actions using existing parent action components.

## 3. Linked Students Table

- [x] 3.1 Create `ParentStudentsTable` that accepts `parentId`, `currentPage`, and `onPageChange`.
- [x] 3.2 Load linked students with `useGetStudentsByParent` and pass pagination params through the generated hook.
- [x] 3.3 Render linked student rows with identifying fields and navigation to each student detail page.
- [x] 3.4 Render loading, error, empty, and pagination states for the linked-students table.

## 4. Form and Cache Integration

- [x] 4.1 Verify `ParentForm` works for detail-page editing with current generated `ParentResponseDTO` fields.
- [x] 4.2 Fix parent mutation invalidation to use current generated query key helpers, including linked-students invalidation when archive status changes.
- [x] 4.3 Remove stale `archivedAt`, undefined linked-student query variables, and misspelled generated helper references from parent feature code.

## 5. Verification

- [x] 5.1 Run `npm run lint` in `client/`.
- [x] 5.2 Run `npm run build` in `client/` or document any remaining unrelated backend/Kubb contract errors.
- [ ] 5.3 Manually verify `/parents/:id` renders parent information, linked students, and the edit modal flow.
