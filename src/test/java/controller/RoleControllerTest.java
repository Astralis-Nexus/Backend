package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class RoleControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all role")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/roles")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    @DisplayName("Creating an role")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Role(Role.RoleName.ADMIN))
                .when()
                .post("/roles")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("ADMIN"))
                .body("headers", nullValue())
                .body("footers", nullValue());
    }

    @Test
    @DisplayName("Get role by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/roles/{id}")
                .then()
                .body("id", equalTo(1))
                .body("name", equalTo("REGULAR"))
                .body("headers", notNullValue())
                .body("footers", notNullValue());
    }

    @Test
    @DisplayName("Update role")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Role(Role.RoleName.ADMIN))
                .pathParam("id", 1)
                .when()
                .put("/roles/{id}")
                .then()
                .body("id", equalTo(1))
                .body("name", equalTo("ADMIN"))
                .body("headers", notNullValue())
                .body("footers", notNullValue());

    }

    @Test
    @DisplayName("Delete an role by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/roles/{id}")
                .then()
                .body("id", equalTo(1))
                .body("name", equalTo("REGULAR"))
                .body("headers", notNullValue())
                .body("footers", notNullValue());
    }

}