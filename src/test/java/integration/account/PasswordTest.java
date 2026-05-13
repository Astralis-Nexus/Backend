package integration.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import integration.BaseIntegrationTest;
import persistence.model.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("AccountDAO should persist accounts with valid password lengths.")
    @ValueSource(strings = {
            "AAAAAAAA", // White box value: min boundary
            "AAAAAAAAA", // White box value: just above min
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "P@ssw0rd2026"
    })
    void createShouldPersistAccountsWithValidPasswordLengths(String password) {
        // Given
        String username = "integration-user";
        Account account = new Account(username, password, regularRole);

        // When
        Account created = accountDAO.create(account);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getPassword()).isEqualTo(password).hasSizeBetween(8, 128);
        assertThat(accountDAO.verifyLogin(username, password)).isNotNull();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("AccountDAO should reject accounts with invalid passwords.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "   ", // White box value: blank branch
            "A",
            "AA",
            "AAAA",
            "AAAAAA",
            "AAAAAAA", // White box value: just below min
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectAccountsWithInvalidPasswords(String password) {
        // Given
        String username = "invalid-password-user";

        // Then
        assertThatThrownBy(() -> accountDAO.create(new Account(username, password, regularRole)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("AccountDAO should verify login based on username and password.")
    @CsvSource({
            "matching-password-user, P@ssw0rd2026, true, true",
            "wrong-password-user, WrongPassword, true, false",
            "missing-user, P@ssw0rd2026, false, false"
    })
    void verifyLoginShouldReturnExpectedResult(
            String username,
            String password,
            boolean shouldCreateAccount,
            boolean shouldFindAccount
    ) {
        // Given
        if (shouldCreateAccount) {
            accountDAO.create(new Account(username, "P@ssw0rd2026", regularRole));
        }

        // When
        Account result = accountDAO.verifyLogin(username, password);

        // Then
        assertThat(result != null).isEqualTo(shouldFindAccount);
    }
}
