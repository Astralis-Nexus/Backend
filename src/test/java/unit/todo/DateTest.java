package unit.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Todo;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DateTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("Date should accept valid LocalDate values.")
    @ValueSource(strings = {
            "2009-01-01",
            "2026-05-05",
            "2099-12-12",
            "2024-02-29"
    })
    void dateShouldAcceptValidLocalDateValues(String dateValue) {
        // When
        LocalDate date = LocalDate.parse(dateValue);
        Todo subject = new Todo();
        subject.setDate(date);

        // Then
        assertThat(subject.getDate()).isNotNull().isEqualTo(date);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Date should reject invalid date strings.")
    @ValueSource(strings = {
            "02-02/2023",
            "2023-13-01",
            "2023-01-32",
            "2023-02-29"
    })
    void dateShouldRejectInvalidDateStrings(String dateValue) {
        // Then
        assertThatThrownBy(() -> LocalDate.parse(dateValue))
                .isInstanceOf(DateTimeParseException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("Date should reject null.")
    @NullSource
    void dateShouldRejectNull(LocalDate date) {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setDate(date))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // <------------------------------ Positive ------------------------------>

    @Test
    @DisplayName("OnCreate should set date to today.")
    void onCreateShouldSetDateToToday() throws Exception {
        // Given
        Todo subject = new Todo();
        Method onCreate = Todo.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);

        // When
        onCreate.invoke(subject);

        // Then
        assertThat(subject.getDate()).isEqualTo(LocalDate.now());
    }
}
