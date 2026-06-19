import { useFormContext } from "react-hook-form";
import { Field } from "./Field";
import type { TextareaHTMLAttributes } from "react";

type Props = TextareaHTMLAttributes<HTMLTextAreaElement> & {
    name: string;
    label: string;
  };

export function TextareaInput({name,label,...props}: Props) {
  const { register, formState: { errors } } = useFormContext();

  const error = errors[name]?.message?.toString();

  return (
    <Field label={label} error={error} >
      <textarea {...register(name)} {...props} className="textarea w-full"/>
    </Field>
  );
}
