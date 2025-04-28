package controller;

import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;

class GameControllerTest extends BaseTest {

    @Test
    @DisplayName("Get all games")
    void getAll() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/games")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("Creating a game")
    void create() {
        int existingAccountId = 1;

        HashMap<String, Object> gamePayload = new HashMap<>();
        gamePayload.put("name", "new-game");

        HashMap<String, Object> accountPayload = new HashMap<>();
        accountPayload.put("id", existingAccountId);
        gamePayload.put("account", accountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(gamePayload)
                .when()
                .post("/games")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("name", containsString("new"))
                .body("accountId", equalTo(existingAccountId));
    }

    @Test
    @DisplayName("Get game by id")
    void getById() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .get("/games/{id}")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("name", containsString("username"));
    }

    @Test
    @DisplayName("Update a game")
    void update() {
        int existingAccountId = 1;

        HashMap<String, Object> createPayload = new HashMap<>();
        createPayload.put("name", "initial-game");

        HashMap<String, Object> createAccountPayload = new HashMap<>();
        createAccountPayload.put("id", existingAccountId);
        createPayload.put("account", createAccountPayload);

        int createdGameId = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createPayload)
                .when()
                .post("/games")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        HashMap<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("name", "updated-game");

        HashMap<String, Object> updateAccountPayload = new HashMap<>();
        updateAccountPayload.put("id", existingAccountId);
        updatePayload.put("account", updateAccountPayload);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(updatePayload)
                .pathParam("id", createdGameId)
                .when()
                .put("/games/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdGameId))
                .body("name", containsString("updated"))
                .body("accountId", equalTo(existingAccountId));
    }

    @Test
    @DisplayName("Delete a game by id")
    void delete() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when()
                .delete("/games/{id}")
                .then()
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(1))
                .body("name", not(emptyOrNullString()))
                .body("accountId", greaterThanOrEqualTo(1));
    }
}
