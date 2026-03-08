# Client form refactor review (2026-03-07)

Scope checked:
- committed change `8ba428e` (`refactoring student forms`)
- current uncommitted diff in `/client` (events + students)

Goal used for review:
- keep code beginner friendly
- avoid too much abstraction
- keep organization without overengineering

## Main problems found

1. **Abstraction level increased again after simplification**
   - New hooks `useEventFormOptions` and `useStudentParentOptions` moved logic away from pages into generic utilities.
   - This adds indirection for beginners: to understand one page, you now need to jump across page + form + hook + utils.
   - Files:
     - `client/src/features/events/utils/eventFormOptions.ts`
     - `client/src/features/students/utils/studentParentOptions.ts`

2. **`utils` folder now contains stateful React hooks**
   - `eventFormOptions.ts` and `studentParentOptions.ts` are hooks, not pure utility helpers.
   - Naming/location mismatch hurts discoverability for beginners (expectation in `utils` is usually pure functions).

3. **Generic parameterized hook for parents is harder to reason about**
   - `useStudentParentOptions({ emptyMode, initialMode })` uses mode configuration to drive behavior for both create and edit.
   - This is DRY, but not very explicit; page-level code becomes less obvious than direct local state.
   - File: `client/src/features/students/utils/studentParentOptions.ts`

4. **Extra coupling between form component and utility module**
   - `StudentForm` now imports `StudentParentMode` from `studentFormUtils`.
   - UI component depending on a utils module for domain type is an odd dependency direction.
   - File: `client/src/features/students/components/StudentForm.tsx`

5. **Student edit path can trigger redundant parent fetches**
   - `useStudentParentOptions` auto-loads in its own `useEffect`.
   - `StudentEditPage` also calls `loadParents()` inside `loadStudent()`.
   - Result: likely duplicate request on first render.
   - File: `client/src/features/students/pages/StudentEditPage.tsx`

6. **Prop surface of shared forms is getting large**
   - `EventForm` and `StudentForm` now require many data/loading/error/reload props.
   - This centralizes rendering, but increases prop complexity and mental load for beginners.
   - Files:
     - `client/src/features/events/components/EventForm.tsx`
     - `client/src/features/students/components/StudentForm.tsx`

## Good changes worth keeping

- Moving API calls out of `StudentForm` (from commit `8ba428e`) was a good direction for separation of concerns.
- `buildStudentPayload` avoids duplicated payload branching code across create/edit pages.
- Build currently passes (`npm run build` in `client`).

## Suggestions (beginner-friendly direction)

1. Keep page-level fetching explicit for now
   - For this project stage, prefer local `useState + useEffect` in create/edit pages over generic hooks.
   - This keeps behavior visible where it is used.

2. If hooks are kept, move and rename them clearly
   - Move from `utils` to `hooks`:
     - `features/events/hooks/useEventFormOptions.ts`
     - `features/students/hooks/useStudentParentOptions.ts`

3. Reduce config-driven behavior in student parent hook
   - Replace `emptyMode/initialMode` config with simpler explicit wrappers:
     - `useCreateStudentParents()`
     - `useEditStudentParents()`
   - This is slightly repetitive but easier to read and teach.

4. Remove double fetch in student edit
   - Choose one source of truth:
     - either hook auto-loads,
     - or page triggers `loadParents()` manually.

5. Move shared types to a dedicated `types` file
   - Example: `features/students/types/studentParentMode.ts`
   - Keeps component <-> utils dependency cleaner.

6. Keep abstractions only where duplication is clearly painful
   - Current codebase size does not strongly require highly generic form option hooks yet.
   - Prefer straightforward and slightly repetitive code until complexity really grows.
