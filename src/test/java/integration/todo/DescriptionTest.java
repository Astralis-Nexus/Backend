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

class DescriptionTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("TodoDAO should persist todos with valid description lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "GH-42 Fix checkout",
            "Line one\nLine two",
    })
    void createShouldPersistTodosWithValidDescriptionLengths(String description) {
        // Given
        Account account = createAccount("todo-user");
        Todo todo = validTodo(account);
        todo.setDescription(description);
        // When
        Todo created = todoDAO.create(todo);
        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDescription()).isEqualTo(description).hasSizeBetween(1, 255);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("TodoDAO should reject todos with invalid descriptions.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    void createShouldRejectTodosWithInvalidDescriptions(String description) {
        // Given
        Todo todo = new Todo();

        // Then
        assertThatThrownBy(() -> todo.setDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ White box positive branches ------------------------------

    @Test
    @DisplayName("TodoDAO should keep existing description on null update and find an existing account.")
    void whiteBoxShouldKeepExistingDescriptionAndFindExistingAccount() {
        // Given
        Account account = createAccount("todo-update-user");
        Todo created = todoDAO.create(validTodo(account));
        Todo partialUpdate = new Todo(
                created.getId(),
                created.getDate(),
                null, // White box: DAO.update Todo description null branch.
                created.getStatus(),
                created.getSource(),
                created.getDoneBy(),
                account
        );
        // When
        Todo updated = todoDAO.update(partialUpdate);
        // Then
        assertThat(updated.getDescription()).isEqualTo("Valid todo");
        assertThat(todoDAO.getAccountById(account.getId())).isNotNull();
    }

    // ------------------------------ White box negative branches ------------------------------

    @Test
    @DisplayName("TodoDAO should return null for missing account and reject missing account relation.")
    void whiteBoxShouldHandleMissingAccountBranches() {
        // Given
        Todo todo = validTodo(null); // White box: DAO.create attachAccountWithRole sourceAccount null branch.

        // Then
        assertThat(todoDAO.getAccountById(404)).isNull(); // White box: TodoDAO missing account branch.
        assertThatThrownBy(() -> todoDAO.create(todo))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error handling entity dynamically");
    }

    private Todo validTodo(Account account) {
        Todo todo = new Todo();
        todo.setDate(LocalDate.now());
        todo.setDescription("Valid todo");
        todo.setStatus(Todo.Status.PENDING);
        todo.setSource(Todo.Source.GAMEHUB);
        todo.setDoneBy("todo-user");
        todo.setAccount(account);
        return todo;
    }
}
