import { z } from "zod/v4";
import { enderecoRequestDTOSchema } from "@/lib/api/generated/zod";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

export const enderecoFormSchema = enderecoRequestDTOSchema.extend({
  rua: requiredText("Rua"),
  numero: requiredText("Número"),
  bairro: requiredText("Bairro"),
  cidade: requiredText("Cidade"),
  estado: requiredText("Estado"),
  cep: requiredText("CEP"),
});
