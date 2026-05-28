import { CircleDollarSign } from "lucide-react";
import { ToggleSwitch } from "./toggle-switch";

type BeautifullToggleProps = {
  hideCharged: boolean;
  onHideChargedChange: (value: boolean) => void;
  icon?: React.ReactNode;
};

export function BeautifullToggle({ hideCharged, onHideChargedChange, icon }: BeautifullToggleProps) {
  return (
      <div className="flex items-center gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-warning/12 text-warning">
          { icon ?? <CircleDollarSign className="w-4" /> }
        </div>
        <ToggleSwitch
          toggled={hideCharged}
          setToggle={onHideChargedChange}
          label="Ocultar Pagos"
          className="border-warning/30 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
        />
      </div>
  );
}
