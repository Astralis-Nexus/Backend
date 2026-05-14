package api.role;

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

class RoleApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Role API should handle positive CRUD branches.")
    void roleApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/roles")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/roles/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "ADMIN"))
                .when().post("/roles")
                .then().statusCode(201).body("id", notNullValue()).body("name", equalTo("ADMIN"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "REGULAR"))
                .when().put("/roles/1")
                .then().statusCode(200).body("id", equalTo(1)).body("name", equalTo("REGULAR"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/roles/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Role API should return an empty list for empty collection.")
    void roleApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/roles")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Role API should handle negative CRUD branches.")
    void roleApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/roles/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of())
                .when().post("/roles")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "REGULAR"))
                .when().put("/roles/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/roles/999")
                .then().statusCode(404);
    }
}
