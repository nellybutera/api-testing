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
@Feature("POST Requests")
public class PostRequestTest extends BaseTest {

    @Test
    @DisplayName("POST /posts creates a new post and returns 201")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a POST request returns 201 and echoes back the submitted payload with an assigned ID")
    void createPostReturns201() {
        String requestBody = """
                {
                    "title": "Test Post",
                    "body": "This is a test post body",
                    "userId": 1
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .contentType(containsString("application/json"))
                .body("title", equalTo("Test Post"))
                .body("body", equalTo("This is a test post body"))
                .body("userId", equalTo(1))
                .body("id", equalTo(101));
    }

    @Test
    @DisplayName("POST /posts returns 201 with partial payload")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the API accepts a minimal payload and still returns 201 with an ID")
    void createPostWithPartialPayload() {
        String requestBody = """
                {
                    "title": "Partial Post"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .body("title", equalTo("Partial Post"))
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("POST /posts response contains Content-Type application/json")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the POST response header includes the correct Content-Type")
    void createPostHasCorrectContentType() {
        String requestBody = """
                {
                    "title": "Header Check Post",
                    "body": "Checking headers",
                    "userId": 2
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .header("Content-Type", containsString("application/json"));
    }
}
