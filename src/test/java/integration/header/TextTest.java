package integration.header;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Welcome!",
            "Line one\nLine two",
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
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    void createShouldRejectHeadersWithInvalidTexts(String text) {
        // Given
        Header header = new Header();
        header.setRole(regularRole);

        // Then
        assertThatThrownBy(() -> header.setText(text))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ White box positive branches ------------------------------

    @Test
    @DisplayName("HeaderDAO should keep existing text when update text is null.")
    void whiteBoxUpdateShouldKeepExistingTextWhenTextIsNull() {
        // Given
        Header created = headerDAO.create(new Header("Original header", regularRole));
        Header partialUpdate = new Header(
                created.getId(),
                null, // White box: DAO.update Header text null branch.
                regularRole
        );
        // When
        Header updated = headerDAO.update(partialUpdate);
        // Then
        assertThat(updated.getText()).isEqualTo("Original header");
    }
}
