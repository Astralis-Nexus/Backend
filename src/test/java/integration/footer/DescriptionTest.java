package integration.footer;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Footer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DescriptionTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("FooterDAO should persist footers with valid description lengths.")
    @ValueSource(strings = {
            "AAAAAAAAAA",
            "AAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Line one\nLine two",
            "<b>Support</b>",
    })

    void createShouldPersistFootersWithValidDescriptionLengths(String description) {
        // Given
        Footer footer = new Footer();
        footer.setHeader("Help");
        footer.setDescription(description);
        footer.setRole(regularRole);

        // When
        Footer created = footerDAO.create(footer);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDescription()).isEqualTo(description).hasSizeBetween(10, 255);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("FooterDAO should reject footers with invalid descriptions.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "A",
            "AA",
            "AAAAA",
            "AAAAAAAA",
            "AAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    
    void createShouldRejectFootersWithInvalidDescriptions(String description) {
        // Given
        Footer footer = new Footer();
        footer.setHeader("Help");
        footer.setRole(regularRole);

        // Then
        assertThatThrownBy(() -> footer.setDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("FooterDAO should update provided values and keep existing values when update values are null.")
    void updateShouldHandleProvidedAndNullValues() {

        // Given
        Footer created = footerDAO.create(new Footer("Help", "Original description", regularRole));
        Footer providedUpdate = new Footer(
                created.getId(),
                "News", // White box: DAO.update Footer header present branch.
                "Updated description", // White box: DAO.update Footer description present branch.
                regularRole // White box: DAO.update Footer role present branch.
        );

        // When
        Footer updated = footerDAO.update(providedUpdate);
        Footer nullUpdate = new Footer(
                updated.getId(),
                null, // White box: DAO.update Footer header null branch.
                null, // White box: DAO.update Footer description null branch.
                null // White box: DAO.update Footer role null branch.
        );
        Footer unchanged = footerDAO.update(nullUpdate);

        // Then
        assertThat(unchanged.getHeader()).isEqualTo("News");
        assertThat(unchanged.getDescription()).isEqualTo("Updated description");
        assertThat(unchanged.getRole()).isNotNull();
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("FooterDAO should handle update when footer does not already exist.")
    void updateShouldHandleMissingExistingFooter() {
        // Given
        Footer missingFooter = new Footer(
                404,
                "Help", // White box: DAO.update Footer existingFooter null branch.
                "Missing footer description",
                regularRole
        );

        // When
        Footer updated = footerDAO.update(missingFooter);

        // Then
        assertThat(updated.getId()).isNotNull();
    }
}
