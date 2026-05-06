import type { EmployeeResponseDTO } from "@/kubb";
import { EmployeeForm } from "./EmployeeForm";

interface EmployeeEditModalProps {
  employee: EmployeeResponseDTO;
  isOpen: boolean;
  onClose: () => void;
}

export function EmployeeEditModal({ employee, isOpen, onClose }: EmployeeEditModalProps) {
  if (!isOpen) return null;

  return (
    <div className="modal modal-open">
      <div className="modal-box max-w-2xl">
        <h3 className="font-bold text-lg mb-4">Editar Colaborador</h3>
        <EmployeeForm
          initialData={employee}
          onSuccess={onClose}
          onCancel={onClose}
        />
      </div>
    </div>
  );
}
