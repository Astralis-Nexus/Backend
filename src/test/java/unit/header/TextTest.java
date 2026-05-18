package unit.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Header;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Text should accept valid lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Welcome!",
            "Line one\nLine two"
    })
    void textShouldAcceptValidLengths(String text) {
        // When
        Header subject = new Header();
        subject.setText(text);

        // Then
        assertThat(subject.getText()).isEqualTo(text).isNotBlank().hasSizeBetween(1, 80);
        assertThat(subject.getText() != null
                && !subject.getText().isBlank()
                && !subject.getText().isEmpty()
                && subject.getText().length() <= 80).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Text should reject invalid lengths.")
    @NullAndEmptySource
    @ValueSource(strings = {
            "",
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void textShouldRejectInvalidLengths(String text) {
        // Given
        Header subject = new Header();

        // Then
        assertThatThrownBy(() -> subject.setText(text))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
