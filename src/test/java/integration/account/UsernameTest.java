package integration.account;

import integration.BaseIntegrationTest;
import jakarta.persistence.PersistenceException;
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
            "A",
            "AA",
            "AAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            " PlayerOne",
            "PlayerOne ",
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
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    void createShouldRejectAccountsWithInvalidUsernames(String username) {
        // Given
        Account account = new Account();
        account.setPassword("P@ssw0rd2026");
        account.setRole(regularRole);

        // Then
        assertThatThrownBy(() -> account.setUsername(username))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("AccountDAO should reject duplicate usernames.")
    void createShouldRejectDuplicateUsername() {
        // Given
        accountDAO.create(new Account("PlayerOne2026DK", "P@ssw0rd2026", regularRole));
        Account duplicateAccount = new Account("PlayerOne2026DK", "P@ssw0rd2026", regularRole);
        // Then
        assertThatThrownBy(() -> accountDAO.create(duplicateAccount))
                .isInstanceOf(RuntimeException.class);
    }

    // ------------------------------ White box positive branches ------------------------------

    @Test
    @DisplayName("AccountDAO should keep existing username and password when update values are null.")
    void whiteBoxUpdateShouldKeepExistingValuesWhenUsernameAndPasswordAreNull() {
        // Given
        Account created = accountDAO.create(new Account("UpdatePlayer", "P@ssw0rd2026", regularRole));
        Account partialUpdate = new Account(
                null,
                created.getId(),
                null, // White box: DAO.update Account username null branch.
                null, // White box: DAO.update Account password null branch.
                regularRole,
                null,
                null,
                null,
                null
        );
        // When
        Account updated = accountDAO.update(partialUpdate);
        // Then
        assertThat(updated.getUsername()).isEqualTo("UpdatePlayer");
        assertThat(updated.getPassword()).isEqualTo("P@ssw0rd2026");
    }

    // ------------------------------ White box negative branches ------------------------------

    @Test
    @DisplayName("AccountDAO should reject null update entity.")
    void whiteBoxUpdateShouldRejectNullEntity() {
        // Then
        assertThatThrownBy(() -> accountDAO.update(null)) // White box: DAO.update entity null branch.
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AccountDAO should wrap persistence errors when update violates username uniqueness.")
    void whiteBoxUpdateShouldWrapPersistenceException() {
        // Given
        Account firstAccount = accountDAO.create(new Account("ExistingPlayer", "P@ssw0rd2026", regularRole));
        Account secondAccount = accountDAO.create(new Account("UpdateTarget", "P@ssw0rd2026", regularRole));
        Account duplicateUpdate = new Account(
                null,
                secondAccount.getId(),
                firstAccount.getUsername(), // White box: DAO.update PersistenceException catch branch.
                "P@ssw0rd2026",
                regularRole,
                null,
                null,
                null,
                null
        );

        // Then
        assertThatThrownBy(() -> accountDAO.update(duplicateUpdate))
                .isInstanceOf(PersistenceException.class)
                .hasMessageContaining("Error updating");
    }
}
