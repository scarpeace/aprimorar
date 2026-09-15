type ProblemDetail = {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  errors?: string[];
};

export function getFriendlyErrorMessage(error: unknown): string {
  if (error instanceof Error) {
    return error.message;
  }

  if (typeof error === "string") {
    return error;
  }

  if (error && typeof error === "object") {
    const problem = error as ProblemDetail;

    return (
      problem.errors?.[0] ??
      problem.detail ??
      problem.title ??
      "Ocorreu um erro desconhecido."
    );
  }

  return "Ocorreu um erro desconhecido.";
}
