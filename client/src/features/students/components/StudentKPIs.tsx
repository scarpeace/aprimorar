import { memo } from "react";

interface StudentKPIsProps {
  studentId: string;
}

export const StudentKPIs = memo(function StudentKPIs({ studentId }: StudentKPIsProps) {
  void studentId;

  return null;
});
