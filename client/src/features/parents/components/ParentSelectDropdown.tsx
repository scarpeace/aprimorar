import { ButtonLink } from "@/components/ui/button";
import { useGetParentsOptions } from "@/kubb";
import { TriangleAlert, UserPlus } from "lucide-react";
import { type UseFormRegisterReturn } from "react-hook-form";
import { Tooltip } from "recharts";

type ParentSelectDropdownProps = {
  registration: UseFormRegisterReturn;
  error?: string;
  className?: string;
  label?: string;
};

export function ParentSelectDropdown({
  registration,
  error,
  className,
  label,
}: ParentSelectDropdownProps) {
  const { data: parents, isPending } = useGetParentsOptions();
  return (
    <fieldset className={`fieldset ${className}`}>
      <legend className="fieldset-legend w-full flex flex-row justify-between items-center">
        {label}
        <ButtonLink
          to="/parents/new"
          variant="success"
          className="btn-xs flex"
        >
          <div className="tooltip flex" data-tip={"Novo Responsável"}>
            <UserPlus className="w-5 h-3" />
          </div>
        </ButtonLink>
      </legend>

      <select
        className="select select-bordered w-full"
        disabled={isPending}
        {...registration}
      >
        <option value="">Selecione um responsável</option>
        {parents?.map((parent) => (
          <option key={parent.id} value={parent.id}>
            {parent.name}
          </option>
        ))}
      </select>

      {error && (
        <p className="label text-error">
          <TriangleAlert className="w-3 h-3" />
          {error}
        </p>
      )}
    </fieldset>
  );
}
