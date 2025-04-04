package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class LicenseControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all licences")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/licences")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an licence")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new License("username2", "password", "name@email.dk", new Game("username", new Account("username", "password", new Role(Role.RoleName.REGULAR)))))
                .when()
                .post("/licences")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("username", equalTo("username2"))
                .body("password", equalTo("password"))
                .body("email", equalTo("name@email.dk"))
                .body("pcNumber", equalTo(0))
                .body("game.id", equalTo(1))
                .body("game.name", equalTo("username"))
                .body("game.account.id", equalTo(1))
                .body("game.account.username", equalTo("username"))
                .body("game.account.password", equalTo("password"))
                .body("game.account.role.id", equalTo(1))
                .body("game.account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Get licence by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/licences/{id}")
                .then()
                .body("id", equalTo(1))
                .body("username", equalTo("username"))
                .body("password", equalTo("password"))
                .body("email", equalTo("username@email.dk"))
                .body("pcNumber", equalTo(0))
                .body("game.id", equalTo(1))
                .body("game.name", equalTo("username"))
                .body("game.account.id", equalTo(1))
                .body("game.account.username", equalTo("username"))
                .body("game.account.password", equalTo("password"))
                .body("game.account.role.id", equalTo(1))
                .body("game.account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Update licence")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new License("username2", "password", "name@email.dk", 2, new Game("username", new Account("username", "password", new Role(Role.RoleName.REGULAR)))))
                .pathParam("id", 1)
                .when()
                .put("/licences/{id}")
                .then()
                .body("id", equalTo(1))
                .body("username", equalTo("username2"))
                .body("password", equalTo("password"))
                .body("email", equalTo("name@email.dk"))
                .body("pcNumber", equalTo(0))
                .body("game.id", equalTo(1))
                .body("game.name", equalTo("username"))
                .body("game.account.id", equalTo(1))
                .body("game.account.username", equalTo("username"))
                .body("game.account.password", equalTo("password"))
                .body("game.account.role.id", equalTo(1))
                .body("game.account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Delete an licence by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/licences/{id}")
                .then()
                .body("id", equalTo(1))
                .body("username", equalTo("username"))
                .body("password", equalTo("password"))
                .body("email", equalTo("username@email.dk"))
                .body("pcNumber", equalTo(0))
                .body("game.id", equalTo(1))
                .body("game.name", equalTo("username"))
                .body("game.account.id", equalTo(1))
                .body("game.account.username", equalTo("username"))
                .body("game.account.password", equalTo("password"))
                .body("game.account.role.id", equalTo(1))
                .body("game.account.role.name", equalTo("REGULAR"));
    }
}