package integration.header;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Header;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TextTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("HeaderDAO should persist headers with valid text lengths.")
    @ValueSource(strings = {
            "A", // White box value: min boundary
            "AA", // White box value: just above min
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "Welcome!",
            "Line one\nLine two"
    })
    void createShouldPersistHeadersWithValidTextLengths(String text) {
        // Given
        Header header = new Header();
        header.setText(text);
        header.setRole(regularRole);

        // When
        Header created = headerDAO.create(header);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getText()).isEqualTo(text).hasSizeBetween(1, 80);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("HeaderDAO should reject headers with invalid texts.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectHeadersWithInvalidTexts(String text) {
        // Then
        assertThatThrownBy(() -> {
            Header header = new Header();
            header.setText(text);
            header.setRole(regularRole);
            headerDAO.create(header);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
