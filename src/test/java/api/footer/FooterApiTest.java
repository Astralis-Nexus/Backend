package api.footer;

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

class FooterApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Footer API should handle positive CRUD branches.")
    void footerApiShouldHandlePositiveCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/footers")
                .then().statusCode(200).body("size()", greaterThanOrEqualTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/footers/1")
                .then().statusCode(200).body("id", equalTo(1));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("header", "News", "description", "Created footer description", "role", Map.of("name", "REGULAR")))
                .when().post("/footers")
                .then().statusCode(201).body("id", notNullValue()).body("header", equalTo("News"));

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("header", "Docs", "description", "Updated footer description", "role", Map.of("name", "REGULAR")))
                .when().put("/footers/1")
                .then().statusCode(200).body("id", equalTo(1)).body("header", equalTo("Docs"));

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/footers/1")
                .then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    @DisplayName("Footer API should return an empty list for empty collection.")
    void footerApiShouldReturnEmptyList() {
        clearDatabase();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/footers")
                .then().statusCode(200).body("", hasSize(0));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Footer API should handle negative CRUD branches.")
    void footerApiShouldHandleNegativeCrudBranches() {
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/footers/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("header", "News"))
                .when().post("/footers")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("header", "News", "description", "Missing role"))
                .when().post("/footers")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body("{\"header\":\"News\",\"description\":\"Missing role name\",\"role\":{}}")
                .when().post("/footers")
                .then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("header", "Docs", "description", "Updated footer description", "role", Map.of("name", "REGULAR")))
                .when().put("/footers/999")
                .then().statusCode(404);

        RestAssured.given().contentType(ContentType.JSON)
                .when().delete("/footers/999")
                .then().statusCode(404);
    }
}
