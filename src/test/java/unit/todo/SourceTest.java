package unit.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Todo;
import persistence.model.Todo.Source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SourceTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Source should accept valid enum values.")
    @EnumSource(value = Source.class, names = {"GAMEHUB", "STORE"})
    void sourceShouldAcceptValidEnumValues(Source source) {
        // When
        Todo subject = new Todo();
        subject.setSource(source);

        // Then
        assertThat(subject.getSource()).isNotNull().isEqualTo(source);
        assertThat(java.util.Arrays.asList(Source.values()).contains(source)).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Source should reject invalid enum values.")
    @ValueSource(strings = {
            "gamehub",
            "WEB",
            "MOBILE"
    })
    void sourceShouldRejectInvalidEnumValues(String source) {
        // Then
        assertThatThrownBy(() -> Source.valueOf(source))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(source != null
                && java.util.Arrays.stream(Source.values())
                .map(Enum::name)
                .anyMatch(source::equals)).isFalse();
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Source should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void sourceShouldRejectNullEmptyAndBlankValues(String source) {
        // Then
        assertThat(source).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
        assertThat(source != null
                && java.util.Arrays.stream(Source.values())
                .map(Enum::name)
                .anyMatch(source::equals)).isFalse();
    }
}
