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
@Feature("Albums")
@Owner("Nelly")
public class AlbumsTest extends BaseTest {

    @Test
    @Story("List All Albums")
    @DisplayName("GET /albums returns 200 with exactly 100 albums")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies the full album dataset is returned with the correct count of 100 items")
    void getAllAlbumsReturns200() {
        given()
            .when()
                .get("/albums")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", hasSize(100))
                .body("[0].userId", notNullValue())
                .body("[0].title", not(emptyString()));
    }

    @Test
    @Story("Get Single Album")
    @DisplayName("GET /albums/1 returns correct album, passes schema, responds within 3s")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies all album fields, JSON schema validation, and response time")
    void getSingleAlbumReturns200() {
        given()
            .when()
                .get("/albums/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("GET /albums/101 returns 404 — first ID over boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 101, one above the last seeded album, returns HTTP 404")
    void getAlbumOverBoundaryReturns404() {
        given()
            .when()
                .get("/albums/101")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /albums?userId=1 returns exactly 10 albums all owned by user 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies userId filter returns exactly 10 albums and every item belongs to user 1")
    void getAlbumsByUserIdFilter() {
        given()
            .queryParam("userId", 1)
            .when()
                .get("/albums")
            .then()
                .statusCode(200)
                .body("$", hasSize(10))
                .body("userId", everyItem(equalTo(1)));
    }
}
