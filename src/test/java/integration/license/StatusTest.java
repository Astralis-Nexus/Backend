package integration.license;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;
import persistence.model.License.LicenseStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
class StatusTest extends BaseIntegrationTest {
    // ------------------------------ Positive values ------------------------------
    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with valid status enum values.")
    @EnumSource(value = LicenseStatus.class, names = {"ACTIVE", "INACTIVE"})
    void createShouldPersistLicensesWithValidStatusEnumValues(LicenseStatus status) {
        // Given
        License license = validLicense();
        license.setStatus(status);
        // When
        License created = licenseDAO.create(license);
        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo(status);
    }
    // ------------------------------ Negative values ------------------------------
    @ParameterizedTest
    @DisplayName("LicenseStatus should reject invalid enum values.")
    @ValueSource(strings = {
            "active",
            "PENDING",
            "EXPIRED",
    })
    void licenseStatusShouldRejectInvalidEnumValues(String status) {
        // Then
        assertThatThrownBy(() -> LicenseStatus.valueOf(status))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("LicenseStatus setter should reject null.")
    void licenseStatusSetterShouldRejectNull() {
        // Given
        License license = new License();
        // Then
        assertThatThrownBy(() -> license.setStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    private License validLicense() {
        License license = new License();
        license.setUsername("steam_user_42");
        license.setPassword("Lic@2026!");
        license.setEmail("player@example.com");
        license.setPcNumber(1);
        license.setStatus(LicenseStatus.ACTIVE);
        license.setGame(createGame("license-game"));
        return license;
    }
}
