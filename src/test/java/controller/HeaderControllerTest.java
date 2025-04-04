package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.Header;
import persistence.model.Role;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class HeaderControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all headers")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/headers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an header")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Header("By ...", new Role(Role.RoleName.REGULAR)))
                .when()
                .post("/headers")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(2))
                .body("text", containsString("By ..."))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("role.name", equalTo("REGULAR"))
                .body("role.id", equalTo(1));
    }

    @Test
    @DisplayName("Get header by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/headers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("text", containsString("username"))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("role.name", equalTo("REGULAR"))
                .body("role.id", equalTo(1));
    }

    @Test
    @DisplayName("Update header")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Header("By ...", new Role(Role.RoleName.REGULAR)))
                .pathParam("id", 1)
                .when()
                .put("/headers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("text", containsString("By ..."))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("role.name", equalTo("REGULAR"))
                .body("role.id", equalTo(1));
    }

    @Test
    @DisplayName("Delete an header by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/headers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("text", containsString("username"))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("role.name", equalTo("REGULAR"))
                .body("role.id", equalTo(1));
    }
}