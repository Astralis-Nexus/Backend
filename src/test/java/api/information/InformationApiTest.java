package api.information;

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

class InformationApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Information API should handle positive CRUD branches.")
    void informationApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/informations")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/informations/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Created information", "importanceLevel", "HIGH", "account", Map.of("id", 1)))
                .when().post("/informations")
                .then().statusCode(201).body("id", notNullValue()).body("description", equalTo("Created information"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Updated information", "importanceLevel", "MEDIUM", "account", Map.of("id", 1)))
                .when().put("/informations/1")
                .then().statusCode(200).body("id", equalTo(1)).body("description", equalTo("Updated information"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/informations/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Information API should return an empty list for empty collection.")
    void informationApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/informations")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Information API should handle negative CRUD branches.")
    void informationApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/informations/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Missing account", "importanceLevel", "HIGH"))
                .when().post("/informations")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Updated information", "importanceLevel", "MEDIUM", "account", Map.of("id", 1)))
                .when().put("/informations/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/informations/999")
                .then().statusCode(404);
    }
}
