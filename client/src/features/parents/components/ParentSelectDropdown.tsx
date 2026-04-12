import { ButtonLink } from "@/components/ui/button";
import type { StudentFormSchema } from "@/features/students/forms/studentFormSchema";
import { useGetParentsOptions } from "@/kubb";
import { TriangleAlert, UserPlus } from "lucide-react";
import {
  Controller,
  type Control
} from "react-hook-form";

type ParentSelectDropdownProps = {
  className?: string;
  label?: string;
  control: Control<StudentFormSchema>;
  error?: string;
};

export function ParentSelectDropdown({
  control,
  error,
  className,
  label,
}: ParentSelectDropdownProps) {
  const { data: parents, isPending } = useGetParentsOptions();
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend w-full flex flex-row justify-between items-center">
        {label}
        <div className="tooltip flex" data-tip={"Novo Responsável"}>
          <ButtonLink to="/parents/new" variant="success" className="btn-xs flex">
            <UserPlus className="w-5 h-3" />
          </ButtonLink>
        </div>
      </legend>

      <Controller
        control={control}
        name="parentId"
        render={({ field }) => (
          <select
            className="select select-bordered w-full"
            disabled={isPending}
            {...field}
            value={field.value ?? ""}
          >
            <option value="">Selecione um responsável</option>
            {parents?.map((parent) => (
              <option key={parent.id} value={parent.id}>
                {parent.name}
              </option>
            ))}
          </select>
        )}
      />

      {error && (<p className="label text-error"> <TriangleAlert className="w-3 h-3" />{error}</p>
      )}
    </fieldset>
  );
}
