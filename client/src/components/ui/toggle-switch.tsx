import { useId } from "react";

export type ToggleSwitchProps = {
  toggled: boolean;
  setToggle: (value: boolean) => void;
  className?: string;
  label: string;
  tip?: string;
};

export function ToggleSwitch({
  toggled,
  setToggle,
  className = "",
  label,
  tip,
}: ToggleSwitchProps) {
  const id = useId();

  return (
    <div className="tooltip flex" data-tip={tip}>
      <label htmlFor={id} className="label cursor-pointer gap-2">
        <input
          id={id}
          className={`toggle w-10 h-6 checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800 ${className}`}
          type="checkbox"
          checked={toggled}
          onChange={() => setToggle(!toggled)}
        />
        <span className="text-sm font-medium">{label}</span>
      </label>
    </div>
  );
}
