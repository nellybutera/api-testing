package com.amali.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Tests")
@Feature("Users")
public class UsersTest extends BaseTest {

    @Test
    @DisplayName("GET /users returns 200 with exactly 10 users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies the full user dataset is returned with correct count and non-empty fields on the first item")
    void getAllUsersReturns200() {
        given()
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", hasSize(10))
                .body("[0].id", notNullValue())
                .body("[0].name", not(emptyString()))
                .body("[0].username", not(emptyString()))
                .body("[0].email", containsString("@"));
    }

    @Test
    @DisplayName("GET /users/1 returns correct user with nested address and company, passes schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies all top-level and nested fields (address, geo, company) and JSON schema validation")
    void getSingleUserReturns200() {
        given()
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("name", not(emptyString()))
                .body("email", containsString("@"))
                .body("address.city", not(emptyString()))
                .body("address.geo.lat", not(emptyString()))
                .body("address.geo.lng", not(emptyString()))
                .body("company.name", not(emptyString()))
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("GET /users/10 returns 200 — upper boundary valid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies the last seeded user (id=10) returns HTTP 200")
    void getLastValidUserReturns200() {
        given()
            .when()
                .get("/users/10")
            .then()
                .statusCode(200)
                .body("id", equalTo(10));
    }

    @Test
    @DisplayName("GET /users/11 returns 404 — first ID over boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 11, one above the last seeded user, returns HTTP 404")
    void getUserOverBoundaryReturns404() {
        given()
            .when()
                .get("/users/11")
            .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /users?username=Bret returns exactly 1 matching user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that filtering by username returns one result with the correct username value")
    void getUserByUsernameFilter() {
        given()
            .queryParam("username", "Bret")
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].username", equalTo("Bret"));
    }
}
