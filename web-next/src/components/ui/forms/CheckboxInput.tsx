import { useFormContext } from "react-hook-form";

type CheckboxInputProps = {
  name: string;
  label: string;
  disabled?: boolean;
};

export function CheckboxInput({ name, label, disabled }: Readonly<CheckboxInputProps>) {
  const { register } = useFormContext();

  return (
    <label className="label cursor-pointer justify-start gap-3">
      <input type="checkbox" className="checkbox" disabled={disabled} {...register(name)} />
      <span className="label-text">{label}</span>
    </label>
  );
}
