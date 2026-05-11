package unit.license;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PasswordTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Password should accept valid lengths.")
    @ValueSource(strings = {
            "AAAAAAAA",
            "AAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Lic@2026!"
    })
    void passwordShouldAcceptValidLengths(String password) {
        // When
        License subject = new License();
        subject.setPassword(password);

        // Then
        assertThat(subject.getPassword()).isEqualTo(password).isNotBlank().hasSizeBetween(8, 128);
        assertThat(subject.getPassword() != null
                && !subject.getPassword().isBlank()
                && subject.getPassword().length() >= 8
                && subject.getPassword().length() <= 128).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Password should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "A",
            "AA",
            "AAAA",
            "AAAAAA",
            "AAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void passwordShouldRejectInvalidLengths(String password) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setPassword(password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Password should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "        " // White
    })
    void passwordShouldRejectNullEmptyAndBlankValues(String password) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setPassword(password))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
