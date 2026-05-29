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
@Feature("Photos")
@Owner("Nelly")
public class PhotosTest extends BaseTest {

    @Test
    @Story("List All Photos")
    @DisplayName("GET /photos returns 200 with exactly 5000 photos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies the full photo dataset is returned with the correct count of 5000 items")
    void getAllPhotosReturns200() {
        given()
            .when()
                .get("/photos")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", hasSize(5000))
                .body("[0].albumId", notNullValue())
                .body("[0].url", not(emptyString()))
                .body("[0].thumbnailUrl", not(emptyString()));
    }

    @Test
    @Story("Get Single Photo")
    @DisplayName("GET /photos/1 returns correct photo with url and thumbnailUrl, passes schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies all photo fields including both URL fields, JSON schema, and response time")
    void getSinglePhotoReturns200() {
        given()
            .when()
                .get("/photos/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("albumId", equalTo(1))
                .body("title", not(emptyString()))
                .body("url", not(emptyString()))
                .body("thumbnailUrl", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/photo-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("GET /photos/5001 returns 404 — first ID over boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 5001, one above the last seeded photo, returns HTTP 404")
    void getPhotoOverBoundaryReturns404() {
        given()
            .when()
                .get("/photos/5001")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /photos?albumId=1 returns exactly 50 photos all belonging to album 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies albumId filter returns exactly 50 photos and every item has albumId=1")
    void getPhotosByAlbumIdFilter() {
        given()
            .queryParam("albumId", 1)
            .when()
                .get("/photos")
            .then()
                .statusCode(200)
                .body("$", hasSize(50))
                .body("albumId", everyItem(equalTo(1)));
    }
}
