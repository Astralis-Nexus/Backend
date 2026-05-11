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

class DoneByTest {

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
        subject.setDoneBy(doneBy);

        // Then
        assertThat(subject.getDoneBy()).isEqualTo(doneBy).isNotBlank().hasSizeBetween(1, 30);
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
        assertThatThrownBy(() -> subject.setDoneBy(doneBy))
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
        assertThatThrownBy(() -> subject.setDoneBy(doneBy))
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
        subject.setDoneBy("OldUser");
        Method onUpdate = Todo.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);

        // When
        onUpdate.invoke(subject);

        // Then
        assertThat(subject.getDoneBy()).isEqualTo("PlayerOne");
    }

}
