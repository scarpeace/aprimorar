import { useId } from "react";

export type ToggleSwitchProps = {
  label: string;
  checked: boolean;
  setToggle: (value: boolean) => void;
  className?: string;
  tip?: string;
};

export function ToggleSwitch({
  checked,
  setToggle,
  className,
  label,
  tip,
}: ToggleSwitchProps) {
  const id = useId();

  return (
    <div className="tooltip flex" data-tip={tip}>
      <label htmlFor={id} className="label flex flex-col cursor-pointer gap-2">
        <span className="text-sm font-medium">{label}</span>
        <input
          id={id}
          className={`toggle h-6 checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800 ${className}`}
          type="checkbox"
          checked={checked}
          onChange={() => setToggle(!checked)}
        />
      </label>
    </div>
  );
}
