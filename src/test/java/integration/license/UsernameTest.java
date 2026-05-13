package integration.license;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Game;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with valid username lengths.")
    @ValueSource(strings = {
            "A", // White box value: min boundary
            "AA", // White box value: just above min
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "steam_user_42"
    })
    void createShouldPersistLicensesWithValidUsernameLengths(String username) {
        // Given
        License license = validLicense();
        license.setUsername(username);

        // When
        License created = licenseDAO.create(license);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUsername()).isEqualTo(username).hasSizeBetween(1, 30);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should reject licenses with invalid usernames.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectLicensesWithInvalidUsernames(String username) {
        // Then
        assertThatThrownBy(() -> {
            License license = validLicense();
            license.setUsername(username);
            licenseDAO.create(license);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("LicenseDAO should reject duplicate usernames.")
    void createShouldRejectDuplicateUsername() {
        // Given
        Game game = createGame("license-game");
        licenseDAO.create(validLicense(game));

        // Then
        assertThatThrownBy(() -> licenseDAO.create(validLicense(game)))
                .isInstanceOf(RuntimeException.class);
    }

    private License validLicense() {
        License license = new License();
        license.setUsername("steam_user_42");
        license.setPassword("Lic@2026!");
        license.setEmail("player@example.com");
        license.setPcNumber(1);
        license.setStatus(License.LicenseStatus.ACTIVE);
        license.setGame(createGame("license-game"));
        return license;
    }

    private License validLicense(Game game) {
        License license = new License();
        license.setUsername("steam_user_42");
        license.setPassword("Lic@2026!");
        license.setEmail("player-two@example.com");
        license.setPcNumber(1);
        license.setStatus(License.LicenseStatus.ACTIVE);
        license.setGame(game);
        return license;
    }
}
