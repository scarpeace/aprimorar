
import type { EventResponseDTO } from "@/kubb";
import { CheckIcon, XIcon } from "lucide-react";


export function EventStatusBadge({ event }: { event: EventResponseDTO }) {

  if(!event.employeePaid || !event.studentCharged) {
    return (
      <div className="badge badge-warning text-xs font-bold">
        <XIcon width={15} />
        Pendente
      </div>
    );
  }

  if (event.employeePaid && event.studentCharged) {
    return (
      <div className="badge badge-success text-xs font-bold">
        <CheckIcon width={15} />
        Pago
      </div>
    );
  }


;
}
