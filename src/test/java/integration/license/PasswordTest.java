package integration.license;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with valid password lengths.")
    @ValueSource(strings = {
            "AAAAAAAA", // White box value: min boundary
            "AAAAAAAAA", // White box value: just above min
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "Lic@2026!"
    })
    void createShouldPersistLicensesWithValidPasswordLengths(String password) {
        // Given
        License license = validLicense();
        license.setPassword(password);

        // When
        License created = licenseDAO.create(license);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getPassword()).isEqualTo(password).hasSizeBetween(8, 128);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should reject licenses with invalid passwords.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "        ", // White box value: blank branch
            "A",
            "AA",
            "AAAA",
            "AAAAAA",
            "AAAAAAA", // White box value: just below min
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectLicensesWithInvalidPasswords(String password) {
        // Then
        assertThatThrownBy(() -> {
            License license = validLicense();
            license.setPassword(password);
            licenseDAO.create(license);
        }).isInstanceOf(IllegalArgumentException.class);
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
}
