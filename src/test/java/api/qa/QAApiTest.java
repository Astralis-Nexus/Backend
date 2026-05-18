package api.qa;

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

class QAApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("QA API should handle positive CRUD branches.")
    void qaApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/qas")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/qas/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("question", "Created question?", "answer", "Created answer", "account", Map.of("id", 1)))
                .when().post("/qas")
                .then().statusCode(201).body("id", notNullValue()).body("question", equalTo("Created question?"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("question", "Updated question?", "answer", "Updated answer", "account", Map.of("id", 1)))
                .when().put("/qas/1")
                .then().statusCode(200).body("id", equalTo(1)).body("question", equalTo("Updated question?"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/qas/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("QA API should return an empty list for empty collection.")
    void qaApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/qas")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("QA API should handle negative CRUD branches.")
    void qaApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/qas/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("question", "Missing answer?", "account", Map.of("id", 1)))
                .when().post("/qas")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("question", "Updated question?", "answer", "Updated answer", "account", Map.of("id", 1)))
                .when().put("/qas/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/qas/999")
                .then().statusCode(404);
    }
}
