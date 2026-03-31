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
  className,
  label,
  tip,
}: ToggleSwitchProps) {
  return (
    <div className="tooltip" data-tip={tip}>
      <label id="showArchived" className="label">
        <input
          className={`toggle w-10 h-6 checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800 ${className}`}
          id="showArchived"
          type="checkbox"
          checked={toggled}
          onChange={() => setToggle(!toggled)}
        />
        <span className="text-xs">{label}</span>
      </label>
    </div>
  );
}
