import { useId } from "react";

const toggleVariants = {
  primary: "toggle-primary",
  secondary: "toggle-secondary",
  accent: "toggle-accent",
  neutral: "toggle-neutral",
  info: "toggle-info",
  success: "toggle-success",
  warning: "toggle-warning",
  error: "toggle-error",
} as const;

export type ToggleSwitchProps = {
  label: string;
  checked: boolean;
  setToggle: (value: boolean) => void;
  className?: string;
  tip?: string;
  variant?: keyof typeof toggleVariants;
};

export function ToggleSwitch({
  checked,
  setToggle,
  className,
  label,
  tip,
  variant = "info",
}: ToggleSwitchProps) {
  const id = useId();

  return (
    <div className="tooltip flex" data-tip={tip}>
      <label htmlFor={id} className="label flex flex-col cursor-pointer gap-2">
        <span className={`text-sm font-medium`}>{label}</span>
        <input
          id={id}
          className={`toggle ${toggleVariants[variant]} h-6 ${className ?? ""}`}
          type="checkbox"
          checked={checked}
          onChange={(event) => setToggle(event.target.checked)}
        />
      </label>
    </div>
  );
}
