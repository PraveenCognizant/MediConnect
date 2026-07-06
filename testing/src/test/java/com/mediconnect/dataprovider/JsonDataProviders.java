package com.mediconnect.dataprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class JsonDataProviders {

    private static Object[][] readJsonData(String filePath, String arrayName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, List<List<String>>> dataMap = mapper.readValue(
                    new File("src/test/resources/testdata/" + filePath),
                    new TypeReference<Map<String, List<List<String>>>>() {}
            );

            List<List<String>> listData = dataMap.get(arrayName);
            if (listData == null) {
                return new Object[0][0];
            }

            Object[][] result = new Object[listData.size()][];
            String timestamp = String.valueOf(System.currentTimeMillis());
            String datePlus21 = LocalDate.now().plusDays(21).toString();

            for (int i = 0; i < listData.size(); i++) {
                List<String> row = listData.get(i);
                result[i] = new Object[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    String val = row.get(j);
                    if (val != null) {
                        val = val.replace("[TIMESTAMP]", timestamp);
                        if (val.contains("[TIMESTAMP_5]")) {
                            val = val.replace("[TIMESTAMP_5]", timestamp.substring(timestamp.length() - 5));
                        }
                        val = val.replace("[DATE_PLUS_21]", datePlus21);
                    }
                    result[i][j] = val;
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data from " + filePath, e);
        }
    }

    @DataProvider(name = "validCredentials")
    public static Object[][] validCredentials() {
        return readJsonData("loginData.json", "validCredentials");
    }

    @DataProvider(name = "invalidCredentials")
    public static Object[][] invalidCredentials() {
        return readJsonData("loginData.json", "invalidCredentials");
    }

    @DataProvider(name = "invalidEmailFormats")
    public static Object[][] invalidEmailFormats() {
        return readJsonData("loginData.json", "invalidEmailFormats");
    }

    @DataProvider(name = "doctorRegistrationApprovalData")
    public static Object[][] doctorRegistrationApprovalData() {
        return readJsonData("registrationData.json", "doctorRegistrationApprovalData");
    }

    @DataProvider(name = "appointmentFlowData")
    public static Object[][] appointmentFlowData() {
        return readJsonData("appointmentData.json", "appointmentFlowData");
    }

    @DataProvider(name = "doctorPrescriptionData")
    public static Object[][] doctorPrescriptionData() {
        return readJsonData("prescriptionData.json", "doctorPrescriptionData");
    }

    @DataProvider(name = "patientRegistrationData")
    public static Object[][] patientRegistrationData() {
        return readJsonData("registrationData.json", "patientRegistrationData");
    }
}
