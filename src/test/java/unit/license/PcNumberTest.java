package unit.license;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PcNumberTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("PcNumber should accept values from 0 to 20.")
    @ValueSource(ints = {
            0,
            1,
            10,
            19,
            20
    })
    void pcNumberShouldAcceptValuesFromZeroToTwenty(Integer pcNumber) {
        // When
        License subject = new License();
        subject.setPcNumber(pcNumber);

        // Then
        assertThat(subject.getPcNumber()).isNotNull().isEqualTo(pcNumber).isBetween(0, 20);
        assertThat(subject.getPcNumber() != null
                && subject.getPcNumber() >= 0
                && subject.getPcNumber() <= 20).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("PcNumber should reject negative values and values above 20.")
    @ValueSource(ints = {
            -20,
            -2,
            -1,
            21,
            22,
            100
    })
    void pcNumberShouldRejectNegativeValuesAndValuesAboveTwenty(Integer pcNumber) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setPcNumber(pcNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("PcNumber should reject non-integer values.")
    @ValueSource(strings = {
            "two",
            "1.5",
            "\uD83D\uDE00"
    })
    void pcNumberShouldRejectNonIntegerValues(String pcNumber) {
        // Then
        assertThat(pcNumber).isNotInstanceOf(Integer.class);
    }

    @ParameterizedTest
    @DisplayName("PcNumber should reject null.")
    @NullSource
    void pcNumberShouldRejectNull(Integer pcNumber) {
        // Given
        License subject = new License();

        // Then
        assertThatThrownBy(() -> subject.setPcNumber(pcNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
