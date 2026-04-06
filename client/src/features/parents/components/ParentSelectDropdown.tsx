import { FormField } from "@/components/ui/form-field";
import type { StudentFormSchema } from "@/features/students/forms/studentFormSchema";
import { useGetParentsOptions } from "@/kubb";
import { Controller, type Control } from "react-hook-form";

type ParentSelectDropdownProps = {
  control: Control<StudentFormSchema>;
  error?: string;
  className?: string;
};

export function ParentSelectDropdown({
  control,
  error,
  className,
}: ParentSelectDropdownProps) {
  const { data: parents, isPending } = useGetParentsOptions();
  return (
    <FormField
      className={className}
      label="Responsável"
      htmlFor="parentId"
      error={error}
    >
      <Controller          // <select
          //   id="parentId"
          //   className="app-input"
          //   disabled={isPending}
          //   value={field.value ?? ""}
          //   onChange={field.onChange}
          // >
          //   <option value="">Selecione um responsável</option>
          //   {parents?.map((parent) => (
          //     <option key={parent.id} value={parent.id}>
          //       {parent.name}
          //     </option>
          //   ))}
          // </select>
        control={control}
        name="parentId"
        render={({ field }) => (
          <select
            id="parentId"
            className="menu dropdown-content bg-base-100 rounded-box border border-base-200 z-1 w-full p-2 shadow-sm"
            disabled={isPending}
            value={field.value ?? ""}
            onChange={field.onChange}>
              {parents?.map((parent) => (
                <option key={parent.id} value={parent.id}>
                  {parent.name}
                </option>
              ))}
            </select>
        )}
      />
    </FormField>
  );
}
