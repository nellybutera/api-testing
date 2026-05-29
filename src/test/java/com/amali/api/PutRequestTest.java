package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("PUT Requests")
public class PutRequestTest extends BaseTest {

    @Test
    @DisplayName("PUT /posts/1 updates a post, echoes payload, returns 200 with correct headers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies 200 status, full echoed payload, Content-Type header, and response time on a full update")
    void updatePostReturns200() {
        String requestBody = """
                {
                    "id": 1,
                    "title": "Updated Title",
                    "body": "Updated body content",
                    "userId": 1
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put("/posts/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .header("Content-Type", containsString("application/json"))
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("Updated body content"))
                .body("userId", equalTo(1))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("PATCH /posts/1 updates only the supplied field, preserves others")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that PATCH merges the supplied title while keeping the original id and body fields intact")
    void patchPostReturns200() {
        String requestBody = """
                {
                    "title": "Patched Title"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .patch("/posts/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Patched Title"))
                .body("body", not(emptyString()));
    }

    @Test
    @DisplayName("PUT /posts/1 with mismatched ID in body still uses path ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the path parameter ID takes precedence and the response id matches the path, not the body")
    void updatePostIdMatchesPath() {
        String requestBody = """
                {
                    "id": 999,
                    "title": "Mismatch Test",
                    "body": "Body content",
                    "userId": 1
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .put("/posts/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }
}
