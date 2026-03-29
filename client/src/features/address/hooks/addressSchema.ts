import { addressRequestDTOStateEnum } from "@/kubb";
import z from "zod";

export const addressInputSchema = z.object({
  street: z.string().min(1, { message: "Rua é obrigatória" }),
  number: z.string().min(1, { message: "Número é obrigatório" }),
  complement: z.string().min(1, { message: "Complemento é obrigatório" }),
  district: z.string().min(1, { message: "Bairro é obrigatório" }),
  city: z.string().min(1, { message: "Cidade é obrigatória" }),
  state: z.enum(addressRequestDTOStateEnum),
  zip: z.string().min(1, { message: "CEP é obrigatório" }),
});

export const addressResponseSchema = z.object({
  street: z.string(),
  number: z.string(),
  complement: z.string().nullable(),
  district: z.string(),
  city: z.string(),
  state: z.enum(addressRequestDTOStateEnum),
  zip: z.string(),
});

export type AddressInputSchema = z.input<typeof addressInputSchema>;
export type AddressResponseSchema = z.input<typeof addressResponseSchema>;
