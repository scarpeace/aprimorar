package aprimorar.registration.student.api;

import java.util.UUID;

public interface StudentChargeStatusPort {

    boolean hasPendingStudentCharges(UUID studentId);
}
