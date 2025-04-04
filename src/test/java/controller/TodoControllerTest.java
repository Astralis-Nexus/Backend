package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Role;
import persistence.model.Todo;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Creating an todo")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Todo(LocalDate.now(), "My Task", false, new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .when()
                .post("/todos")
                .then()
                .statusCode(200)
                .body("description", equalTo("My Task"))
                .body("status", equalTo(false))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
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
                .body("description", equalTo("My Task"))
                .body("status", equalTo(false))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Update todo")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Todo(LocalDate.now(), "My Task2", false, new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .pathParam("id", 1)
                .when()
                .put("/todos/{id}")
                .then()
                .body("description", equalTo("My Task2"))
                .body("status", equalTo(false))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Delete an todo by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/todos/{id}")
                .then()
                .body("description", equalTo("My Task"))
                .body("status", equalTo(false))
                .body("account.username", equalTo("username"))
                .body("account.role.name", equalTo("REGULAR"));
    }
}