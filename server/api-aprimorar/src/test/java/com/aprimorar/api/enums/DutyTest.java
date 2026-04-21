package com.aprimorar.api.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DutyTest {

    @Test
    @DisplayName("should have correct descriptions")
    void shouldHaveCorrectDescriptions() {
        assertThat(Duty.TEACHER.getDescription()).isEqualTo("Professor");
        assertThat(Duty.ADM.getDescription()).isEqualTo("Administrativo");
        assertThat(Duty.THERAPIST.getDescription()).isEqualTo("Terapeuta");
        assertThat(Duty.MENTOR.getDescription()).isEqualTo("Mentor");
        assertThat(Duty.SYSTEM.getDescription()).isEqualTo("Sistema");
    }
}
