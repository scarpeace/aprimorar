import z from "zod"
import { BRAZILIAN_STATE_VALUES } from "../shared/brazilianStates"
import { formatZip } from "../shared/formatter"

export const addressFormSchema = z.object({
  street: z.string().min(1, "Rua é obrigatória").max(100, "Rua pode ter somente até 100 caracteres"),
  number: z.string().min(1, "Número é obrigatório"),
  complement: z.string().optional(),
  district: z.string().min(1, "Bairro é obrigatório"),
  city: z.string().min(1, "Cidade é obrigatória"),
  state: z
    .string()
    .min(1, "Estado é obrigatório")
    .refine((value) => BRAZILIAN_STATE_VALUES.includes(value as (typeof BRAZILIAN_STATE_VALUES)[number]), "Estado inválido"),
  zip: z.string().regex(/^\d{5}-?\d{3}$/, "CEP inválido"),
})

export const addressResponseSchema = z.object({
  street: z.string(),
  number: z.string(),
  complement: z.string().nullable(),
  district: z.string(),
  city: z.string(),
  state: z.string(),
  zip: z.string().transform(formatZip),
})

export type AddressFormInput = z.infer<typeof addressFormSchema>
export type AddressResponse = z.infer<typeof addressResponseSchema>
