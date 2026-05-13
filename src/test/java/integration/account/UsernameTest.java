package integration.account;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("AccountDAO should persist accounts with valid username lengths.")
    @ValueSource(strings = {
            "A", // White box value: min boundary
            "AA", // White box value: just above min
            "AAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            " PlayerOne",
            "PlayerOne "
    })
    void createShouldPersistAccountsWithValidUsernameLengths(String username) {
        // Given
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("P@ssw0rd2026");
        account.setRole(regularRole);

        // When
        Account created = accountDAO.create(account);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUsername()).isEqualTo(username).hasSizeBetween(1, 30);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("AccountDAO should reject accounts with invalid usernames.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectAccountsWithInvalidUsernames(String username) {
        // Then
        assertThatThrownBy(() -> {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword("P@ssw0rd2026");
            account.setRole(regularRole);
            accountDAO.create(account);
        })
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("AccountDAO should reject duplicate usernames.")
    void createShouldRejectDuplicateUsername() {
        // Given
        accountDAO.create(new Account("PlayerOne2026DK", "P@ssw0rd2026", regularRole));

        // Then
        assertThatThrownBy(() -> accountDAO.create(new Account("PlayerOne2026DK", "P@ssw0rd2026", regularRole)))
                .isInstanceOf(RuntimeException.class);
    }
}
