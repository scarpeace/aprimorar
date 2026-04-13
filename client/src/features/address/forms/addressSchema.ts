import { addressRequestDTOStateEnum } from "@/kubb";
import z from "zod";

export const addressFormSchema = z.object({
  street: z.string().min(1, { message: "Rua é obrigatória" }),
  district: z.string().min(1, { message: "Bairro é obrigatório" }),
  complement: z.string().min(1, { message: "Complemento é obrigatório" }).optional(),
  city: z.string().min(1, { message: "Cidade é obrigatória" }),
  state: z.enum(addressRequestDTOStateEnum),
  zip: z.string().min(1, { message: "CEP é obrigatório" }),
});

export type AddressInputSchema = z.input<typeof addressFormSchema>;
