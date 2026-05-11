package unit.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Role;
import persistence.model.Role.RoleName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Name should accept valid enum values.")
    @EnumSource(value = RoleName.class, names = {"NONE", "REGULAR", "ADMIN"})
    void nameShouldAcceptValidEnumValues(RoleName name) {
        // When
        Role subject = new Role();
        subject.setName(name);

        // Then
        assertThat(subject.getName()).isNotNull().isEqualTo(name);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Name should reject invalid enum values.")
    @ValueSource(strings = {
            "admin",
            "SUPER_ADMIN",
            "USER"
    })
    void nameShouldRejectInvalidEnumValues(String name) {
        // Then
        assertThatThrownBy(() -> RoleName.valueOf(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Name should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void nameShouldRejectNullEmptyAndBlankValues(String name) {
        // Then
        assertThat(name).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
    }

    @Test
    @DisplayName("Name setter should reject null.")
    void nameSetterShouldRejectNull() {
        // Given
        Role subject = new Role();

        // Then
        assertThatThrownBy(() -> subject.setName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
