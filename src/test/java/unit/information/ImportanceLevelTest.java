package unit.information;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Information;
import persistence.model.Information.ImportanceLevel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportanceLevelTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("ImportanceLevel should accept valid enum values.")
    @EnumSource(value = ImportanceLevel.class, names = {"LOW", "MEDIUM", "HIGH"})
    void importanceLevelShouldAcceptValidEnumValues(ImportanceLevel importanceLevel) {
        // When
        Information subject = new Information();
        subject.setImportanceLevel(importanceLevel);

        // Then
        assertThat(subject.getImportanceLevel()).isNotNull().isEqualTo(importanceLevel);
        assertThat(java.util.Arrays.asList(ImportanceLevel.values()).contains(importanceLevel)).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("ImportanceLevel should reject invalid enum values.")
    @ValueSource(strings = {
            "low",
            "URGENT",
            "CRITICAL"
    })
    void importanceLevelShouldRejectInvalidEnumValues(String importanceLevel) {
        // Then
        assertThatThrownBy(() -> ImportanceLevel.valueOf(importanceLevel))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(importanceLevel != null
                && java.util.Arrays.stream(ImportanceLevel.values())
                .map(Enum::name)
                .anyMatch(importanceLevel::equals)).isFalse();
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("ImportanceLevel should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void importanceLevelShouldRejectNullEmptyAndBlankValues(String importanceLevel) {
        // Then
        assertThat(importanceLevel).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
        assertThat(importanceLevel != null
                && java.util.Arrays.stream(ImportanceLevel.values())
                .map(Enum::name)
                .anyMatch(importanceLevel::equals)).isFalse();
    }

    @Test
    @DisplayName("ImportanceLevel setter should reject null.")
    void importanceLevelSetterShouldRejectNull() {
        // Given
        Information subject = new Information();

        // Then
        assertThatThrownBy(() -> subject.setImportanceLevel(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
