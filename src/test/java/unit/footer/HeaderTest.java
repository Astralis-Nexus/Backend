package unit.footer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Footer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeaderTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Header should accept valid lengths.")
    @ValueSource(strings = {
            "AAAA",
            "AAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Help!",
            "FAQ 2026"
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

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Header should reject invalid lengths.")
    @NullAndEmptySource
    @ValueSource(strings = {
            "",
            " ",
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
}
