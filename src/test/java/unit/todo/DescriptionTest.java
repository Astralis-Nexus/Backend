package unit.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DescriptionTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Description should accept valid lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void descriptionShouldAcceptValidLengths(String description) {
        // When
        Todo subject = new Todo();
        subject.setDescription(description);

        // Then
        assertThat(subject.getDescription()).isEqualTo(description).isNotBlank().hasSizeBetween(1, 255);
        assertThat(subject.getDescription() != null
                && !subject.getDescription().isBlank()
                && subject.getDescription().length() >= 1
                && subject.getDescription().length() <= 255).isTrue();
    }

    @ParameterizedTest
    @DisplayName("Description should accept valid special edge cases.")
    @ValueSource(strings = {
            "GH-42 Fix checkout",
            "Line one\nLine two"
    })
    void descriptionShouldAcceptValidSpecialEdgeCases(String description) {
        // When
        Todo subject = new Todo();
        subject.setDescription(description);

        // Then
        assertThat(subject.getDescription()).isEqualTo(description).isNotBlank().hasSizeBetween(1, 255);
        assertThat(subject.getDescription() != null
                && !subject.getDescription().isBlank()
                && subject.getDescription().length() >= 1
                && subject.getDescription().length() <= 255).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Description should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void descriptionShouldRejectInvalidLengths(String description) {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Description should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void descriptionShouldRejectNullEmptyAndBlankValues(String description) {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
