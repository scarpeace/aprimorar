import { useFormContext } from "react-hook-form";

type Props = {
  name: string;
  label: string;
};

export function CheckboxInput({ name, label,}: Props) {
  const { register } = useFormContext();

  return (
    <label className="label cursor-pointer gap-2">
      <input type="checkbox" className="checkbox" {...register(name)} />
      <span>{label}</span>
    </label>
  );
}
