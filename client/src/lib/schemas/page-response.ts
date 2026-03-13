import z from "zod";

export const pageSchema = <T extends z.ZodTypeAny>(itemSchema: T) => (
    z.object({
        content: z.array(itemSchema),
        page: {
            number: z.number(),
            size: z.number(),
            totalElements: z.number(),
            totalPages: z.number(),
            first: z.boolean(),
            last: z.boolean(),
        }
    })
);

export type PageResponse<T> = {
    content: T[]
    page: {
        number: number
        size: number
        totalElements: number
        totalPages: number
        first: boolean
        last: boolean
    }
}