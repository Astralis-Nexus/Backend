package controller;

import dto.AccountDTO;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

class SecurityControllerTest extends BaseTest {

    @Test
    @DisplayName("Login with an existing account.")
    void login() {
        AccountDTO loginDTO = new AccountDTO("username", "password");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginDTO) 
                .when()
                .post("/security/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token.length()", greaterThan(10))
                .body("username", equalTo("username"))
                .body("role", equalTo("REGULAR"));
    }

}