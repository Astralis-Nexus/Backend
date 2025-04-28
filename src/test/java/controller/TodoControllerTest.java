package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dto.TodoDTO;
import persistence.model.Account;
import persistence.model.Todo;
import java.time.LocalDate;
import java.util.HashMap;
import static org.hamcrest.Matchers.equalTo;

class TodoControllerTest extends BaseTest {

    @Test
    @DisplayName("Get all todos")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating a Todo successfully")
    void create() {
        Account account = new Account();
        account.setId(1);

        TodoDTO todo = new TodoDTO();
        todo.setDate(LocalDate.now());
        todo.setDescription("My Task1");
        todo.setStatus(Todo.Status.COMPLETED);
        todo.setSource(Todo.Source.STORE);
        todo.setDone_by("John Doe");
        todo.setAccount(account);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .post("/todos")
                .then()
                .statusCode(200)
                .body("description", equalTo("My Task1"))
                .body("status", equalTo("COMPLETED"))
                .body("done_by", equalTo("John Doe"))
                .body("account.id", equalTo(1));
    }

    @Test
    @DisplayName("Get todo by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/todos/{id}")
                .then()
                .statusCode(200)
                .body("description", equalTo("My Task"))
                .body("status", equalTo("COMPLETED"))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Update todo")
    void update() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", "My Task2");
        requestBody.put("status", "COMPLETED");
        requestBody.put("source", "STORE");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(requestBody)
                .when()
                .put("/todos/{id}")
                .then()
                .statusCode(200)
                .body("description", equalTo("My Task2"))
                .body("status", equalTo("COMPLETED"))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Delete a todo by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/todos/{id}")
                .then()
                .statusCode(200)
                .body("description", equalTo("My Task"))
                .body("status", equalTo("COMPLETED"))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }
}
