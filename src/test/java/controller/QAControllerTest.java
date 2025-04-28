package controller;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

class QAControllerTest extends BaseTest {

    @Test
    @DisplayName("Get all QAs")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/qas")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating a QA")
    void create() {
        int existingAccountId = 1;

        HashMap<String, Object> qaPayload = new HashMap<>();
        qaPayload.put("question", "What is Java?");
        qaPayload.put("answer", "A programming language.");

        HashMap<String, Object> accountPayload = new HashMap<>();
        accountPayload.put("id", existingAccountId);
        qaPayload.put("account", accountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(qaPayload)
                .when()
                .post("/qas")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("question", containsString("Java"))
                .body("answer", containsString("programming"))
                .body("accountId", equalTo(existingAccountId));
    }

    @Test
    @DisplayName("Get QA by id")
    void getById() {
        int existingId = 1;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", existingId)
                .when()
                .get("/qas/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(existingId))
                .body("question", notNullValue())
                .body("answer", notNullValue())
                .body("accountId", equalTo(existingId));
    }

    @Test
    @DisplayName("Update QA")
    void update() {
        int existingAccountId = 1;

        HashMap<String, Object> qaPayload = new HashMap<>();
        qaPayload.put("question", "Updated question?");
        qaPayload.put("answer", "Updated answer.");

        HashMap<String, Object> accountPayload = new HashMap<>();
        accountPayload.put("id", existingAccountId);
        qaPayload.put("account", accountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(qaPayload)
                .pathParam("id", 1)
                .when()
                .put("/qas/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("question", containsString("Updated"))
                .body("answer", containsString("Updated"))
                .body("accountId", equalTo(existingAccountId));
    }

    @Test
    @DisplayName("Delete a QA by id")
    void delete() {
        int existingId = 1;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", existingId)
                .when()
                .delete("/qas/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(existingId))
                .body("question", notNullValue())
                .body("answer", notNullValue())
                .body("accountId", equalTo(existingId));
    }
}
