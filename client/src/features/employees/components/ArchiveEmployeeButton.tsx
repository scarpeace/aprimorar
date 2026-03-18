import { Button } from "@/components/ui/button";
import { ArchiveIcon, ArchiveRestoreIcon, Loader2Icon } from "lucide-react";
import { useArchiveEmployee, useUnarchiveEmployee } from "../hooks/use-employees";

export const ArchiveEmployeeButton = ({
    employeeId,
    isArchived,
}: {
    employeeId: string;
    isArchived: boolean | null;
}) => {
    const { mutate: unarchiveEmployee, isPending: isUnarchiving } = useUnarchiveEmployee();
    const { mutate: archiveEmployee, isPending: isArchiving } = useArchiveEmployee();

    const isLoading = isArchiving || isUnarchiving;

    const handleArchive = () => {
        archiveEmployee(employeeId);
    };

    const handleUnarchive = () => {
        unarchiveEmployee(employeeId);
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