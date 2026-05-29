# API Test Plan

## 1. Overview

**Project:** API Testing with REST Assured  
**API Under Test:** JSONPlaceholder (`https://jsonplaceholder.typicode.com`)  
**Test Framework:** REST Assured + JUnit 5  
**Reporting:** Allure Reports  
**Date:** 2026-05-29

---

## 2. Scope

### In Scope
- GET, POST, PUT, PATCH, DELETE operations on the `/posts` resource
- Status code validation
- Response body validation (field presence, values, types)
- Response header validation (Content-Type)
- JSON schema validation for GET single resource
- Query parameter filtering

### Out of Scope
- Authentication/authorization (JSONPlaceholder has none)
- Data persistence across requests (JSONPlaceholder is a mock — writes are simulated)
- Performance/load testing
- Other resources (`/comments`, `/albums`, `/photos`, `/todos`, `/users`)

---

## 3. Test Approach

Tests are implemented using:
- **REST Assured** for HTTP request construction and response assertion
- **JUnit 5** as the test runner with `@Test`, `@BeforeAll`, and display annotations
- **Allure** annotations (`@Epic`, `@Feature`, `@Severity`, `@Description`) for structured reporting
- **JSON Schema Validator** (bundled with REST Assured) for schema-level validation

All test classes extend `BaseTest`, which configures the base URI and attaches the Allure REST Assured filter for capturing request/response details in the report.

---

## 4. Test Cases

### 4.1 GET Requests

| ID | Test Case | Expected Result |
|----|-----------|----------------|
| GET-01 | GET `/posts` — fetch all posts | 200, `application/json`, array with 100 items |
| GET-02 | GET `/posts/1` — fetch single post | 200, `id=1`, `userId=1`, fields non-empty, passes JSON schema |
| GET-03 | GET `/posts/99999` — non-existent resource | 404 |
| GET-04 | GET `/posts?userId=1` — filter by userId | 200, all returned items have `userId=1` |

### 4.2 POST Requests

| ID | Test Case | Expected Result |
|----|-----------|----------------|
| POST-01 | POST `/posts` with full payload | 201, body echoes payload, `id=101` |
| POST-02 | POST `/posts` with partial payload | 201, `id` present in response |
| POST-03 | POST `/posts` — verify Content-Type header | 201, `Content-Type: application/json` |

### 4.3 PUT / PATCH Requests

| ID | Test Case | Expected Result |
|----|-----------|----------------|
| PUT-01 | PUT `/posts/1` with full payload | 200, echoes back all updated fields |
| PUT-02 | PATCH `/posts/1` with partial payload | 200, `id=1`, updated `title` returned |
| PUT-03 | PUT `/posts/1` — verify Content-Type header | 200, `Content-Type: application/json` |

### 4.4 DELETE Requests

| ID | Test Case | Expected Result |
|----|-----------|----------------|
| DEL-01 | DELETE `/posts/1` — delete a post | 200, body is `{}` |
| DEL-02 | DELETE `/posts/1` — verify Content-Type header | 200, `Content-Type: application/json` |
| DEL-03 | DELETE `/posts/50` — delete another valid post | 200 |

---

## 5. Test Data

JSONPlaceholder is a public mock API seeded with static data:
- 100 posts (`/posts`)
- Posts 1–100 exist; anything outside returns 404
- POST always returns `id: 101` (simulated)
- PUT/PATCH/DELETE always return 200 (simulated, no persistence)

No test data setup or teardown is required.

---

## 6. Test Environment

| Component | Details |
|-----------|---------|
| Language | Java 17 |
| Build tool | Maven 3.9.x |
| Test framework | JUnit 5.12.2 |
| HTTP library | REST Assured 5.5.2 |
| Report tool | Allure 2.29.1 |
| CI/CD | GitHub Actions |
| Container | Docker (maven:3.9-eclipse-temurin-17) |
| API | https://jsonplaceholder.typicode.com |

---

## 7. How to Run

**Locally:**
```bash
mvn test
mvn allure:serve    # opens Allure report in browser
```

**Via Docker:**
```bash
docker build -t api-tests .
docker run --rm api-tests
```

**CI/CD:**  
Push to `main` or `master` branch triggers the GitHub Actions workflow automatically.

---

## 8. Risks and Assumptions

| Risk | Mitigation |
|------|-----------|
| JSONPlaceholder is a public external service | Tests are read-only safe; no side effects. If the service is down, tests fail — acceptable for a mock API lab. |
| Write operations are not persisted | Assertions target simulated echo responses only, never check persistence. |
| Network latency may cause flaky results | Tests have no hard timeout; REST Assured uses sensible defaults. |

---

## 9. Deliverables

- [x] Test plan (this document)
- [x] Maven project with REST Assured + JUnit 5
- [x] Test cases for GET, POST, PUT/PATCH, DELETE
- [x] JSON schema validation
- [x] Allure reporting integration
- [x] Dockerfile for containerization
- [x] GitHub Actions CI/CD pipeline
