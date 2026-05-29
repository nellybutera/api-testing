package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("POST Requests")
@Owner("Nelly")
public class PostRequestTest extends BaseTest {

    @Test
    @Story("Create Post")
    @DisplayName("POST /posts creates a new post and returns 201")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies 201 status, echoed payload fields, assigned id=101, and Content-Type header")
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
                .header("Content-Type", containsString("application/json"))
                .body("title", equalTo("Test Post"))
                .body("body", equalTo("This is a test post body"))
                .body("userId", equalTo(1))
                .body("id", equalTo(101));
    }

    @Test
    @Story("Edge Cases")
    @DisplayName("POST /posts with partial payload still returns 201 with an ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies the API accepts a minimal payload and still assigns an id in the response")
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
    @Story("Edge Cases")
    @DisplayName("POST /posts with empty body returns 201 with an ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies the API handles an empty JSON object and still returns 201 with a simulated id")
    void createPostWithEmptyBodyReturns201() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Story("Performance")
    @DisplayName("POST /posts responds within 3 seconds")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the POST endpoint meets a 3-second response time threshold")
    void createPostResponseTimeIsAcceptable() {
        String requestBody = """
                {
                    "title": "Performance Test Post",
                    "body": "Checking response time",
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
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }
}
