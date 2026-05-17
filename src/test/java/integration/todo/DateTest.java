package integration.todo;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Todo;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------
    @ParameterizedTest
    @DisplayName("TodoDAO should persist todos with valid LocalDate values.")
    @ValueSource(strings = {
            "2009-01-01",
            "2026-05-05",
            "2099-12-12",
            "2024-02-29",
    })

    void createShouldPersistTodosWithValidLocalDateValues(String dateValue) {
        // Given
        Account account = createAccount("todo-user");
        LocalDate date = LocalDate.parse(dateValue);
        Todo todo = validTodo(account);
        todo.setDate(date);

        // When
        Todo created = todoDAO.create(todo);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDate()).isEqualTo(LocalDate.now());
    }

    // ------------------------------ Negative values ------------------------------
    @ParameterizedTest
    @DisplayName("Date should reject invalid date strings.")
    @ValueSource(strings = {
            "02-02/2023",
            "2023-13-01",
            "2023-01-32",
            "2023-02-29",
    })

    void dateShouldRejectInvalidDateStrings(String dateValue) {
        // Then
        assertThatThrownBy(() -> LocalDate.parse(dateValue))
                .isInstanceOf(DateTimeParseException.class);
    }

    @ParameterizedTest
    @DisplayName("TodoDAO should reject todos with null dates before persistence.")
    @NullSource
    void createShouldRejectTodosWithNullDates(LocalDate date) {
        // Given
        Todo todo = new Todo();

        // Then
        assertThatThrownBy(() -> todo.setDate(date))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TodoDAO should set date to today on create.")
    void createShouldSetDateToToday() {
        // Given
        Account account = createAccount("todo-user");
        Todo todo = validTodo(account);

        // When
        Todo created = todoDAO.create(todo);

        // Then
        assertThat(created.getDate()).isEqualTo(LocalDate.now());
    }
    
    private Todo validTodo(Account account) {
        Todo todo = new Todo();
        todo.setDate(LocalDate.of(2026, 5, 5));
        todo.setDescription("Valid todo");
        todo.setStatus(Todo.Status.PENDING);
        todo.setSource(Todo.Source.GAMEHUB);
        todo.setDoneBy("todo-user");
        todo.setAccount(account);
        return todo;
    }
}
