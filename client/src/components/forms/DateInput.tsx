import { useFormContext } from "react-hook-form";
import { Field } from "./Field";

type Props = {
  name: string;
  label: string;
};

export function DateInput({ name, label }: Props) {

  const {register,formState: { errors }} = useFormContext();
  const error = errors[name]?.message?.toString();

  return (
    <Field label={label} error={error}>
      <input type="date" className="input w-full" {...register(name)}/>
    </Field>
  );
}
