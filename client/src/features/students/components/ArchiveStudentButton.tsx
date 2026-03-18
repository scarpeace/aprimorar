import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
import { useArchiveStudent, useUnarchiveStudent } from "../hooks/use-students";

export const ArchiveStudentButton = ({
    studentId,
    isArchived,
}: {
    studentId: string;
    isArchived: boolean | null;
}) => {
    const { mutate: unarchiveStudent, isPending: isUnarchiving } = useUnarchiveStudent();
    const { mutate: archiveStudent, isPending: isArchiving } = useArchiveStudent();

    const isLoading = isArchiving || isUnarchiving;

    const handleArchive = () => {
        if (!globalThis.confirm("Deseja realmente arquivar este aluno?")) return;
        archiveStudent(studentId);
    };

    const handleUnarchive = () => {
        unarchiveStudent(studentId);
    };

    if (isLoading) {
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

    return (
        <Button
            type="button"
            onClick={handleArchive}
            variant="warning"
            className="sm:mr-auto"
        >
            <ArchiveIcon className="h-4 w-4" />
            Arquivar
        </Button>
    );
};
