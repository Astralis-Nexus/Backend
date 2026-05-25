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
import persistence.model.Todo.Status;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatusTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------
    @ParameterizedTest
    @DisplayName("TodoDAO should persist todos with valid status enum values.")
    @EnumSource(value = Status.class, names = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    void createShouldPersistTodosWithValidStatusEnumValues(Status status) {
        // Given
        Account account = createAccount("todo-user");
        Todo todo = validTodo(account);
        todo.setStatus(status);

        // When
        Todo created = todoDAO.create(todo);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo(status);
    }

    // ------------------------------ Negative values ------------------------------
    @ParameterizedTest
    @DisplayName("Status should reject invalid enum values.")
    @ValueSource(strings = {
            "pending",
            "DONE",
            "CANCELLED",
    })

    void statusShouldRejectInvalidEnumValues(String status) {
        // Then
        assertThatThrownBy(() -> Status.valueOf(status))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("Status should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
    })

    void statusShouldRejectNullEmptyAndBlankValues(String status) {
        // Then
        assertThat(status).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
    }

    @Test
    @DisplayName("Status setter should reject null.")
    void statusSetterShouldRejectNull() {
        // Given
        Todo todo = new Todo();

        // Then
        assertThatThrownBy(() -> todo.setStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    private Todo validTodo(Account account) {
        Todo todo = new Todo();
        todo.setDate(LocalDate.now());
        todo.setDescription("Valid todo");
        todo.setStatus(Status.PENDING);
        todo.setSource(Todo.Source.GAMEHUB);
        todo.setDoneBy("todo-user");
        todo.setAccount(account);
        return todo;
    }
}
