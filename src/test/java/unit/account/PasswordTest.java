package unit.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Password should accept valid lengths.")
    @ValueSource(strings = {
            "AAAAAAA",
            "AAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "P@ssw0rd2026"
    })
    void passwordShouldAcceptValidLengths(String password) {
        // When
        Account subject = new Account();
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
    @NullAndEmptySource
    @ValueSource(strings = {
            "",
            " ",
            "   ",
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
        Account subject = new Account();

        // Then
        assertThatThrownBy(() -> subject.setPassword(password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // <------------------------------ Positive ------------------------------>

    @Test
    @DisplayName("VerifyPassword should return true when the password matches.")
    void verifyPasswordShouldReturnTrueWhenPasswordMatches() {
        // When
        Account subject = new Account();
        subject.setPassword("P@ssw0rd2026");

        // Then
        assertThat(subject.verifyPassword("P@ssw0rd2026")).isTrue(); // White box value
    }

    // <------------------------------ Negative ------------------------------>

    @Test
    @DisplayName("VerifyPassword should return false when the password does not match.")
    void verifyPasswordShouldReturnFalseWhenPasswordDoesNotMatch() {
        // When
        Account subject = new Account();
        subject.setPassword("P@ssw0rd2026");

        // Then
        assertThat(subject.verifyPassword("WrongPassword")).isFalse(); // White box value
    }

    @Test
    @DisplayName("VerifyPassword should return false when no password is saved.")
    void verifyPasswordShouldReturnFalseWhenNoPasswordIsSaved() {
        // When
        Account subject = new Account();

        // Then
        assertThat(subject.verifyPassword("P@ssw0rd2026")).isFalse(); // White box value
    }

}
