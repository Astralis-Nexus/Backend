package integration.todo;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Todo;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoneByTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("TodoDAO should persist todos with valid doneBy lengths.")
    @ValueSource(strings = {
            "A", // White box value: min boundary
            "AA", // White box value: just above min
            "AAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "support_agent_02"
    })
    void createShouldPersistTodosWithValidDoneByLengths(String doneBy) {
        // Given
        Account account = createAccount("todo-user");
        Todo todo = validTodo(account);
        todo.setDoneBy(doneBy);

        // When
        Todo created = todoDAO.create(todo);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDoneBy()).isEqualTo(doneBy).hasSizeBetween(1, 30);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("TodoDAO should reject todos with invalid doneBy values.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectTodosWithInvalidDoneByValues(String doneBy) {
        // Then
        assertThatThrownBy(() -> {
            Account account = createAccount("todo-user");
            Todo todo = validTodo(account);
            todo.setDoneBy(doneBy);
            todoDAO.create(todo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TodoDAO should set doneBy to the account username on update.")
    void updateShouldSetDoneByToCurrentAccountUsername() {
        // Given
        Account account = createAccount("PlayerOne");
        Todo created = todoDAO.create(validTodo(account));
        created.setDescription("Updated todo");

        // When
        Todo updated = todoDAO.update(created);

        // Then
        assertThat(updated.getDoneBy()).isEqualTo("PlayerOne");
    }

    private Todo validTodo(Account account) {
        Todo todo = new Todo();
        todo.setDate(LocalDate.now());
        todo.setDescription("Valid todo");
        todo.setStatus(Todo.Status.PENDING);
        todo.setSource(Todo.Source.GAMEHUB);
        todo.setDoneBy("OldUser");
        todo.setAccount(account);
        return todo;
    }
}
