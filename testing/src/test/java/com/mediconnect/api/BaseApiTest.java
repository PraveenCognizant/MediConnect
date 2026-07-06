package com.mediconnect.api;

import com.mediconnect.utils.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    @BeforeClass
    public void setupBaseURI() {
        String apiBaseUrl = ConfigReader.getProperty("apiBaseUrl");
        if (apiBaseUrl != null && !apiBaseUrl.isEmpty()) {
            RestAssured.baseURI = apiBaseUrl;
        } else {
            RestAssured.baseURI = "http://localhost:8081";
        }
    }
}
