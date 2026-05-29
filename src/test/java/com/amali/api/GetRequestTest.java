package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("GET Requests")
public class GetRequestTest extends BaseTest {

    @Test
    @DisplayName("GET all posts returns 200 with non-empty array")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that fetching all posts returns HTTP 200 and a JSON array with content")
    void getAllPostsReturns200() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .header("Content-Type", containsString("charset=utf-8"))
                .body("$", hasSize(greaterThan(0)))
                .body("[0].userId", notNullValue())
                .body("[0].id", notNullValue())
                .body("[0].title", not(emptyString()))
                .body("[0].body", not(emptyString()));
    }

    @Test
    @DisplayName("GET /posts/1 returns correct post with valid schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that fetching a specific post returns expected fields and passes JSON schema validation")
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
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test
    @DisplayName("GET /posts/99999 returns 404 for non-existent post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that a request for a non-existent resource returns HTTP 404")
    void getNonExistentPostReturns404() {
        given()
            .when()
                .get("/posts/99999")
            .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /posts?userId=1 filters posts by userId")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the userId query parameter correctly filters posts to those owned by user 1")
    void getPostsByUserIdFilter() {
        given()
            .queryParam("userId", 1)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("userId", everyItem(equalTo(1)));
    }
}
