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

class EmailTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with valid email lengths.")
    @ValueSource(strings = {
            "a@b.dk", // White box value: min boundary
            "ab@b.dk", // White box value: just above min
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.dk",
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc.ddddddddddddddddddddddddddddddddddddddddddddddddddddd.dk", // White box value: just below max
            "user@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc.dddddddddddddddddddddddddddddddddddddddddddddddddddddd.dk", // White box value: max boundary
            "support.user+dk@example.co"
    })
    void createShouldPersistLicensesWithValidEmailLengths(String email) {
        // Given
        License license = validLicense();
        license.setEmail(email);

        // When
        License created = licenseDAO.create(license);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getEmail()).isEqualTo(email).hasSizeBetween(6, 254).contains("@").contains(".");
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should reject licenses with invalid emails.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "A",
            "AA",
            "AAA",
            "AAAA",
            "AAAAA", // White box value: just below min
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "player.example.com",
            "player@",
            "player@example", // White box value: missing dot branch
            "@example.com", // White box value: starts with @ branch
            "player.example@" // White box value: ends with @ branch
    })
    void createShouldRejectLicensesWithInvalidEmails(String email) {
        // Then
        assertThatThrownBy(() -> {
            License license = validLicense();
            license.setEmail(email);
            licenseDAO.create(license);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("LicenseDAO should reject duplicate emails.")
    void createShouldRejectDuplicateEmail() {
        // Given
        Game game = createGame("license-game");
        licenseDAO.create(validLicense(game, "steam_user_42", "player@example.com"));

        // Then
        assertThatThrownBy(() -> licenseDAO.create(validLicense(game, "steam_user_43", "player@example.com")))
                .isInstanceOf(RuntimeException.class);
    }

    private License validLicense() {
        return validLicense(createGame("license-game"), "steam_user_42", "player@example.com");
    }

    private License validLicense(Game game, String username, String email) {
        License license = new License();
        license.setUsername(username);
        license.setPassword("Lic@2026!");
        license.setEmail(email);
        license.setPcNumber(1);
        license.setStatus(License.LicenseStatus.ACTIVE);
        license.setGame(game);
        return license;
    }
}
