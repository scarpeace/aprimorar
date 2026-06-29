export function isSafeRedirectPath(value: string | null | undefined): value is string {
  return !!value && value.startsWith("/") && !value.startsWith("//");
}

export function resolveRedirectPath(value: string | null | undefined) {
  return isSafeRedirectPath(value) ? value : "/";
}

