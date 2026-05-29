package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("PUT Requests")
public class PutRequestTest extends BaseTest {

    @Test
    @DisplayName("PUT /posts/1 updates a post and returns 200")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a PUT request echoes back the full updated payload with the correct resource ID")
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
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("Updated body content"))
                .body("userId", equalTo(1));
    }

    @Test
    @DisplayName("PATCH /posts/1 partially updates a post and returns 200")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that a PATCH request merges only the supplied fields and returns 200")
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
                .body("title", equalTo("Patched Title"));
    }

    @Test
    @DisplayName("PUT /posts/1 response contains correct Content-Type header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the PUT response header includes application/json Content-Type")
    void updatePostHasCorrectContentType() {
        String requestBody = """
                {
                    "id": 1,
                    "title": "Header Check",
                    "body": "Checking PUT headers",
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
                .header("Content-Type", containsString("application/json"));
    }
}
