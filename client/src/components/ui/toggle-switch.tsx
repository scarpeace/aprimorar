export type ToggleSwitchProps = {
  toggled: boolean;
  setToggle: (value: boolean) => void;
  className?: string;
  label: string,
  tip?: string,
};

export function ToggleSwitch({ toggled, setToggle, className, label, tip }: ToggleSwitchProps) {
  return (
            <div className="tooltip" data-tip={tip}>
              <label id="showArchived" className="label">
                <input
                  id="showArchived"
                  type="checkbox"
                  checked={toggled}
                  onChange={() => setToggle(!toggled)}
                  className={`toggle checked:border-orange-500 checked:bg-orange-400 checked:text-orange-800 ${className}`}
                />
                <span className="text-sm">{label}</span>
              </label>
      </div>
  );
}
