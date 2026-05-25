package api.zenquotes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;

class QuoteApiTest {
    private static HttpServer zenQuotesMockServer;
    private static int mockStatus = 200;

    @BeforeAll
    static void setUp() throws IOException {
        zenQuotesMockServer = HttpServer.create(new InetSocketAddress(0), 0);
        zenQuotesMockServer.createContext("/api/today", QuoteApiTest::handleQuoteRequest);
        zenQuotesMockServer.start();

        RestAssured.baseURI = "http://localhost:" + zenQuotesMockServer.getAddress().getPort();
    }

    private static void handleQuoteRequest(HttpExchange exchange) throws IOException {
        String body = mockStatus == 200
                ? "[{\"q\":\"Perpetual optimism is a force multiplier.\",\"a\":\"Colin Powell\"}]"
                : "";

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(mockStatus, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @AfterAll
    static void tearDown() {
        if (zenQuotesMockServer != null) {
            zenQuotesMockServer.stop(0);
        }
        RestAssured.reset();
    }

    // ------------------------------ Positive ------------------------------

    @Test
    @DisplayName("ZenQuotes API mock should return quote of the day.")
    void zenQuotesApiMockShouldReturnQuoteOfTheDay() {
        mockStatus = 200;

        RestAssured.when().get("/api/today")
                .then()
                .statusCode(200)
                .body("[0].q", equalTo("Perpetual optimism is a force multiplier."))
                .body("[0].a", equalTo("Colin Powell"));
    }

    // ------------------------------ Negative ------------------------------

    @Test
    @DisplayName("ZenQuotes API mock should return an error when service fails.")
    void zenQuotesApiMockShouldReturnErrorWhenServiceFails() {
        mockStatus = 500;

        RestAssured.when().get("/api/today")
                .then()
                .statusCode(500);
    }
}
