package integration.todo;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Todo;
import persistence.model.Todo.Source;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SourceTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("TodoDAO should persist todos with valid source enum values.")
    @EnumSource(value = Source.class, names = {"GAMEHUB", "STORE"})
    void createShouldPersistTodosWithValidSourceEnumValues(Source source) {
        // Given
        Account account = createAccount("todo-user");
        Todo todo = validTodo(account);
        todo.setSource(source);

        // When
        Todo created = todoDAO.create(todo);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getSource()).isEqualTo(source);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("Source should reject invalid enum values.")
    @ValueSource(strings = {
            "gamehub",
            "WEB",
            "MOBILE"
    })
    void sourceShouldRejectInvalidEnumValues(String source) {
        // Then
        assertThatThrownBy(() -> Source.valueOf(source))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("Source should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " "
    })
    void sourceShouldRejectNullEmptyAndBlankValues(String source) {
        // Then
        assertThat(source).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
    }

    @Test
    @DisplayName("Source setter should reject null.")
    void sourceSetterShouldRejectNull() {
        // Given
        Todo todo = new Todo();

        // Then
        assertThatThrownBy(() -> todo.setSource(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Todo validTodo(Account account) {
        Todo todo = new Todo();
        todo.setDate(LocalDate.now());
        todo.setDescription("Valid todo");
        todo.setStatus(Todo.Status.PENDING);
        todo.setSource(Source.GAMEHUB);
        todo.setDoneBy("todo-user");
        todo.setAccount(account);
        return todo;
    }
}
