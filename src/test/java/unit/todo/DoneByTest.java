package unit.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Todo;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DoneByTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("DoneBy should accept valid lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "support_agent_02"
    })
    void doneByShouldAcceptValidLengths(String doneBy) {
        // When
        Todo subject = new Todo();
        subject.setDone_by(doneBy);

        // Then
        assertThat(subject.getDone_by()).isEqualTo(doneBy).isNotBlank().hasSizeBetween(1, 30);
        assertThat(subject.getDone_by() != null
                && !subject.getDone_by().isBlank()
                && subject.getDone_by().length() >= 1
                && subject.getDone_by().length() <= 30).isTrue();
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("DoneBy should reject invalid lengths.")
    @ValueSource(strings = {
            "",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void doneByShouldRejectInvalidLengths(String doneBy) {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setDone_by(doneBy))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ Edge cases ------------------------------

    @ParameterizedTest
    @DisplayName("DoneBy should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void doneByShouldRejectNullEmptyAndBlankValues(String doneBy) {
        // Given
        Todo subject = new Todo();

        // Then
        assertThatThrownBy(() -> subject.setDone_by(doneBy))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // <------------------------------ Positive ------------------------------>

    @Test
    @DisplayName("OnUpdate should set done by to the current account username.")
    void onUpdateShouldSetDoneByToCurrentAccountUsername() throws Exception {
        // Given
        Account account = new Account("PlayerOne");
        Todo subject = new Todo();
        subject.setAccount(account);
        subject.setDone_by("OldUser");
        Method onUpdate = Todo.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);

        // When
        onUpdate.invoke(subject);

        // Then
        assertThat(subject.getDone_by()).isEqualTo("PlayerOne");
    }

}
