package api.security;

import api.BaseApiTest;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class SecurityApiTest extends BaseApiTest {

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("Security API should return a token for valid login.")
    void loginShouldReturnTokenForValidCredentials() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "seed-user", "password", "P@ssw0rd2026"))
                .when()
                .post("/security/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("username", equalTo("seed-user"))
                .body("role", equalTo("REGULAR"));
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("Security API should reject invalid login.")
    void loginShouldRejectInvalidCredentials() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "seed-user", "password", "wrong-password"))
                .when()
                .post("/security/login")
                .then()
                .statusCode(401);
    }
}
