import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
import { useArchiveStudent, useUnarchiveStudent } from "../hooks/use-students";
import { InlineConfirmAlert } from "@/components/ui/inline-confirm-alert";

//TODO falta adicionar o comportamento de Alert para desarquivar também
export const ArchiveStudentButton = ({
    studentId,
    isArchived,
}: {
    studentId: string;
    isArchived: boolean | null;
}) => {
    const [showConfirm, setShowConfirm] = useState(false);
    const { mutate: unarchiveStudent, isPending: isUnarchiving } = useUnarchiveStudent();
    const { mutate: archiveStudent, isPending: isArchiving } = useArchiveStudent();

    const isLoading = isArchiving || isUnarchiving;

    const handleArchiveConfirm = () => {
        archiveStudent(studentId, {
            onSuccess: () => setShowConfirm(false)
        });
    };

    const handleUnarchive = () => {
        unarchiveStudent(studentId);
    };

    if (isLoading && !showConfirm) {
        return (
            <Button type="button" disabled variant="outline" className="sm:mr-auto">
                <Loader2Icon className="h-4 w-4 animate-spin" />
                Processando...
            </Button>
        );
    }

    if (isArchived) {
        return (
            <Button
                type="button"
                onClick={handleUnarchive}
                variant="outline"
                className="sm:mr-auto"
            >
                <ArchiveRestoreIcon className="h-4 w-4" />
                Desarquivar
            </Button>
        );
    }

    if (showConfirm) {
        return (
            <InlineConfirmAlert
                variant="warning"
                message="Deseja realmente arquivar este aluno?"
                confirmText="Sim, arquivar"
                cancelText="Cancelar"
                onConfirm={handleArchiveConfirm}
                onCancel={() => setShowConfirm(false)}
                className="sm:mr-auto"
                isLoading={isArchiving}
            />
        );
    }

    return (
        <Button
            type="button"
            onClick={() => setShowConfirm(true)}
            variant="warning"
            className="sm:mr-auto"
        >
            <ArchiveIcon className="h-4 w-4" />
            Arquivar
        </Button>
    );
};
