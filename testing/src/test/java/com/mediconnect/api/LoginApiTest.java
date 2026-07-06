package com.mediconnect.api;

import com.mediconnect.dataprovider.JsonDataProviders;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginApiTest extends BaseApiTest {

    @Test
    public void testUserLoginSuccess() {
        Object[][] validCreds = JsonDataProviders.validCredentials();
        String email = "";
        String password = "";
        for (Object[] row : validCreds) {
            if ("patient".equalsIgnoreCase((String) row[0])) {
                email = (String) row[1];
                password = (String) row[2];
                break;
            }
        }

        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", email);
        loginPayload.put("password", password);

        given()
            .contentType(ContentType.JSON)
            .body(loginPayload)
        .when()
            .post("/loginuser")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("email", equalTo(email));
    }

    @Test
    public void testUserLoginFailure() {
        Object[][] validCreds = JsonDataProviders.validCredentials();
        String email = (String) validCreds[2][1]; // Get a valid email

        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", email);
        loginPayload.put("password", "Wrong@123");

        given()
            .contentType(ContentType.JSON)
            .body(loginPayload)
        .when()
            .post("/loginuser")
        .then()
            .statusCode(500);
    }
}
