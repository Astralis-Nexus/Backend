package api.header;

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

class HeaderApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Header API should handle positive CRUD branches.")
    void headerApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/headers")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/headers/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("text", "Created header", "role", Map.of("name", "REGULAR")))
                .when().post("/headers")
                .then().statusCode(201).body("id", notNullValue()).body("text", equalTo("Created header"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("text", "Updated header", "role", Map.of("name", "REGULAR")))
                .when().put("/headers/1")
                .then().statusCode(200).body("id", equalTo(1)).body("text", equalTo("Updated header"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/headers/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Header API should return an empty list for empty collection.")
    void headerApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/headers")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Header API should handle negative CRUD branches.")
    void headerApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/headers/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("text", "Missing role"))
                .when().post("/headers")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("text", "Updated header", "role", Map.of("name", "REGULAR")))
                .when().put("/headers/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/headers/999")
                .then().statusCode(404);
    }
}
