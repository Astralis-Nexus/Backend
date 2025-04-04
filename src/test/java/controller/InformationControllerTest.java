package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Information;
import persistence.model.Role;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class InformationControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all informations")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/informations")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an information")
    void create() {
      RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Information("username", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .when()
                .post("/informations")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(2))
                .body("description", containsString("user"))
                .body("account.password", not(isEmptyOrNullString()))
                .body("account.id", lessThanOrEqualTo(1))
                .body("account.username", containsString("name"))
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.role.id", lessThanOrEqualTo(2));
    }

    @Test
    @DisplayName("Get information by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/informations/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("account.password", not(isEmptyOrNullString()))
                .body("account.id", lessThanOrEqualTo(1))
                .body("account.username", containsString("name"))
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.role.id", lessThanOrEqualTo(2));
    }

    @Test
    @DisplayName("Update information")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Information("username", new Account("username", "password", new Role(Role.RoleName.REGULAR))))
                .pathParam("id", 1)
                .when()
                .put("/informations/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("account.password", not(isEmptyOrNullString()))
                .body("account.id", lessThanOrEqualTo(1))
                .body("account.username", containsString("name"))
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.role.id", lessThanOrEqualTo(2));
    }

    @Test
    @DisplayName("Delete an information by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/informations/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("account.password", not(isEmptyOrNullString()))
                .body("account.id", lessThanOrEqualTo(1))
                .body("account.username", containsString("name"))
                .body("account.role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")))
                .body("account.role.id", lessThanOrEqualTo(2));
    }
}