package controller;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.hamcrest.Matchers.*;

class InformationControllerTest extends BaseTest {

    @Test
    @DisplayName("Get all informations")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/informations")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Get information by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/informations/{id}")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("accountId", lessThanOrEqualTo(10))
                .body("importanceLevel", anyOf(equalTo("HIGH"), equalTo("LOW"), equalTo("MEDIUM")));
    }

    @Test
    @DisplayName("Creating an information")
    void create() {
        int existingAccountId = 1;

        HashMap<String, Object> informationPayload = new HashMap<>();
        informationPayload.put("description", "user description");
        informationPayload.put("importanceLevel", "MEDIUM");

        HashMap<String, Object> accountPayload = new HashMap<>();
        accountPayload.put("id", existingAccountId);
        informationPayload.put("account", accountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(informationPayload)
                .when()
                .post("/informations")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("accountId", equalTo(existingAccountId))
                .body("importanceLevel", anyOf(equalTo("HIGH"), equalTo("MEDIUM"), equalTo("LOW")));
    }

    @Test
    @DisplayName("Updating an information")
    void update() {
        int existingAccountId = 1;

        HashMap<String, Object> createPayload = new HashMap<>();
        createPayload.put("description", "original description");
        createPayload.put("importanceLevel", "MEDIUM");

        HashMap<String, Object> accountPayload = new HashMap<>();
        accountPayload.put("id", existingAccountId);
        createPayload.put("account", accountPayload);

        int createdInfoId = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createPayload)
                .when()
                .post("/informations")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        HashMap<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("description", "updated description");
        updatePayload.put("importanceLevel", "HIGH");

        HashMap<String, Object> updateAccountPayload = new HashMap<>();
        updateAccountPayload.put("id", existingAccountId);
        updatePayload.put("account", updateAccountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(updatePayload)
                .pathParam("id", createdInfoId)
                .when()
                .put("/informations/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdInfoId))
                .body("description", containsString("updated"))
                .body("accountId", equalTo(existingAccountId))
                .body("importanceLevel", equalTo("HIGH"));
    }

    @Test
    @DisplayName("Delete an information by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/informations/{id}")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("description", containsString("user"))
                .body("accountId", greaterThanOrEqualTo(1))
                .body("importanceLevel", anyOf(equalTo("HIGH"), equalTo("MEDIUM"), equalTo("LOW")));
    }
}
