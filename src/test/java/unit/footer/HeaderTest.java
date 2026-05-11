package unit.footer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Footer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HeaderTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Header should accept valid lengths.")
    @ValueSource(strings = {
            "AAAA",
            "AAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void headerShouldAcceptValidLengths(String header) {
        // When
        Footer subject = new Footer();
        subject.setHeader(header);

        // Then
        assertThat(subject.getHeader()).isEqualTo(header).isNotBlank().hasSizeBetween(4, 50);
        assertThat(subject.getHeader() != null
                && !subject.getHeader().isBlank()
                && subject.getHeader().length() >= 4
                && subject.getHeader().length() <= 50).isTrue();
    }

    @ParameterizedTest
    @DisplayName("Header should accept valid special edge cases.")
    @ValueSource(strings = {
            "Help!",
            "FAQ 2026"
    })
    void headerShouldAcceptValidSpecialEdgeCases(String header) {
        // When
        Footer subject = new Footer();
        subject.setHeader(header);

        // Then
        assertThat(subject.getHeader()).isEqualTo(header).isNotBlank().hasSizeBetween(4, 50);
        assertThat(subject.getHeader() != null
                && !subject.getHeader().isBlank()
                && subject.getHeader().length() >= 4
                && subject.getHeader().length() <= 50).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Header should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "A",
            "AA",
            "AAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void headerShouldRejectInvalidLengths(String header) {
        // Given
        Footer subject = new Footer();

        // Then
        assertThatThrownBy(() -> subject.setHeader(header))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Header should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void headerShouldRejectNullEmptyAndBlankValues(String header) {
        // Given
        Footer subject = new Footer();

        // Then
        assertThatThrownBy(() -> subject.setHeader(header))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
