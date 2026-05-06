## 1. Structural Layout

- [ ] 1.1 In `client/src/features/employees/pages/EmployeeDetailPage.tsx`, wrap the main return content in `<div className="flex flex-col gap-6 max-w-7xl mx-auto">`.
- [ ] 1.2 Remove the old outer `<div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">` container to prepare for section-specific animations.

## 2. Info Section

- [ ] 2.1 Keep `EmployeeInfoSection` at the top and ensure it has no extra wrapper that breaks the `flex-col` flow.

## 3. Filters and KPIs Grouping

- [ ] 3.1 Create a new wrapper `<div className="flex flex-col z-30 gap-4 animate-[fade-up_450ms_ease-out_both]">` below the Info section.
- [ ] 3.2 Inside this wrapper, create the new filter header: `<div className="flex flex-col sm:flex-row justify-between gap-4 items-center bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm">`.
- [ ] 3.3 Add the title `<h3 className="text-lg font-bold text-base-content/80">Indicadores e Filtros</h3>` to the left side of the filter header.
- [ ] 3.4 Move the `DateRangeInput` and the clear filters `Button` inside a `<div className="flex gap-2 items-center w-full sm:w-auto">` on the right side of the filter header.
- [ ] 3.5 Move the `EmployeeKPIs` component immediately below the filter header, but still inside the `z-30` animated wrapper.

## 4. Events Table Section

- [ ] 4.1 Wrap the `EmployeeEventsTable` component in `<div className="animate-[fade-up_600ms_ease-out_both]">`.

## 5. Verification

- [ ] 5.1 Verify that the employee details page loads smoothly with the staggered fade-up animations (Info -> Filters/KPIs -> Table).
- [ ] 5.2 Verify that the date picker calendar dropdown renders correctly above other elements (z-index check).