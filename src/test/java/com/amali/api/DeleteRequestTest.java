package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("DELETE Requests")
public class DeleteRequestTest extends BaseTest {

    @Test
    @DisplayName("DELETE /posts/1 returns 200 with empty JSON body")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a DELETE request returns HTTP 200 and an empty JSON object as the response body")
    void deletePostReturns200() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200)
                .body(equalTo("{}"));
    }

    @Test
    @DisplayName("DELETE /posts/1 response has correct Content-Type header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the DELETE response includes the expected application/json Content-Type header")
    void deletePostHasCorrectContentType() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"));
    }

    @Test
    @DisplayName("DELETE /posts/50 returns 200")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting any valid post ID returns HTTP 200")
    void deleteAnotherPostReturns200() {
        given()
            .when()
                .delete("/posts/50")
            .then()
                .statusCode(200);
    }
}
