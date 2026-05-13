package integration.license;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PcNumberTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with pcNumber values from 0 to 20.")
    @ValueSource(ints = {
            0, // White box value: min boundary
            1, // White box value: just above min
            10,
            19, // White box value: just below max
            20 // White box value: max boundary
    })
    void createShouldPersistLicensesWithPcNumberValuesFromZeroToTwenty(Integer pcNumber) {
        // Given
        License license = validLicense();
        license.setPcNumber(pcNumber);

        // When
        License created = licenseDAO.create(license);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getPcNumber()).isEqualTo(pcNumber).isBetween(0, 20);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should reject licenses with invalid pcNumber values.")
    @NullSource
    @ValueSource(ints = {
            -20,
            -2,
            -1, // White box value: just below min
            21, // White box value: just above max
            22,
            100
    })
    void createShouldRejectLicensesWithInvalidPcNumberValues(Integer pcNumber) {
        // Then
        assertThatThrownBy(() -> {
            License license = validLicense();
            license.setPcNumber(pcNumber);
            licenseDAO.create(license);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("PcNumber should reject non-integer values.")
    @ValueSource(strings = {
            "two",
            "1.5",
            "\uD83D\uDE00"
    })
    void pcNumberShouldRejectNonIntegerValues(String pcNumber) {
        // Then
        assertThat(pcNumber).isNotInstanceOf(Integer.class);
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
