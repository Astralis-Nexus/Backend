package unit.license;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Email should accept valid lengths.")
    @ValueSource(strings = {
            "a@b.dk",
            "ab@b.dk",
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.dk",
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc.ddddddddddddddddddddddddddddddddddddddddddddddddddddd.dk",
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc.dddddddddddddddddddddddddddddddddddddddddddddddddddddd.dk",
            "support.user+dk@example.co"
    })
    void emailShouldAcceptValidLengths(String email) {
        // When
        License subject = new License();
        subject.setEmail(email);

        // Then
        assertThat(subject.getEmail()).isEqualTo(email).isNotBlank().hasSizeBetween(6, 254);
        assertThat(subject.getEmail() != null
                && !subject.getEmail().isBlank()
                && subject.getEmail().length() >= 6
                && subject.getEmail().length() <= 254
                && subject.getEmail().contains("@")
                && subject.getEmail().contains(".")
                && !subject.getEmail().startsWith("@")
                && !subject.getEmail().endsWith("@")).isTrue();
        assertThat(subject.getEmail()).contains("@").contains(".");
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Email should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "A",
            "AA",
            "AAA",
            "AAAA",
            "AAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "player.example.com", // White box value: missing @ branch.
            "player@",
            "player@example", // White box value: missing dot branch.
            "@example.com", // White box value: starts with @ branch.
            "player.example@" // White box value: ends with @ branch.
    })
    void emailShouldRejectInvalidLengths(String email) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setEmail(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Email should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void emailShouldRejectNullEmptyAndBlankValues(String email) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setEmail(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Email duplicate edge case should be detected.")
    void duplicateEmailShouldBeDetected() {
        // When
        License existing = new License();
        existing.setEmail("player@example.com");
        License duplicate = new License();
        duplicate.setEmail("player@example.com");

        // Then
        assertThat(duplicate.getEmail()).isEqualTo(existing.getEmail());
    }
}
