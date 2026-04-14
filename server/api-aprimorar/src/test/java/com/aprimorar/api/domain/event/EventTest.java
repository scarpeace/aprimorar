package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EventTest {

    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
    private static final Instant CURRENT_TIME = Instant.parse("2026-03-24T10:00:00Z");

    @Nested
    @DisplayName("Validation methods")
    class ValidationMethods {

        @Test
        @DisplayName("should set title when value is valid")
        void shouldSetTitleWhenValueIsValid() {
            // Arrange
            Event input = new Event();
            String expected = "Aula de matemática";

            // Act
            input.setTitle(expected);

            // Assert
            assertThat(input.getTitle()).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when title is blank")
        void shouldThrowWhenTitleIsBlank() {
            // Arrange
            Event input = new Event();

            // Act + Assert
            assertThatThrownBy(() -> input.setTitle(" "))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Título do evento é obrigatório");
        }

        @Test
        @DisplayName("should set end date when value is valid")
        void shouldSetEndDateWhenValueIsValid() {
            // Arrange
            Event input = new Event();
            Instant expected = EVENT_END;
            input.setStartDate(EVENT_START);

            // Act
            input.setEndDateTime(expected);

            // Assert
            assertThat(input.getEndDateTime()).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when end date is before start date")
        void shouldThrowWhenEndDateIsBeforeStartDate() {
            // Arrange
            Event input = new Event();
            input.setStartDate(EVENT_END);

            // Act + Assert
            assertThatThrownBy(() -> input.setEndDateTime(EVENT_START))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
        }

        @Test
        @DisplayName("should set price when value is valid")
        void shouldSetPriceWhenValueIsValid() {
            // Arrange
            Event input = new Event();
            BigDecimal expected = BigDecimal.valueOf(120);
            input.setPayment(BigDecimal.valueOf(80));

            // Act
            input.setPrice(expected);

            // Assert
            assertThat(input.getPrice()).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when price is less than payment")
        void shouldThrowWhenPriceIsLessThanPayment() {
            // Arrange
            Event input = new Event();
            input.setPayment(BigDecimal.valueOf(100));

            // Act + Assert
            assertThatThrownBy(() -> input.setPrice(BigDecimal.valueOf(80)))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que o pagamento");
        }

        @Test
        @DisplayName("should throw when price is less than fifty")
        void shouldThrowWhenPriceIsLessThanFifty() {
            // Arrange
            Event input = new Event();
            input.setPayment(BigDecimal.valueOf(40));

            // Act + Assert
            assertThatThrownBy(() -> input.setPrice(BigDecimal.valueOf(49)))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("O valor do evento não pode ser menor que R$50,00");
        }

        @Test
        @DisplayName("should set content when value is valid")
        void shouldSetContentWhenValueIsValid() {
            // Arrange
            Event input = new Event();
            EventContent expected = EventContent.AULA;

            // Act
            input.setContent(expected);

            // Assert
            assertThat(input.getContent()).isEqualTo(expected);
        }

        @Test
        @DisplayName("should validate dates for creation when end date is in the future")
        void shouldValidateDatesForCreationWhenEndDateIsInTheFuture() {
            // Arrange
            Event input = new Event();
            input.setStartDate(EVENT_START);
            input.setEndDateTime(EVENT_END);

            // Act
            input.validateDatesForCreation(CURRENT_TIME);

            // Assert
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when end date is in the past during creation")
        void shouldThrowWhenEndDateIsInThePastDuringCreation() {
            // Arrange
            Event input = new Event();
            Instant now = EVENT_END.plusSeconds(1);
            input.setStartDate(EVENT_START);
            input.setEndDateTime(EVENT_END);

            // Act + Assert
            assertThatThrownBy(() -> input.validateDatesForCreation(now))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Data de fim do evento não pode estar no passado");
        }

        @Test
        @DisplayName("should validate edit window when deadline is still open")
        void shouldValidateEditWindowWhenDeadlineIsStillOpen() {
            // Arrange
            Event input = new Event();
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60);
            input.setStartDate(EVENT_START);
            input.setEndDateTime(EVENT_END);

            // Act
            input.validateEditWindow(now);

            // Assert
            assertThat(input.getEndDateTime()).isEqualTo(EVENT_END);
        }

        @Test
        @DisplayName("should throw when edit window is closed")
        void shouldThrowWhenEditWindowIsClosed() {
            // Arrange
            Event input = new Event();
            Instant now = EVENT_END.plusSeconds(20L * 24 * 60 * 60 + 1);
            input.setStartDate(EVENT_START);
            input.setEndDateTime(EVENT_END);

            // Act + Assert
            assertThatThrownBy(() -> input.validateEditWindow(now))
                .isInstanceOf(NotAllowedToUpdateEventException.class)
                .hasMessage("A janela de 20 dias para editar as informações do evento encerrou");
        }
    }
}
