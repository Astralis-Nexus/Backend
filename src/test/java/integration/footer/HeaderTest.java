package integration.footer;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Footer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
class HeaderTest extends BaseIntegrationTest {
    // ------------------------------ Positive values ------------------------------
    @ParameterizedTest
    @DisplayName("FooterDAO should persist footers with valid header lengths.")
    @ValueSource(strings = {
            "AAAA",
            "AAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Help!",
            "FAQ 2026",
    })
    void createShouldPersistFootersWithValidHeaderLengths(String header) {
        // Given
        Footer footer = new Footer();
        footer.setHeader(header);
        footer.setDescription("Valid footer description.");
        footer.setRole(regularRole);
        // When
        Footer created = footerDAO.create(footer);
        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getHeader()).isEqualTo(header).hasSizeBetween(4, 50);
    }
    // ------------------------------ Negative values ------------------------------
    @ParameterizedTest
    @DisplayName("FooterDAO should reject footers with invalid headers.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "A",
            "AA",
            "AAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    void createShouldRejectFootersWithInvalidHeaders(String header) {
        // Then
        assertThatThrownBy(() -> {
            Footer footer = new Footer();
            footer.setHeader(header);
            footer.setDescription("Valid footer description.");
            footer.setRole(regularRole);
            footerDAO.create(footer);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
