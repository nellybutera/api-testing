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
@Feature("GET Requests")
@Owner("Nelly")
public class GetRequestTest extends BaseTest {

    @Test
    @Story("List All Posts")
    @DisplayName("GET /posts returns 200 with exactly 100 posts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies status code, Content-Type, charset, and that the full dataset of 100 posts is returned")
    void getAllPostsReturns200() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .header("Content-Type", containsString("charset=utf-8"))
                .body("$", hasSize(100))
                .body("[0].userId", notNullValue())
                .body("[0].id", notNullValue())
                .body("[0].title", not(emptyString()))
                .body("[0].body", not(emptyString()));
    }

    @Test
    @Story("Get Single Post")
    @DisplayName("GET /posts/1 returns correct post, passes schema, responds within 3s")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies fields, JSON schema validation, and response time for a single post")
    void getSinglePostReturns200() {
        given()
            .when()
                .get("/posts/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyString()))
                .body("body", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("Boundary Validation")
    @DisplayName("GET /posts/100 returns 200 — upper boundary valid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies the last valid post ID (100) returns HTTP 200 with a well-formed response")
    void getLastValidPostReturns200() {
        given()
            .when()
                .get("/posts/100")
            .then()
                .statusCode(200)
                .body("id", equalTo(100))
                .body("title", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test
    @Story("Boundary Validation")
    @DisplayName("GET /posts/101 returns 404 — first ID over upper boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 101, one above the last seeded post, returns HTTP 404")
    void getFirstOverBoundaryReturns404() {
        given()
            .when()
                .get("/posts/101")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Boundary Validation")
    @DisplayName("GET /posts/0 returns 404 — invalid lower boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 0 (no post has id=0) returns HTTP 404")
    void getZeroIdReturns404() {
        given()
            .when()
                .get("/posts/0")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("GET /posts/99999 returns 404 — far out-of-range ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that a completely out-of-range ID returns HTTP 404")
    void getNonExistentPostReturns404() {
        given()
            .when()
                .get("/posts/99999")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /posts?userId=1 returns exactly 10 posts all owned by user 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies userId filter returns the correct count (10) and every item belongs to the requested user")
    void getPostsByUserIdFilter() {
        given()
            .queryParam("userId", 1)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .body("$", hasSize(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    @Story("Security Headers")
    @DisplayName("GET /posts response includes CORS and security headers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that Access-Control-Allow-Credentials and X-Content-Type-Options headers are present")
    void getAllPostsHasSecurityHeaders() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .header("Access-Control-Allow-Credentials", equalTo("true"))
                .header("X-Content-Type-Options", equalTo("nosniff"));
    }
}
