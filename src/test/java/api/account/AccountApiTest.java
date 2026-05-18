package api.account;

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

class AccountApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Account API should handle positive CRUD branches.")
    void accountApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/accounts")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/accounts/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "api-account", "password", "P@ssw0rd2026", "role", Map.of("name", "REGULAR")))
                .when().post("/accounts")
                .then().statusCode(201).body("id", notNullValue()).body("username", equalTo("api-account"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "default-role-account", "password", "P@ssw0rd2026"))
                .when().post("/accounts")
                .then().statusCode(201)
                .body("id", notNullValue())
                .body("username", equalTo("default-role-account"))
                .body("role.name", equalTo("REGULAR"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "updated-account", "password", "NewPass2026", "role", Map.of("name", "NONE")))
                .when().put("/accounts/1")
                .then().statusCode(200).body("id", equalTo(1)).body("username", equalTo("updated-account"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/accounts/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Account API should return an empty list for empty collection.")
    void accountApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/accounts")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Account API should handle negative CRUD branches.")
    void accountApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/accounts/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "missing-password", "role", Map.of("name", "REGULAR")))
                .when().post("/accounts")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("username", "updated-account", "password", "NewPass2026", "role", Map.of("name", "NONE")))
                .when().put("/accounts/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/accounts/999")
                .then().statusCode(404);
    }
}
