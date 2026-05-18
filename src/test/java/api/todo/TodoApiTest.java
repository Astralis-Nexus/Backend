package api.todo;

import api.BaseApiTest;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class TodoApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Todo API should handle positive CRUD branches.")
    void todoApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/todos")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/todos/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of(
                        "date", LocalDate.now().toString(),
                        "description", "Created todo",
                        "status", "PENDING",
                        "source", "GAMEHUB",
                        "done_by", "api-user",
                        "account", Map.of("id", 1)
                ))
                .when().post("/todos")
                .then().statusCode(201).body("id", notNullValue()).body("description", equalTo("Created todo"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Updated todo", "status", "COMPLETED", "source", "STORE"))
                .when().put("/todos/1")
                .then().statusCode(200).body("id", equalTo(1)).body("description", equalTo("Updated todo"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/todos/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Todo API should return an empty list for empty collection.")
    void todoApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/todos")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Todo API should handle negative CRUD branches.")
    void todoApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/todos/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Missing account"))
                .when().post("/todos")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("description", "Updated todo", "status", "COMPLETED", "source", "STORE"))
                .when().put("/todos/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/todos/999")
                .then().statusCode(404);
    }
}
