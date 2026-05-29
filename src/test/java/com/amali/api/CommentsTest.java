package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Comments")
@Owner("Nelly")
public class CommentsTest extends BaseTest {

    @Test
    @Story("List All Comments")
    @DisplayName("GET /comments returns 200 with exactly 500 comments")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies the full comment dataset is returned with the correct count of 500 items")
    void getAllCommentsReturns200() {
        given()
            .when()
                .get("/comments")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", hasSize(500))
                .body("[0].postId", notNullValue())
                .body("[0].email", containsString("@"));
    }

    @Test
    @Story("Get Single Comment")
    @DisplayName("GET /comments/1 returns correct comment with valid email, passes schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies all comment fields, that email contains @, JSON schema, and response time")
    void getSingleCommentReturns200() {
        given()
            .when()
                .get("/comments/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("postId", equalTo(1))
                .body("name", not(emptyString()))
                .body("email", containsString("@"))
                .body("body", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/comment-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("GET /comments/501 returns 404 — first ID over boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 501, one above the last seeded comment, returns HTTP 404")
    void getCommentOverBoundaryReturns404() {
        given()
            .when()
                .get("/comments/501")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /comments?postId=1 returns exactly 5 comments all belonging to post 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies postId filter returns exactly 5 comments and every item has postId=1")
    void getCommentsByPostIdFilter() {
        given()
            .queryParam("postId", 1)
            .when()
                .get("/comments")
            .then()
                .statusCode(200)
                .body("$", hasSize(5))
                .body("postId", everyItem(equalTo(1)));
    }
}
