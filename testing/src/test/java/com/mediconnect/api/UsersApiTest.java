package com.mediconnect.api;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

public class UsersApiTest extends BaseApiTest {

    @Test
    public void testGetTotalUsers() {
        given()
                .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
        .when()
            .get("/gettotalusers")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }
}
