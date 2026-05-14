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
            "AAAAAAAA",
            "AAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "P@ssw0rd2026",
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
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "   ",
            "A",
            "AA",
            "AAAA",
            "AAAAAA",
            "AAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
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
