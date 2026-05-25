package api.game;

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

class GameApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Game API should handle positive CRUD branches.")
    void gameApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/games")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/games/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "ApiGame", "account", Map.of("id", 1)))
                .when().post("/games")
                .then().statusCode(201).body("id", notNullValue()).body("name", equalTo("ApiGame"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "UpdatedGame", "account", Map.of("id", 1)))
                .when().put("/games/1")
                .then().statusCode(200).body("id", equalTo(1)).body("name", equalTo("UpdatedGame"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/games/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Game API should return an empty list for empty collection.")
    void gameApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/games")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Game API should handle negative CRUD branches.")
    void gameApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/games/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("account", Map.of("id", 1)))
                .when().post("/games")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "MissingAccount"))
                .when().post("/games")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "UnknownAccountGame", "account", Map.of("id", 999)))
                .when().post("/games")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "UpdatedGame"))
                .when().put("/games/1")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "UpdatedGame", "account", Map.of("id", 999)))
                .when().put("/games/1")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "UpdatedGame", "account", Map.of("id", 1)))
                .when().put("/games/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/games/999")
                .then().statusCode(404);
    }
}
