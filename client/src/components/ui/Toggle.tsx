import type { ChangeEventHandler, MouseEventHandler } from "react";

type ToggleProps = {
  checked: boolean;
  onChange: ChangeEventHandler<HTMLInputElement>;
  disabled?: boolean;
  ariaLabel?: string;
  className?: string;
  onClick?: MouseEventHandler<HTMLInputElement>;
};

export function Toggle({ checked, onChange, disabled, ariaLabel, className = "", onClick }: Readonly<ToggleProps>) {
  return (
    <input
      type="checkbox"
      className={`toggle toggle-success toggle-sm ${className}`.trim()}
      checked={checked}
      disabled={disabled}
      aria-label={ariaLabel}
      onClick={onClick}
      onChange={onChange}
    />
  );
}
