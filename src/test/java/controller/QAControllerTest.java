package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class QAControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all licences")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/qas")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an QA")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new QA("username", "password", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .when()
                .post("/qas")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("question", equalTo("username"))
                .body("answer", equalTo("password"))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.id", equalTo(1))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Get QA by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/qas/{id}")
                .then()
                .body("id", equalTo(1))
                .body("question", equalTo("username"))
                .body("answer", equalTo("password"))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.id", equalTo(1))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Update QA")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new QA("username2", "password2", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .pathParam("id", 1)
                .when()
                .put("/qas/{id}")
                .then()
                .body("id", equalTo(1))
                .body("question", equalTo("username2"))
                .body("answer", equalTo("password2"))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.id", equalTo(1))
                .body("account.role.name", equalTo("REGULAR"));
    }

    @Test
    @DisplayName("Delete an QA by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/qas/{id}")
                .then()
                .body("id", equalTo(1))
                .body("question", equalTo("username"))
                .body("answer", equalTo("password"))
                .body("account.id", equalTo(1))
                .body("account.username", equalTo("username"))
                .body("account.password", equalTo("password"))
                .body("account.role.id", equalTo(1))
                .body("account.role.name", equalTo("REGULAR"));
    }
}