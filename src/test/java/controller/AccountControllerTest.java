package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Role;
import static org.hamcrest.Matchers.*;

class AccountControllerTest extends BaseTest {

    @Test
    @DisplayName("Get all accounts")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an account")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Account("i-username", "password", new Role(Role.RoleName.REGULAR)))
                .when()
                .post("/accounts")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(2))
                .body("username", containsString("user"))
                .body("password", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(1))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("todos", nullValue())
                .body("information", nullValue())
                .body("games", nullValue())
                .body("qas", nullValue());
    }

    @Test
    @DisplayName("Get account by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/accounts/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("username", containsString("username"))
                .body("password", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(2))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("todos", notNullValue())
                .body("information", notNullValue())
                .body("games", notNullValue())
                .body("qas", notNullValue());
    }

    @Test
    @DisplayName("Update account")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Account("new-username", "new-password", new Role(Role.RoleName.NONE)))
                .pathParam("id", 1)
                .when()
                .put("/accounts/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("username", containsString("new"))
                .body("password", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(2))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("todos", notNullValue())
                .body("information", notNullValue())
                .body("games", notNullValue())
                .body("qas", notNullValue());
    }

    @Test
    @DisplayName("Delete an account by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/accounts/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("username", containsString("username"))
                .body("password", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(2))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("todos", notNullValue())
                .body("information", notNullValue())
                .body("games", notNullValue())
                .body("qas", notNullValue());
    }
}