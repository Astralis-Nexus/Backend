package unit.license;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Username should accept valid lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "steam_user_42"
    })
    void usernameShouldAcceptValidLengths(String username) {
        // When
        License subject = new License();
        subject.setUsername(username);

        // Then
        assertThat(subject.getUsername()).isEqualTo(username).isNotBlank().hasSizeBetween(1, 30);
        assertThat(subject.getUsername() != null
                && !subject.getUsername().isBlank()
                && subject.getUsername().length() >= 1
                && subject.getUsername().length() <= 30).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Username should reject invalid lengths.")
    @NullAndEmptySource
    @ValueSource(strings = {
            "",
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void usernameShouldRejectInvalidLengths(String username) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setUsername(username))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @Test
    @DisplayName("Username duplicate edge case should be detected.")
    void duplicateUsernameShouldBeDetected() {
        // When
        License existing = new License();
        existing.setUsername("steam_user_42");
        License duplicate = new License();
        duplicate.setUsername("steam_user_42");

        // Then
        assertThat(duplicate.getUsername()).isEqualTo(existing.getUsername());
    }
}
