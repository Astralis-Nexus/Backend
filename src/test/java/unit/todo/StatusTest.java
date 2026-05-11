package unit.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Todo;
import persistence.model.Todo.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatusTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Status should accept valid enum values.")
    @EnumSource(value = Status.class, names = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    void statusShouldAcceptValidEnumValues(Status status) {
        // When
        Todo subject = new Todo();
        subject.setStatus(status);

        // Then
        assertThat(subject.getStatus()).isNotNull().isEqualTo(status);
        assertThat(java.util.Arrays.asList(Status.values()).contains(status)).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Status should reject invalid enum values.")
    @ValueSource(strings = {
            "pending",
            "DONE",
            "CANCELLED"
    })
    void statusShouldRejectInvalidEnumValues(String status) {
        // Then
        assertThatThrownBy(() -> Status.valueOf(status))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(status != null
                && java.util.Arrays.stream(Status.values())
                .map(Enum::name)
                .anyMatch(status::equals)).isFalse();
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Status should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void statusShouldRejectNullEmptyAndBlankValues(String status) {
        // Then
        assertThat(status).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
        assertThat(status != null
                && java.util.Arrays.stream(Status.values())
                .map(Enum::name)
                .anyMatch(status::equals)).isFalse();
    }

    @Test
    @DisplayName("Status setter should reject null.")
    void statusSetterShouldRejectNull() {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
