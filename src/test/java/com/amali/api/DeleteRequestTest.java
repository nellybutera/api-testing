package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("DELETE Requests")
public class DeleteRequestTest extends BaseTest {

    @Test
    @DisplayName("DELETE /posts/1 returns 200 with empty JSON body and correct headers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies 200 status, empty JSON object body, Content-Type header, and response time on delete")
    void deletePostReturns200() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .header("Content-Type", containsString("application/json"))
                .body(equalTo("{}"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("DELETE /posts/100 returns 200 — upper boundary post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting the last seeded post (id=100) returns 200 with an empty body")
    void deleteLastValidPostReturns200() {
        given()
            .when()
                .delete("/posts/100")
            .then()
                .statusCode(200)
                .body(equalTo("{}"));
    }

    @Test
    @DisplayName("DELETE /posts/1 response includes CORS header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the DELETE response carries the Access-Control-Allow-Credentials header")
    void deletePostHasCorsHeader() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200)
                .header("Access-Control-Allow-Credentials", equalTo("true"));
    }
}
