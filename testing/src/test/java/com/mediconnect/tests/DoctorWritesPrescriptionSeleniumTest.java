package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.AddPrescriptionPage;
import com.mediconnect.pages.LoginPage;
import com.mediconnect.pages.PrescriptionListPage;
import com.mediconnect.dataprovider.JsonDataProviders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

public class DoctorWritesPrescriptionSeleniumTest extends BaseTest {

        @Test(dataProvider = "doctorPrescriptionData", dataProviderClass = JsonDataProviders.class)
        public void doctorPraveenWritesPrescriptionAndPatientSeesIt(

                        String doctorEmail,
                        String doctorPassword,
                        String patientEmail,
                        String patientPassword,
                        String patientName) {
                // Login as doctor
                LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).openLoggedOut();
                loginPage.loginAsRole("doctor", doctorEmail, doctorPassword);
                Assert.assertTrue(loginPage.isOnPath("/doctordashboard"), "Doctor should land on doctordashboard");

                // Go to add prescription page
                AddPrescriptionPage addPrescriptionPage = new AddPrescriptionPage(DriverFactory.getDriver()).open();
                Assert.assertTrue(addPrescriptionPage.isLoaded(),
                                "Add prescription page should load and show patient dropdown");

                // Check patient is present in dropdown
                // Use exact dropdown selection only if the display name matches.
                // If not, select the first available patient to keep the flow robust.
                try {
                        addPrescriptionPage.selectPatientContaining(patientName);
                } catch (Exception ex) {
                        addPrescriptionPage.selectFirstPatient();
                }
                addPrescriptionPage.ensureDoctorNamePresent();

                // Write a deterministic random prescription
                String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
                addPrescriptionPage.fillRandomPrescriptionFields(
                                "Hypertension",
                                "Male",
                                "45",
                                "Test Prescription Notes - " + uniqueSuffix);

                addPrescriptionPage.clickSubmit();

                // Login as patient and verify prescription appears
                loginPage.openLoggedOut();
                loginPage.loginAsRole("patient", patientEmail, patientPassword);
                Assert.assertTrue(loginPage.isOnPath("/userdashboard"), "Patient should land on userdashboard");

                PrescriptionListPage prescriptionListPage = new PrescriptionListPage(DriverFactory.getDriver()).open();
                Assert.assertTrue(prescriptionListPage.hasPrescriptionForPatient(patientName),
                                "Prescription list should contain patient name/prescription details for: "
                                                + patientName);

                // Also confirm page has the unique notes substring (best verification)
                Assert.assertTrue(
                                prescriptionListPage.pageContainsText("Test Prescription Notes - " + uniqueSuffix),
                                "Prescription notes should be visible in patient's prescription list");
        }
}
