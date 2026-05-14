package integration.information;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Information;
import persistence.model.Information.ImportanceLevel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
class ImportanceLevelTest extends BaseIntegrationTest {
    // ------------------------------ Positive values ------------------------------
    @ParameterizedTest
    @DisplayName("InformationDAO should persist information with valid importance levels.")
    @EnumSource(value = ImportanceLevel.class, names = {"LOW", "MEDIUM", "HIGH"})
    void createShouldPersistInformationWithValidImportanceLevels(ImportanceLevel importanceLevel) {
        // Given
        Account account = createAccount("information-user");
        Information information = new Information();
        information.setDescription("Valid information description.");
        information.setImportanceLevel(importanceLevel);
        information.setAccount(account);
        // When
        Information created = informationDAO.create(information);
        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getImportanceLevel()).isEqualTo(importanceLevel);
    }
    // ------------------------------ Negative values ------------------------------
    @ParameterizedTest
    @DisplayName("ImportanceLevel should reject invalid enum values.")
    @ValueSource(strings = {
            "low",
            "URGENT",
            "CRITICAL",
    })
    void importanceLevelShouldRejectInvalidEnumValues(String importanceLevel) {
        // Then
        assertThatThrownBy(() -> ImportanceLevel.valueOf(importanceLevel))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @DisplayName("ImportanceLevel should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
    })
    void importanceLevelShouldRejectNullEmptyAndBlankValues(String importanceLevel) {
        // Then
        assertThat(importanceLevel).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
    }
    @Test
    @DisplayName("ImportanceLevel setter should reject null.")
    void importanceLevelSetterShouldRejectNull() {
        // Given
        Information information = new Information();
        // Then
        assertThatThrownBy(() -> information.setImportanceLevel(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
