import { addressRequestDTOSchema } from "@/kubb";
import z from "zod";

export const addressFormSchema = addressRequestDTOSchema.extend({
  street: z.string().min(1, { message: "Rua do endereço é obrigatória" }),
  number: z.string().min(1, { message: "Número do endereço é obrigatório" }),
  district: z.string().min(1, { message: "Bairro do endereço é obrigatório" }),
  city: z.string().min(1, { message: "Cidade do endereço é obrigatória" }),
  zip: z.string().min(1, { message: "CEP do endereço é obrigatório" }),
});

export type AddressFormInput = z.input<typeof addressFormSchema>;
