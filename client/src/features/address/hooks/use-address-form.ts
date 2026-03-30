import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import { addressInputSchema, type AddressInputSchema, type AddressResponseSchema } from "./addressSchema";

export function useAddressForm(address?: AddressResponseSchema) {
  const form = useForm<AddressInputSchema>({
    resolver: zodResolver(addressInputSchema),
    mode: "onBlur",
    values: {
      street: address?.street ?? "",
      number: address?.number ?? "",
      complement: address?.complement ?? "",
      district: address?.district ?? "",
      city: address?.city ?? "",
      state: address?.state ?? "DF",
      zip: address?.zip ?? "",
    },
  });

  const registerWithMask = useHookFormMask(form.register);

  return {
    ...form,
    errors: form.formState.errors,
    registerWithMask,
  };
}
