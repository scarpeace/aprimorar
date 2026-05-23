package aprimorar.enums;

import static org.assertj.core.api.Assertions.assertThat;

import aprimorar.registration.employee.api.contract.DutyEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DutyTest {

    @Test
    @DisplayName("should have correct descriptions")
    void shouldHaveCorrectDescriptions() {
        assertThat(DutyEnum.TEACHER.getDescription()).isEqualTo("Professor");
        assertThat(DutyEnum.ADM.getDescription()).isEqualTo("Administrativo");
        assertThat(DutyEnum.THERAPIST.getDescription()).isEqualTo("Terapeuta");
        assertThat(DutyEnum.MENTOR.getDescription()).isEqualTo("Mentor");
        assertThat(DutyEnum.SYSTEM.getDescription()).isEqualTo("Sistema");
    }
}
