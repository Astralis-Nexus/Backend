package unit.license;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License.LicenseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatusTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseStatus should accept valid enum values.")
    @EnumSource(value = LicenseStatus.class, names = {"ACTIVE", "INACTIVE"})
    void licenseStatusShouldAcceptValidEnumValues(LicenseStatus status) {
        // When
        persistence.model.License subject = new persistence.model.License();
        subject.setStatus(status);

        // Then
        assertThat(subject.getStatus()).isNotNull().isEqualTo(status);
        assertThat(java.util.Arrays.asList(LicenseStatus.values()).contains(status)).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseStatus should reject invalid enum values.")
    @ValueSource(strings = {
            "active",
            "PENDING",
            "EXPIRED"
    })
    void licenseStatusShouldRejectInvalidEnumValues(String status) {
        // Then
        assertThatThrownBy(() -> LicenseStatus.valueOf(status))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(status != null
                && java.util.Arrays.stream(LicenseStatus.values())
                .map(Enum::name)
                .anyMatch(status::equals)).isFalse();
    }

    @Test
    @DisplayName("LicenseStatus setter should reject null.")
    void licenseStatusSetterShouldRejectNull() {
        // Given
        persistence.model.License subject = new persistence.model.License();

        // Then
        assertThatThrownBy(() -> subject.setStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
