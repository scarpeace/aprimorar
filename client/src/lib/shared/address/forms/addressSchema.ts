import { z } from "zod/v4";

export const addressFormSchema = z.object({
  rua: z.string().min(1, { message: "Rua é obrigatória" }),
  numero: z.string().optional(),
  bairro: z.string().min(1, { message: "Bairro é obrigatório" }),
  complemento: z.string().min(1, { message: "Complemento é obrigatório" }).optional(),
  cidade: z.string().min(1, { message: "Cidade é obrigatória" }),
  estado: z.string().min(1, { message: "Estado é obrigatório" }),
  cep: z.string().min(1, { message: "CEP é obrigatório" }),
});

export type AddressInputSchema = z.input<typeof addressFormSchema>;
