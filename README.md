# API Testing with REST Assured

Automated API test suite for [JSONPlaceholder](https://jsonplaceholder.typicode.com) built with REST Assured, JUnit 5, and Allure Reports.

## Tech Stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Maven | 3.9.x |
| REST Assured | 5.5.2 |
| JUnit 5 | 5.12.2 |
| Allure | 2.29.1 |

## Project Structure

```
src/test/java/com/amali/api/
├── BaseTest.java          # Base URI and filter configuration
├── GetRequestTest.java    # GET request tests
├── PostRequestTest.java   # POST request tests
├── PutRequestTest.java    # PUT and PATCH request tests
└── DeleteRequestTest.java # DELETE request tests

src/test/resources/
├── allure.properties
└── schemas/
    └── post-schema.json   # JSON schema for response validation
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker (optional)

## Running Tests Locally

```bash
mvn test
```

## Viewing the Allure Report

After running tests, launch the interactive report in your browser:

```bash
mvn allure:serve
```

## Running with Docker

```bash
docker build -t api-tests .
docker run --rm api-tests
```

## CI/CD

GitHub Actions runs the full test suite on every push to `main`. The Allure report is automatically published to GitHub Pages after each run.

**Live report:** https://nellybutera.github.io/api-testing

## Test Coverage

| Method | Endpoint | Tests |
|--------|----------|-------|
| GET | `/posts` | All posts, filter by userId, 404 |
| GET | `/posts/{id}` | Single post + JSON schema validation |
| POST | `/posts` | Create post, partial payload, headers |
| PUT | `/posts/{id}` | Full update, headers |
| PATCH | `/posts/{id}` | Partial update |
| DELETE | `/posts/{id}` | Delete post, headers |

**Total: 13 test cases**

Validations cover: status codes, response body fields, Content-Type headers, and JSON schema.
