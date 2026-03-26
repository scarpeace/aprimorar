import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useHookFormMask } from "use-mask-input";
import type { AddressResponseDTO, AddressResponseDTOSchema } from "@/kubb";
import { addressFormSchema, type AddressFormInput } from "../schemas/addressFormSchema";

export function useAddressForm(address?: AddressResponseDTOSchema) {

  const form = useForm<AddressFormInput>({
    resolver: zodResolver(addressFormSchema),
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
