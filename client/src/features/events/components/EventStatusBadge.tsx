
import { CheckIcon, XIcon } from "lucide-react";

type EventStatusBadgeProps = {
  settled: boolean;
  onSettle: () => void;
};

export function EventStatusBadge({ settled, onSettle }: EventStatusBadgeProps) {

  if(!settled) {
    return (
      <div className="badge badge-warning text-xs font-bold" onClick={() => onSettle()}>
        <XIcon width={15} />
        Pendente
      </div>
    );
  }

  if (settled) {
    return (
      <div className="badge badge-success text-xs font-bold" onClick={()=> onSettle()}>
        <CheckIcon width={15} />
        Pago
      </div>
    );
  }


;
}
