import { z } from "zod/v4";

export const enderecoFormSchema = z.object({
  rua: z.string().min(1, { message: "Rua é obrigatória" }),
  numero: z.string().min(1, { message: "Número é obrigatório" }),
  bairro: z.string().min(1, { message: "Bairro é obrigatório" }),
  cidade: z.string().min(1, { message: "Cidade é obrigatória" }),
  estado: z.string().min(1, { message: "Estado é obrigatório" }),
  cep: z.string().min(1, { message: "CEP é obrigatório" }),
  complemento: z.string().optional(),
});

export type AddressInputSchema = z.input<typeof enderecoFormSchema>;
