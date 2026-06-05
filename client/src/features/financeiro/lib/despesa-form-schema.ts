import {
  despesaRequestDTOCategoryEnum,
  type DespesaRequestDTO,
  type DespesaRequestDTOCategoryEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const categoryOptions = Object.values(
  despesaRequestDTOCategoryEnum,
) as [DespesaRequestDTOCategoryEnumKey, ...DespesaRequestDTOCategoryEnumKey[]];

export const despesaFormSchema: z.ZodType<DespesaRequestDTO> = z.object({
  amount: z
    .number({ error: "O valor da despesa é obrigatório" })
    .positive({ message: "O valor deve ser maior que zero" }),
  category: z.enum(categoryOptions, { error: "A categoria da despesa é obrigatória" }),
  date: z.string().min(1, { message: "A data da despesa é obrigatória" }),
  description: z.string().min(1, { message: "A descrição da despesa é obrigatória" }),
});

export type DespesaFormSchema = z.input<typeof despesaFormSchema>;
