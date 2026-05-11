package unit.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Name should accept valid lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Baldur's Gate 3"
    })
    void nameShouldAcceptValidLengths(String name) {
        // When
        Game subject = new Game();
        subject.setName(name);

        // Then
        assertThat(subject.getName()).isEqualTo(name).isNotBlank().hasSizeBetween(1, 100);
        assertThat(subject.getName() != null
                && !subject.getName().isBlank()
                && subject.getName().length() >= 1
                && subject.getName().length() <= 100).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Name should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void nameShouldRejectInvalidLengths(String name) {
        // Given
        Game subject = new Game();

        // Then
        assertThatThrownBy(() -> subject.setName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Name should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void nameShouldRejectNullEmptyAndBlankValues(String name) {
        // Given
        Game subject = new Game();

        // Then
        assertThatThrownBy(() -> subject.setName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Name duplicate edge case should be detected.")
    void duplicateNameShouldBeDetected() {
        // When
        Game existing = new Game();
        existing.setName("BaldursGate3");
        Game duplicate = new Game();
        duplicate.setName("BaldursGate3");

        // Then
        assertThat(duplicate.getName()).isEqualTo(existing.getName());
    }
}
