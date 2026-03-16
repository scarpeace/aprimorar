import { cloneElement, isValidElement, type ReactElement, type ReactNode } from "react"

type FormFieldProps = {
  label: string
  htmlFor: string
  error?: string
  hint?: ReactNode
  children: ReactNode
  className?: string
}

function resolveAliasedClassName(className?: string) {
  if (!className) return className

  return className
    .replace(/\bapp-input\b/g, "input input-bordered w-full")
    .replace(/\bapp-select\b/g, "select select-bordered w-full")
    .replace(/\bapp-textarea\b/g, "textarea textarea-bordered min-h-[80px] w-full")
    .replace(/\s+/g, " ")
    .trim()
}

export function FormField({ label, htmlFor, error, hint, children, className }: FormFieldProps) {
  const child = isValidElement(children)
    ? (children as ReactElement<{ className?: string }>)
    : null

  const currentClassName = child?.props.className
  const resolvedClassName = resolveAliasedClassName(currentClassName)

  const control =
    child && resolvedClassName && resolvedClassName !== currentClassName
      ? cloneElement(child, { className: resolvedClassName })
      : children

  return (
    <div className={`flex flex-col gap-2 ${className ?? ""}`.trim()}>
      <label className="app-text text-sm font-semibold" htmlFor={htmlFor}>
        {label}
      </label>

      {control}

      {hint ? <p className="app-text-muted text-xs">{hint}</p> : null}
      {error ? <p className="text-xs text-error">{error}</p> : null}
    </div>
  )
}
