package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Footer;
import persistence.model.Game;
import persistence.model.Role;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all games")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/games")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an game")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Game("new-game", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .when()
                .post("/games")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(2))
                .body("name", containsString("new"))
                .body("licenses", nullValue())
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.name", equalTo("REGULAR"))
                .body("account.role.id", equalTo(1));
    }

    @Test
    @DisplayName("Get game by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/games/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("name", containsString("username"));
    }

    @Test
    @DisplayName("Update game")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Game("new-game", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .pathParam("id", 1)
                .when()
                .put("/games/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("name", containsString("new"))
                .body("licenses", nullValue())
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.name", equalTo("REGULAR"))
                .body("account.role.id", equalTo(1));
    }

    @Test
    @DisplayName("Delete an game by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/games/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("name", containsString("username"))
                .body("licenses", notNullValue())
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.name", equalTo("REGULAR"))
                .body("account.role.id", equalTo(1));
    }
}