package com.mediconnect.api;

import com.mediconnect.dataprovider.JsonDataProviders;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationApiTest extends BaseApiTest {

    @Test(dataProvider = "patientRegistrationData", dataProviderClass = JsonDataProviders.class)
    public void testUserRegistration(String name, String email, String gender, String age, String mobile, String address, String password) {
        // Ensure email uniqueness to prevent 409 Conflict
        String uniqueEmail = email.replace("@", System.currentTimeMillis() + "@");
        
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("username", name);
        userPayload.put("email", uniqueEmail);
        userPayload.put("password", password);
        userPayload.put("gender", gender);
        userPayload.put("age", age);
        userPayload.put("mobile", mobile);
        userPayload.put("address", address);

        given()
                .relaxedHTTPSValidation()
            .contentType(ContentType.JSON)
            .body(userPayload)
        .when()
            .post("/registeruser")
        .then()
            .statusCode(200)
            .body("email", equalTo(uniqueEmail))
            .body("username", equalTo(name))
            .body("token", notNullValue());
    }
}
