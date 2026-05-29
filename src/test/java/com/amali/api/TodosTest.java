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
@Feature("Todos")
@Owner("Nelly")
public class TodosTest extends BaseTest {

    @Test
    @Story("List All Todos")
    @DisplayName("GET /todos returns 200 with exactly 200 todos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies the full todo dataset is returned with the correct count of 200 items")
    void getAllTodosReturns200() {
        given()
            .when()
                .get("/todos")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", hasSize(200))
                .body("[0].userId", notNullValue())
                .body("[0].title", not(emptyString()));
    }

    @Test
    @Story("Get Single Todo")
    @DisplayName("GET /todos/1 returns correct todo, completed is boolean, passes schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies fields, that completed is a boolean type, JSON schema, and response time")
    void getSingleTodoReturns200() {
        given()
            .when()
                .get("/todos/1")
            .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyString()))
                .body("completed", isA(Boolean.class))
                .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"))
                .time(lessThan(3000L), TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("Error Handling")
    @DisplayName("GET /todos/201 returns 404 — first ID over boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that ID 201, one above the last seeded todo, returns HTTP 404")
    void getTodoOverBoundaryReturns404() {
        given()
            .when()
                .get("/todos/201")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /todos?completed=true returns only completed todos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the completed filter returns results where every item has completed=true")
    void getCompletedTodosFilter() {
        given()
            .queryParam("completed", true)
            .when()
                .get("/todos")
            .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("completed", everyItem(equalTo(true)));
    }

    @Test
    @Story("Query Filtering")
    @DisplayName("GET /todos?userId=1 returns exactly 20 todos all owned by user 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies userId filter returns the correct count (20) and every item belongs to user 1")
    void getTodosByUserIdFilter() {
        given()
            .queryParam("userId", 1)
            .when()
                .get("/todos")
            .then()
                .statusCode(200)
                .body("$", hasSize(20))
                .body("userId", everyItem(equalTo(1)));
    }
}
