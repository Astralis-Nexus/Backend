package api.license;

import api.BaseApiTest;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class LicenseApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("License API should handle positive CRUD branches.")
    void licenseApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/licences")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/licences/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "api-license",
                        "password", "P@ssw0rd2026",
                        "email", "api-license@example.com",
                        "pcNumber", 1,
                        "game", Map.of("id", 1),
                        "status", "ACTIVE"
                ))
                .when().post("/licences")
                .then().statusCode(201).body("id", notNullValue()).body("username", equalTo("api-license"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "updated-license",
                        "password", "NewPass2026",
                        "email", "updated-license@example.com",
                        "pcNumber", 1,
                        "game", Map.of("id", 1),
                        "status", "INACTIVE"
                ))
                .when().put("/licences/1")
                .then().statusCode(200).body("id", equalTo(1)).body("username", equalTo("updated-license"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/licences/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("License API should return an empty list for empty collection.")
    void licenseApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/licences")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("License API should handle negative CRUD branches.")
    void licenseApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/licences/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "missing-game"))
                .when().post("/licences")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body("{\"username\":\"missing-game-id\",\"game\":{}}")
                .when().post("/licences")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of(
                        "username", "updated-license",
                        "password", "NewPass2026",
                        "email", "updated-license@example.com",
                        "pcNumber", 1,
                        "game", Map.of("id", 1),
                        "status", "INACTIVE"
                ))
                .when().put("/licences/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/licences/999")
                .then().statusCode(404);
    }
}
