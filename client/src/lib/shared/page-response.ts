import { z } from "zod";

export const pageResponseSchema = <T extends z.ZodTypeAny>(itemSchema: T) =>
  z.object({
    content: z.array(itemSchema),
    page: z.object({
      size: z.number(),
      totalElements: z.number(),
      totalPages: z.number(),
      number: z.number(),
    }),
  });

export type PageResponse<T> = {
  content: T[];
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
};
