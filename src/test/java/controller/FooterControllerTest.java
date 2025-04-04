package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Footer;
import persistence.model.Header;
import persistence.model.Role;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class FooterControllerTest extends BaseTest{

    @Test
    @DisplayName("Get all footers")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/footers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating an footer")
    void create() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Footer("username", "password", new Role(Role.RoleName.REGULAR)))
                .when()
                .post("/footers")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(2))
                .body("header", containsString("user"))
                .body("description", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(1))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")));
    }

    @Test
    @DisplayName("Get footer by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/footers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("header", containsString("Header"))
                .body("description", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(2))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")));
    }

    @Test
    @DisplayName("Update footer")
    void update() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new Footer("new-username", "new-password", new Role(Role.RoleName.REGULAR)))
                .pathParam("id", 1)
                .when()
                .put("/footers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("header", containsString("new"))
                .body("description", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(1))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")));
    }

    @Test
    @DisplayName("Delete an footer by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/footers/{id}")
                .then()
                .body("id", greaterThanOrEqualTo(1))
                .body("header", containsString("Header"))
                .body("description", not(isEmptyOrNullString()))
                .body("role.id", lessThanOrEqualTo(2))
                .body("role.name", anyOf(equalTo("REGULAR"), equalTo("ADMIN"), equalTo("NONE")));
    }
}